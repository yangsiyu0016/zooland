package com.zoo.service.erp.inbound;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;

@Service
@Transactional
public class InboundDetailService {
	@Autowired
	InboundDetailMapper inboundDetailMapper;
	@Autowired
	InboundService inboundService;
	@Autowired
	StockService stockService;
	@Autowired
	StockDetailService stockDetailService;
	@Autowired
	JournalAccountService journalAccountService;
	public int deleteDetailByInboundId(String inboundId) {
		return inboundDetailMapper.deleteDetailByInboundId(inboundId);
		
	}
	public List<InboundDetail> getDetailByInboundForeignKey(String foreignKey) {
		// TODO Auto-generated method stub
		return inboundDetailMapper.getDetailByInboundForeignKey(foreignKey);
	}
	public void addDetail(InboundDetail detail) {
		inboundDetailMapper.addDetail(detail);
		
	}
	public InboundDetail updatePrice(String id,String price) {
		InboundDetail inboundDetail = this.getDetailById(id);
		if(inboundDetail.getFinished()) {
			throw new ZooException("已入库完成不能修改成本价");
		}
		inboundDetail.setPrice(new BigDecimal(price));
		inboundDetail.setTotalMoney(new BigDecimal(price).multiply(inboundDetail.getNumber()));
		
		inboundDetailMapper.update(inboundDetail);
		return inboundDetail;
	}
	public InboundDetail getDetailById(String id) {
		// TODO Auto-generated method stub
		return inboundDetailMapper.getDetailById(id);
	}
	public void inbound(String id) {
		InboundDetail detail = this.inboundDetailMapper.getDetailById(id);
		Inbound inbound = inboundService.getInboundById(detail.getInboundId());
		if(detail.getFinished()) {
			throw new ZooException("不能重复入库");
		}else {
			if(detail.getPrice().compareTo(BigDecimal.ZERO)==0) {
				throw new ZooException("成本价没有录入不能入库");
			}else {
				Stock stock = stockService.getStock(detail.getProduct().getId(), inbound.getWarehouse().getId());
				if(stock!=null) {
					StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), detail.getGoodsAllocation().getId());
					if(stockDetail!=null) {
						stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(detail.getNumber()));
						stockDetailService.updateStockDetail(stockDetail);
					}else {
						stockDetail = new StockDetail();
						stockDetail.setId(UUID.randomUUID().toString());
						stockDetail.setUsableNumber(detail.getNumber());
						stockDetail.setStockId(stock.getId());
						stockDetailService.addStockDetail(stockDetail);
					}
					BigDecimal after_usableNumber = stock.getUsableNumber().add(detail.getNumber());
					BigDecimal after_totalMoney = stock.getTotalMoney().add(detail.getTotalMoney());
					BigDecimal after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
					
					stock.setCostPrice(after_costPrice);
					stock.setUsableNumber(after_usableNumber);
					stock.setTotalMoney(after_totalMoney);
					stockService.updateStock(stock);
				}else {
					stock = new Stock();
					stock.setId(UUID.randomUUID().toString());
					stock.setWarehouse(inbound.getWarehouse());
					stock.setProduct(detail.getProduct());
					stock.setCostPrice(detail.getPrice());
					stock.setUsableNumber(detail.getNumber());
					stock.setTotalMoney(detail.getTotalMoney());
					stockService.addStock(stock);
					
					StockDetail stockDetail = new StockDetail();
					stockDetail.setId(UUID.randomUUID().toString());
					stockDetail.setStockId(stock.getId());
					stockDetail.setGoodsAllocation(detail.getGoodsAllocation());
					stockDetail.setUsableNumber(detail.getNumber());
					stockDetailService.addStockDetail(stockDetail);
				}
				
				JournalAccount journalAccount = new JournalAccount();
				journalAccount.setId(UUID.randomUUID().toString());
				journalAccount.setType(JournalAccountType.SPLITRK);
				journalAccount.setOrderDetailId(detail.getId());
				journalAccount.setOrderCode(inbound.getCode());
				journalAccount.setStock(stock);
				journalAccount.setRkNumber(detail.getNumber());
				journalAccount.setRkPrice(detail.getPrice());
				journalAccount.setRkTotalMoney(detail.getTotalMoney());
				journalAccount.setCtime(new Date());
				journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
				journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				journalAccountService.addJournalAccount(journalAccount);
				this.updateFinished(id,true);
			}
			
		}
		
	}
	public void cancelInbound(String id) {
		InboundDetail inboundDetail = this.inboundDetailMapper.getDetailById(id);
		
		if(!inboundDetail.getFinished()) {
			throw new ZooException("已取消入库，请重新打开此页面");
		}else {
			Inbound inbound = inboundService.getInboundById(inboundDetail.getInboundId());
			Stock stock = stockService.getStock(inboundDetail.getProduct().getId(), inbound.getWarehouse().getId());
			if(stock.getUsableNumber().subtract(inboundDetail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
				throw new ZooException(inboundDetail.getProduct().getName()+"库存不足，不能取消");
			}else {
				StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), inboundDetail.getGoodsAllocation().getId());
				if(stockDetail.getUsableNumber().subtract(inboundDetail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
					throw new ZooException(inboundDetail.getProduct().getName()+"在货位"+inboundDetail.getGoodsAllocation().getName()+"上库存不足");
				}else {
					stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(inboundDetail.getNumber()));
					stockDetailService.updateStockDetail(stockDetail);
					
					BigDecimal after_usableNumber = stock.getUsableNumber().subtract(inboundDetail.getNumber());
					BigDecimal after_totalMoney = stock.getTotalMoney().subtract(inboundDetail.getTotalMoney());
					BigDecimal after_costPrice;
					if(after_usableNumber.compareTo(BigDecimal.ZERO)==0) {
						after_totalMoney = new BigDecimal("0");
						after_costPrice = stock.getCostPrice();
					}else {
						after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
					}
					
					stock.setCostPrice(after_costPrice);
					stock.setTotalMoney(after_totalMoney);
					stock.setUsableNumber(after_usableNumber);
					stockService.updateStock(stock);
					JournalAccount journalAccount = new JournalAccount();
					journalAccount.setId(UUID.randomUUID().toString());
					journalAccount.setType(JournalAccountType.CFCANCELRK);
					journalAccount.setOrderDetailId("");
					journalAccount.setOrderCode(inbound.getCode());
					journalAccount.setStock(stock);
					journalAccount.setCkNumber(inboundDetail.getNumber());
					journalAccount.setCkPrice(inboundDetail.getPrice());
					journalAccount.setCkTotalMoney(inboundDetail.getTotalMoney());
					journalAccount.setCtime(new Date());
					journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
					journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
					journalAccountService.addJournalAccount(journalAccount);
					
					this.updateFinished(id, false);
				}
				
			}
		}
	}
	public void updateFinished(String id, boolean finished) {
		inboundDetailMapper.updateFinished(id,finished);
		
	}
	public void deleteDetailById(String id) {
		inboundDetailMapper.deleteDetailById(id);
		
	}
	public void deleteByInboundId(String id) {
		inboundDetailMapper.deleteDetailByInboundId(id);
		
	}
	
}
