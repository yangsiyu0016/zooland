package com.zoo.service.erp.assembled;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.assembled.ProductAssembledMaterialMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.assembled.ProductAssembledMaterial;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.outbound.Outbound;
import com.zoo.model.erp.outbound.OutboundDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.inbound.InboundDetailService;
import com.zoo.service.erp.inbound.InboundService;
import com.zoo.service.erp.outbound.OutboundDetailService;

import com.zoo.service.erp.warehouse.GoodsAllocationService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;import com.zoo.service.erp.outbound.OutboundService;

@Service
public class ProductAssembledMaterialService {

	@Autowired
	private ProductAssembledMaterialMapper materialMapper;
	@Autowired
	private ProductAssembledService productAssembledService;
	@Autowired
	StockService stockService;
	@Autowired
	StockDetailService stockDetailService;
	@Autowired
	GoodsAllocationService gaService;
	@Autowired
	private JournalAccountService journalAccountService;
	
	@Autowired
	private OutboundService outboundService;
	@Autowired
	private OutboundDetailService outboundDetailService;
	@Autowired
	private InboundService inboundService;
	@Autowired
	private InboundDetailService inboundDetailService;
	public int updateNotOutNumber(BigDecimal notOutNumber, String id) {
		return materialMapper.updateNotOutNumber(notOutNumber, id);
	}
	
	public ProductAssembledMaterial getMaterialById(String id) {
		return materialMapper.getMaterialById(id);
	}
	
	public void addOutbound(Outbound outbound) {
		ProductAssembled assembled = productAssembledService.getProductAssembledById(outbound.getForeignKey());
		/*新增出库单*/
		outbound.setId(UUID.randomUUID().toString());
		outbound.setCode(assembled.getCode());
		outbound.setCtime(new Date());
		outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		outbound.setWarehouse(assembled.getWarehouse());
		outbound.setType("ZZ");
		outboundService.addOutbound(outbound);
		
		for(OutboundDetail detail: outbound.getDetails()) {
			
			//设置出库详情单
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setOutboundId(outbound.getId());
			//获取库存单
			Stock stock = stockService.getStock(detail.getProduct().getId(), assembled.getWarehouse().getId());
			
			if(stock != null) {
				if(stock.getUsableNumber().subtract(detail.getNumber()).compareTo(BigDecimal.ZERO) == -1) {//库存不足
					throw new ZooException(detail.getProduct().getName() + "库存不足");
				}else {
					StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), detail.getGoodsAllocation().getId());
					if(stockDetail != null) {
						if(stockDetail.getUsableNumber().subtract(detail.getNumber()).compareTo(BigDecimal.ZERO) == -1) {//货位库存不足
							throw new ZooException(detail.getGoodsAllocation().getName() + "货位库存不足");
						}else {
							stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(detail.getNumber()));
							stockDetailService.updateStockDetail(stockDetail);
							
							BigDecimal after_usableNumber = stock.getUsableNumber().subtract(detail.getNumber());
							BigDecimal after_totalMoney, after_costPrice, ckTotalMoney, ckPrice;
							stock.setUsableNumber(after_usableNumber);
							if(after_usableNumber.compareTo(BigDecimal.ZERO) == 0) {
								ckTotalMoney = stock.getTotalMoney();
								ckPrice = stock.getCostPrice();
								after_totalMoney = new BigDecimal("0");
								after_costPrice = stock.getCostPrice();
							}else {
								ckPrice = stock.getCostPrice();
								ckTotalMoney = ckPrice.multiply(detail.getNumber());
								after_totalMoney = stock.getTotalMoney().subtract(ckTotalMoney);
								after_costPrice = after_totalMoney.divide(after_usableNumber, 4, BigDecimal.ROUND_HALF_UP);
							}
							stock.setCostPrice(after_costPrice);
							stock.setTotalMoney(after_totalMoney);
							stockService.updateStock(stock);
							detail.setPrice(ckPrice);
							detail.setTotalMoney(ckTotalMoney);
							//添加出库详情单
							outboundDetailService.addDetail(detail);
							
							/*添加库存变动明细*/
							JournalAccount account = new JournalAccount();
							account.setId(UUID.randomUUID().toString());
							account.setType(JournalAccountType.ASSEMBLEDCK);
							account.setOrderDetailId(detail.getId());
							account.setOrderCode(assembled.getCode());
							account.setStock(stock);
							account.setCkNumber(detail.getNumber());
							account.setCkPrice(ckPrice);
							account.setCkTotalMoney(ckTotalMoney);
							account.setCtime(new Date());
							account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
							account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
							journalAccountService.addJournalAccount(account);
							
							//更新组装单未出库数量
							List<ProductAssembledMaterial> materials = assembled.getMaterials();
							for(ProductAssembledMaterial material: materials) {
								if(material.getProduct().getId().equals(detail.getProduct().getId())) {
									BigDecimal notOutNumber = material.getNotOutNumber().subtract(detail.getNumber());
									material.setNotOutNumber(notOutNumber);
									this.updateNotOutNumber(notOutNumber, material.getId());
								}
							}
						}
					}else {
						throw new ZooException(detail.getGoodsAllocation().getName()+"货位库存不存在");
					}
				}
			}else {
				throw new ZooException(assembled.getProduct().getName()+"库存不存在");
			}
		}
	}

	/**
	 *	删除出库信息
	 * @param assembledId
	 * @param outboundDetailId
	 * @param type
	 * @return
	 */
	public void deleteOut(String assembledId, String outboundDetailId, String type) {
		ProductAssembled assembled = productAssembledService.getProductAssembledById(assembledId);
		OutboundDetail outboundDetail = outboundDetailService.getDetailById(outboundDetailId);
		Outbound outbound = outboundService.getOutboundById(outboundDetail.getOutboundId());
		if("all".equals(type)) {//删除所有
			for(OutboundDetail detail: outbound.getDetails()) {
				deleteOutDetail(detail, outbound);

				for(ProductAssembledMaterial material: assembled.getMaterials()) {
					if(detail.getProduct().getId().equals(material.getProduct().getId())) {
						BigDecimal notOutNumber = material.getNotOutNumber().add(detail.getNumber());
						material.setNotOutNumber(notOutNumber);
						materialMapper.updateNotOutNumber(notOutNumber,material.getId());
					}
				}
				outboundService.deleteById(outbound.getId());
			}
			
		}else {
			deleteOutDetail(outboundDetail, outbound);
			outboundDetailService.deleteDetailById(outboundDetailId);
			boolean hasDetails = outboundService.checkHasDetails(outbound.getId());
			if(!hasDetails) {
				outboundService.deleteById(outbound.getId());
			}
			for(ProductAssembledMaterial material:assembled.getMaterials()) {
				if(outboundDetail.getProduct().getId().equals(material.getProduct().getId())) {
					materialMapper.updateNotOutNumber(material.getNotOutNumber().add(outboundDetail.getNumber()), material.getId());
					break;
				}
			}
		}
	}
	
	private void deleteOutDetail(OutboundDetail outboundDetail, Outbound outbound) {
		Stock stock = stockService.getStock(outboundDetail.getProduct().getId(), outbound.getWarehouse().getId());
		
		StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), outboundDetail.getGoodsAllocation().getId());
		stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(outboundDetail.getNumber()));
		stockDetailService.updateStockDetail(stockDetail);
		
		BigDecimal after_usableNumber = stock.getUsableNumber().add(outboundDetail.getNumber());
		BigDecimal after_totalMoney = stock.getTotalMoney().add(outboundDetail.getTotalMoney());
		BigDecimal after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
		stock.setUsableNumber(after_usableNumber);
		stock.setTotalMoney(after_totalMoney);
		stock.setCostPrice(after_costPrice);
		stockService.updateStock(stock);
		
		JournalAccount journalAccount = new JournalAccount();
		journalAccount.setId(UUID.randomUUID().toString());
		journalAccount.setType(JournalAccountType.ASSEMBLEDCKDELETE);
		journalAccount.setOrderDetailId("");
		journalAccount.setOrderCode(outbound.getCode());
		journalAccount.setStock(stock);
		journalAccount.setRkNumber(outboundDetail.getNumber());
		journalAccount.setRkPrice(outboundDetail.getPrice());
		journalAccount.setRkTotalMoney(outboundDetail.getTotalMoney());
		journalAccount.setCtime(new Date());
		journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		journalAccountService.addJournalAccount(journalAccount);
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
					journalAccount.setType(JournalAccountType.ZZCANCELRK);
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
