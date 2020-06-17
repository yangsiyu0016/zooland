package com.zoo.service.erp.cost;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.cost.CostDetailGoodsAllocationMapper;
import com.zoo.mapper.erp.cost.CostDetailMapper;
import com.zoo.mapper.erp.cost.CostMapper;
import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.mapper.erp.inbound.InboundMapper;
import com.zoo.mapper.erp.outbound.OutboundDetailMapper;
import com.zoo.mapper.erp.outbound.OutboundMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.mapper.erp.purchase.PurchaseDetailMapper;
import com.zoo.mapper.erp.purchase.PurchaseMapper;
import com.zoo.mapper.erp.sell.SellDetailMapper;
import com.zoo.mapper.erp.sell.SellMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.cost.Cost;
import com.zoo.model.erp.cost.CostDetail;
import com.zoo.model.erp.cost.CostDetailGoodsAllocation;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.outbound.Outbound;
import com.zoo.model.erp.outbound.OutboundDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.model.erp.purchase.PurchaseDetail;
import com.zoo.model.erp.sell.Sell;
import com.zoo.model.erp.sell.SellDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.outbound.OutboundService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class CostService {
	@Autowired
	CostMapper costMapper;
	@Autowired
	CostDetailMapper detailMapper;
	@Autowired
	SellMapper sellMapper;
	@Autowired
	OutboundMapper outboundMapper;
	@Autowired
	OutboundService outboundService;
	@Autowired
	StockMapper stockMapper;
	@Autowired
	OutboundDetailMapper outboundDetailMapper;
	@Autowired
	StockDetailMapper stockDetailMapper;
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	PurchaseMapper purchaseMapper;
	@Autowired
	InboundMapper inboundMapper;
	@Autowired
	InboundDetailMapper inboundDetailMapper;
	@Autowired
	PurchaseDetailMapper purchaseDetailMapper;
	@Autowired
	SellDetailMapper sellDetailMapper;
	@Autowired
	CostDetailGoodsAllocationMapper costDetailGoodsAllocationMapper;
	public List<Cost> getCostByForeignKey(String foreignKey){
		List<Cost> costs = costMapper.getCostByForeignKey(foreignKey);
		return costs;
	} 
	public void addCostFromPurchase(Cost cost) {
		//Purchase purchase = purchaseMapper.getPurchaseById(cost.getForeignKey());
		cost.setId(UUID.randomUUID().toString());
		cost.setWarehouse(cost.getDetails().get(0).getWarehouse());
		cost.setCtime(new Date());
		costMapper.addCost(cost);
		
		for(CostDetail detail:cost.getDetails()) {
			PurchaseDetail pdetail = purchaseDetailMapper.getDetailById(detail.getDetailId());
			BigDecimal notOutNumber =pdetail.getNotOutNumber().subtract(detail.getNumber());
			if(notOutNumber.compareTo(BigDecimal.ZERO)==-1) {
				throw new ZooException(ExceptionEnum.NUMBER_ERROR);
			}else {
				detail.setId(UUID.randomUUID().toString());
				detail.setCostId(cost.getId());
				
				detailMapper.addCostDetail(detail);
				
				purchaseDetailMapper.updateNotOutNumber(pdetail.getId(), notOutNumber);
				
			}
			
		}
	}
	public void addCostFromSell(Cost cost) {
		Sell sell = sellMapper.getSellById(cost.getForeignKey());
		cost.setId(UUID.randomUUID().toString());
		cost.setWarehouse(cost.getDetails().get(0).getWarehouse());
		cost.setCtime(new Date());
		costMapper.addCost(cost);
		
		Outbound outbound = new Outbound();
		outbound.setId(UUID.randomUUID().toString());
		outbound.setCode(sell.getCode());
		outbound.setType("SELL");
		outbound.setForeignKey(sell.getId());
		outbound.setCtime(new Date());
		outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		outbound.setCost(cost);
		outbound.setWarehouse(cost.getWarehouse());
		outboundMapper.addOutbound(outbound);
		
		for(CostDetail detail:cost.getDetails()) {
			SellDetail sdetail = sellDetailMapper.getDetailById(detail.getDetailId());
			BigDecimal notOutNumber =sdetail.getNotOutNumber();
			if(sdetail.getNotOutNumber().subtract(detail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
				throw new ZooException(ExceptionEnum.NUMBER_ERROR);
			}else {
				detail.setId(UUID.randomUUID().toString());
				detail.setCostId(cost.getId());
				
				
				Stock stock = stockMapper.getStock(detail.getProduct().getId(), outbound.getWarehouse().getId());
				
				detail.setPrice(stock.getCostPrice());
				detail.setTotalMoney(stock.getCostPrice().multiply(detail.getNumber()));
				
				detailMapper.addCostDetail(detail);
				for(CostDetailGoodsAllocation cdga:detail.getCdgas()) {
					cdga.setId(UUID.randomUUID().toString());
					cdga.setCostDetailId(detail.getId());
					
					costDetailGoodsAllocationMapper.addCostDetailGoodsAllocation(cdga);
					
					OutboundDetail outboundDetail = new OutboundDetail();
					outboundDetail.setId(UUID.randomUUID().toString());
					outboundDetail.setCtime(new Date());
					outboundDetail.setGoodsAllocation(cdga.getGoodsAllocation());
					outboundDetail.setNumber(cdga.getNumber());
					outboundDetail.setOrderDetailId(detail.getDetailId());
					outboundDetail.setOutboundId(outbound.getId());
					outboundDetail.setProduct(detail.getProduct());
					
					
					if(stock!=null) {
						StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), cdga.getGoodsAllocation().getId());
						if(stockDetail!=null) {
							BigDecimal stock_detail_usableNumber = stockDetail.getUsableNumber();
							
							if(stock_detail_usableNumber.subtract(cdga.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
								throw new ZooException(ExceptionEnum.STOCK_DETAIL_NO_ENOUGH);
							}else {
								//出库成本 价
								BigDecimal outPrice = stock.getCostPrice();
								//出库总额
								BigDecimal outTotalMoney = outPrice.multiply(cdga.getNumber());
								outboundDetail.setPrice(outPrice);
								outboundDetail.setTotalMoney(outTotalMoney);
								outboundDetailMapper.addDetail(outboundDetail);
								
								stockDetail.setUsableNumber(stock_detail_usableNumber.subtract(cdga.getNumber()));
								stockDetailMapper.updateStockDetail(stockDetail);
								
								stock.setUsableNumber(stock.getUsableNumber().subtract(cdga.getNumber()));
								
								if(stock.getUsableNumber().compareTo(BigDecimal.ZERO)==0) {
									stock.setTotalMoney(new BigDecimal("0"));
								}else {
									BigDecimal after_totalMoney = stock.getTotalMoney().subtract(outTotalMoney);
									BigDecimal after_costPrice = after_totalMoney.divide(stock.getUsableNumber(),4,BigDecimal.ROUND_HALF_UP);
									stock.setCostPrice(after_costPrice);
									stock.setTotalMoney(after_totalMoney);
								}
								stockMapper.updateStock(stock);
								
								JournalAccount journalAccount = new JournalAccount();
								journalAccount.setId(UUID.randomUUID().toString());
								journalAccount.setType(JournalAccountType.SELL);
								journalAccount.setOrderDetailId(detail.getId());
								journalAccount.setOrderCode(sell.getCode());
								journalAccount.setStock(stock);
								journalAccount.setCkNumber(cdga.getNumber());
								journalAccount.setCkPrice(outPrice);
								journalAccount.setCkTotalMoney(outTotalMoney);
								journalAccount.setCtime(new Date());
								journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
								journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
								journalAccountService.addJournalAccount(journalAccount);
								
								notOutNumber = notOutNumber.subtract(cdga.getNumber());
								sellDetailMapper.updateNotOutNumber(sdetail.getId(),notOutNumber);
							}
							
						}else {
							throw new ZooException(ExceptionEnum.STOCK_DETAIL_NOT_FOUND);
						}
						
					}else {
						throw new ZooException(ExceptionEnum.STOCK_NOT_FOUND);
					}	
				}
			}
			
		}
		
		
	}
	public void inboundFromPurchase(String costId) {
		Cost cost = costMapper.getCostById(costId);
		Warehouse warehouse = cost.getWarehouse();
		Purchase purchase = purchaseMapper.getPurchaseById(cost.getForeignKey());
		Inbound inbound = new Inbound();
		inbound.setId(UUID.randomUUID().toString());
		inbound.setCode(purchase.getCode());
		inbound.setCost(cost);
		inbound.setCtime(new Date());
		inbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		inbound.setForeignKey(purchase.getId());
		inbound.setType("PURCHASE");
		inbound.setWarehouse(cost.getWarehouse());
		
		inboundMapper.addInbound(inbound);
		
		for(CostDetail detail:cost.getDetails()) {
			PurchaseDetail purchaseDetail = purchaseDetailMapper.getDetailById(detail.getDetailId());
			
			for(CostDetailGoodsAllocation cdga:detail.getCdgas()) {
				InboundDetail inboundDetail = new InboundDetail();
				inboundDetail.setId(UUID.randomUUID().toString());
				inboundDetail.setCtime(new Date());
				inboundDetail.setInboundId(inbound.getId());
				inboundDetail.setProductSku(detail.getProductSku());
				inboundDetail.setGoodsAllocation(cdga.getGoodsAllocation());
				inboundDetail.setOrderDetailId(detail.getDetailId());
				inboundDetail.setNumber(cdga.getNumber());
				
				/**计算入库成本价开始**/
				
				/**计算入库成本价结束**/
				
				inboundDetail.setPrice(purchaseDetail.getPrice());
				inboundDetail.setTotalMoney(purchaseDetail.getPrice().multiply(cdga.getNumber()));
				inboundDetailMapper.addDetail(inboundDetail);
				
				Stock stock = stockMapper.getStock(detail.getProductSku().getId(), warehouse.getId());
				if(stock==null) {
					stock = new Stock();
					stock.setId(UUID.randomUUID().toString());
					stock.setWarehouse(warehouse);
					stock.setProductSku(detail.getProductSku());
					stock.setCostPrice(purchaseDetail.getPrice());
					stock.setUsableNumber(cdga.getNumber());
					stock.setTotalMoney(purchaseDetail.getPrice().multiply(cdga.getNumber()));
					
					stockMapper.addStock(stock);
					
					StockDetail stockDetail = new StockDetail();
					stockDetail.setId(UUID.randomUUID().toString());
					stockDetail.setGoodsAllocation(cdga.getGoodsAllocation());
					stockDetail.setStockId(stock.getId());
					stockDetail.setUsableNumber(cdga.getNumber());
					stockDetailMapper.addDetail(stockDetail);
				}else {
					//入库后结存总额
					BigDecimal afterInTotalMoney =  stock.getTotalMoney().add(purchaseDetail.getPrice().multiply(cdga.getNumber()));
					//入库后结存总数
					BigDecimal afterIntotalNumber = stock.getUsableNumber().add(cdga.getNumber());
					//入库后结存成本价
					BigDecimal afterInCostPrice = afterInTotalMoney.divide(afterIntotalNumber,4,BigDecimal.ROUND_HALF_UP);
					stock.setCostPrice(afterInCostPrice);
					stock.setUsableNumber(afterIntotalNumber);
					stock.setTotalMoney(afterInTotalMoney);
					stockMapper.updateStock(stock);
					
					StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), cdga.getGoodsAllocation().getId());
					if(stockDetail==null) {
						stockDetail = new StockDetail();
						stockDetail.setId(UUID.randomUUID().toString());
						stockDetail.setGoodsAllocation(cdga.getGoodsAllocation());
						stockDetail.setStockId(stock.getId());
						stockDetail.setUsableNumber(cdga.getNumber());
						stockDetailMapper.addDetail(stockDetail);
					}else {
						stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(cdga.getNumber()));
						stockDetailMapper.updateStockDetail(stockDetail);
					}
				}
				JournalAccount journalAccount = new JournalAccount();
				journalAccount.setId(UUID.randomUUID().toString());
				journalAccount.setType(JournalAccountType.PURCHASE);
				journalAccount.setOrderDetailId(detail.getId());
				journalAccount.setOrderCode(purchase.getCode());
				journalAccount.setStock(stock);
				journalAccount.setRkNumber(cdga.getNumber());
				journalAccount.setRkPrice(purchaseDetail.getPrice());
				journalAccount.setRkTotalMoney(purchaseDetail.getPrice().multiply(cdga.getNumber()));
				journalAccount.setCtime(new Date());
				journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
				journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				journalAccountService.addJournalAccount(journalAccount);
				
				BigDecimal notInNumber = purchaseDetail.getNotInNumber().subtract(cdga.getNumber());
				purchaseDetailMapper.updateNotInNumber(purchaseDetail.getId(), notInNumber);
			}
			costMapper.updateFinished(cost.getId(),true);
		}
	}
	public void deleteCostFromPurchase(String id) {
		List<CostDetail> details = detailMapper.getDetailByCostId(id);
		for(CostDetail detail:details) {
			PurchaseDetail purchaseDetail = purchaseDetailMapper.getDetailById(detail.getDetailId());
			BigDecimal notOutNumber = purchaseDetail.getNotOutNumber().add(detail.getNumber());
					
			purchaseDetailMapper.updateNotOutNumber(detail.getDetailId(), notOutNumber);
			detailMapper.deleteDetailById(detail.getId());
		}
		costMapper.deleteCostById(id);
	}
	/**
	 * 
	 * @param id
	 * @param type 删除或作废
	 */
	public void deleteCostFromSell(String id,String type) {
		
		Cost cost = costMapper.getCostById(id);
		Sell sell =sellMapper.getSellById(cost.getForeignKey());
		//List<CostDetail> details = detailMapper.getDetailByCostId(id);
		for(CostDetail detail:cost.getDetails()) {
			//更新未发货数量
			SellDetail sellDetail = sellDetailMapper.getDetailById(detail.getDetailId());
			sellDetailMapper.updateNotOutNumber(detail.getDetailId(), sellDetail.getNotOutNumber().add(detail.getNumber()));
			
			Stock stock = stockMapper.getStock(detail.getProduct().getId(), cost.getWarehouse().getId());
			//更新货位数量
			for(CostDetailGoodsAllocation cdga:detail.getCdgas()) {
				StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), cdga.getGoodsAllocation().getId());
				stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(cdga.getNumber()));
				stockDetailMapper.updateStockDetail(stockDetail);
				costDetailGoodsAllocationMapper.deleteCostDetailGoodsAllocationById(cdga.getId());
			}
			//更新库存数量
			stock.setUsableNumber(stock.getUsableNumber().add(detail.getNumber()));
			stock.setTotalMoney(stock.getTotalMoney().add(detail.getTotalMoney()));
			stock.setCostPrice(stock.getTotalMoney().divide(stock.getUsableNumber(),4,BigDecimal.ROUND_HALF_UP));
			
			stockMapper.updateStock(stock);
			
			//删除明细
			//journalAccountService.deleteByOrderDetailId(detail.getId());
			
			//删除出库单
			outboundService.deleteByCostId(id);
			
			detailMapper.deleteDetailById(detail.getId());
			
			JournalAccount journalAccount = new JournalAccount();
			journalAccount.setId(UUID.randomUUID().toString());
			if(type.equals("DESTROY")) {
				journalAccount.setType(JournalAccountType.SELLDESTROY);
			}else if(type.equals("DELETE")) {
				journalAccount.setType(JournalAccountType.SELLDETAILDELETE);
			}
			journalAccount.setOrderDetailId(detail.getId());
			journalAccount.setOrderCode(sell.getCode());
			journalAccount.setStock(stock);
			journalAccount.setRkNumber(detail.getNumber());
			journalAccount.setRkPrice(detail.getPrice());
			journalAccount.setRkTotalMoney(detail.getTotalMoney());
			journalAccount.setCtime(new Date());
			journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
			journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
			journalAccountService.addJournalAccount(journalAccount);
		}
		costMapper.deleteCostById(id);
		
	}
	public void deleteByForeignKey(String sellId) {
		List<Cost> costs = costMapper.getCostByForeignKey(sellId);
		for(Cost cost:costs) {
			List<CostDetail> details = cost.getDetails();
			for(CostDetail detail:details) {
				detailMapper.deleteDetailById(detail.getId());
			}
			costMapper.deleteCostById(cost.getId());
		}
		
	}
}
