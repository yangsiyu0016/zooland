package com.zoo.service.erp.productsplit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.productsplit.ProductSplitDetailMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.inbound.InboundDetailService;
import com.zoo.service.erp.inbound.InboundService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;

@Service
public class ProductSplitDetailService {

	@Autowired
	private ProductSplitDetailMapper detailMapper;
	
	@Autowired
	StockService stockService;
	@Autowired
	StockDetailService stockDetailService;
	@Autowired
	InboundService inboundService;
	@Autowired
	InboundDetailService inboundDetailService;
	@Autowired
	GoodsAllocationMapper gaMapper;
	@Autowired
	ProductSplitService splitService;
	@Autowired
	JournalAccountService journalAccountService;
	public ProductSplitDetail addDetail(ProductSplitDetail detail) {
		String id = UUID.randomUUID().toString();
		detail.setId(id);
		detail.setCtime(new Date());
		detailMapper.addDetail(detail);
		return detail;
	}
	
	public int  updateDetail(ProductSplitDetail detail) {
		return detailMapper.updateDetail(detail);
	}
	
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		detailMapper.deleteDetailById(split);
	}

	public void updateNotInNumberById(BigDecimal notInNumber, String id) {
		// TODO Auto-generated method stub
		detailMapper.updateNumber(id, notInNumber);
	}

	public void deleteByProductSplitId(String id) {
		detailMapper.deleteByProductSplitId(id);
		
	}
	public void inbound(String id) {
		InboundDetail detail = this.inboundDetailService.getDetailById(id);
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
				inboundDetailService.updateFinished(id,true);
			}
			
		}
		
	}
	public void cancelInbound(String id) {
		InboundDetail inboundDetail = inboundDetailService.getDetailById(id);
		
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
					
					inboundDetailService.updateFinished(id, false);
				}
				
			}
		}
	}
}
