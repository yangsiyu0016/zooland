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
import com.zoo.mapper.erp.cost.CostDetailMapper;
import com.zoo.mapper.erp.cost.CostMapper;
import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.mapper.erp.inbound.InboundMapper;
import com.zoo.mapper.erp.outbound.OutboundDetailMapper;
import com.zoo.mapper.erp.outbound.OutboundMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.mapper.erp.purchase.PurchaseDetailMapper;
import com.zoo.mapper.erp.purchase.PurchaseMapper;
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
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.service.erp.JournalAccountService;

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
	StockMapper stockMapper;
	@Autowired
	OutboundDetailMapper outboundDetailMapper;
	@Autowired
	StockDetailMapper stockDetailMapper;
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	SpecParamMapper paramMapper;
	@Autowired
	PurchaseMapper purchaseMapper;
	@Autowired
	InboundMapper inboundMapper;
	@Autowired
	InboundDetailMapper inboundDetailMapper;
	@Autowired
	PurchaseDetailMapper purchaseDetailMapper;
	public List<Cost> getCostByForeignKey(String foreignKey){
		List<Cost> costs = costMapper.getCostByForeignKey(foreignKey);
		List<String> built = new ArrayList<String>();
		for(Cost cost:costs) {
			for(CostDetail detail:cost.getDetails()) {
				ProductSku sku = detail.getProductSku();
				if(!built.contains(sku.getProduct().getId())){
					//通用规格参数处理
					String genericSpec = sku.getProduct().getProductDetail().getGenericSpec();
					Map<String,String> map = new HashMap<String,String>();
					JSONObject obj = JSONObject.fromObject(genericSpec);
					Set<String> keyset = obj.keySet();
					for(String key:keyset) {
						SpecParam param = paramMapper.getParamById(key);
						map.put(param.getName(), StringUtils.isBlank(obj.getString(key))?"其它":obj.getString(key));
					}
					sku.getProduct().getProductDetail().setGenericSpec(map.toString());
					
					
					String ownSpec = sku.getOwnSpec();
					 map = new HashMap<String,String>();
					 obj  = JSONObject.fromObject(ownSpec);
					keyset = obj.keySet();
					for(String key:keyset) {
						SpecParam param = paramMapper.getParamById(key);
						map.put(param.getName(), obj.getString(key));
					}
					sku.setOwnSpec(map.toString());
					detail.setProductSku(sku);
					built.add(sku.getProduct().getId());
				}
			}
		}
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
		
		/*
		 * Sell sell = sellMapper.getSellById(cost.getForeignKey());
		 * 
		 * cost.setId(UUID.randomUUID().toString()); cost.setCtime(new Date());
		 * cost.setWarehouse(cost.getDetails().get(0).getWarehouse());
		 * costMapper.addCost(cost); //生成出库单 Outbound outbound = new Outbound();
		 * outbound.setId(UUID.randomUUID().toString());
		 * outbound.setCode(sell.getCode()); outbound.setCtime(new Date());
		 * outbound.setType("SELL");
		 * 
		 * outbound.setWarehouse(cost.getDetails().get(0).getWarehouse());
		 * outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		 * outbound.setCost(cost);
		 * 
		 * outboundMapper.addOutbound(outbound);
		 * 
		 * for(CostDetail detail:cost.getDetails()) {
		 * 
		 * Stock stock = stockMapper.getStock(detail.getProductSku().getId(),
		 * detail.getWarehouse().getId()); if(stock==null) throw new
		 * RuntimeException("库存不存在");
		 * 
		 * detail.setId(UUID.randomUUID().toString()); detail.setCostId(cost.getId());
		 * detailMapper.addCostDetail(detail);
		 * 
		 * OutboundDetail outboundDetail = new OutboundDetail();
		 * outboundDetail.setId(UUID.randomUUID().toString());
		 * outboundDetail.setGoodsAllocation(detail.getGoodsAllocation());
		 * outboundDetail.setNumber(detail.getNumber()); BigDecimal ckPrice =
		 * stock.getCostPrice(); outboundDetail.setPrice(ckPrice); BigDecimal totalMoney
		 * = detail.getNumber().multiply(ckPrice);
		 * outboundDetail.setTotalMoney(totalMoney);
		 * outboundDetail.setOrderDetailId(detail.getDetailId());
		 * outboundDetail.setOutboundId(outbound.getId());
		 * outboundDetail.setProductSku(detail.getProductSku());
		 * outboundDetail.setCtime(new Date());
		 * outboundDetailMapper.addDetail(outboundDetail);
		 * 
		 * //总库存改变后的可用数量 BigDecimal stock_afterUsableNumberChange =
		 * stock.getUsableNumber().subtract(detail.getNumber()); //总库存改变后的锁定数量
		 * BigDecimal after_costPrice = stock.getCostPrice(); BigDecimal
		 * after_totalMoney = stock.getTotalMoney().subtract(totalMoney);
		 * stock.setUsableNumber(stock_afterUsableNumberChange);
		 * 
		 * if(stock_afterUsableNumberChange.compareTo(BigDecimal.ZERO)==0) {
		 * after_totalMoney = new BigDecimal("0"); }else { after_costPrice =
		 * after_totalMoney.divide(stock_afterUsableNumberChange, 4,
		 * BigDecimal.ROUND_HALF_UP); } stock.setCostPrice(after_costPrice);
		 * stock.setTotalMoney(after_totalMoney);
		 * 
		 * stockMapper.updateStock(stock);
		 * 
		 * StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(),
		 * detail.getGoodsAllocation().getId()); if(stockDetail==null) throw new
		 * RuntimeException("货位库存不存在");
		 * 
		 * BigDecimal stockDetail_afterUsableNumberChange =
		 * stockDetail.getUsableNumber().subtract(detail.getNumber());
		 * stockDetail.setUsableNumber(stockDetail_afterUsableNumberChange);
		 * stockDetailMapper.updateStockDetail(stockDetail);
		 * 
		 * JournalAccount journalAccount = new JournalAccount();
		 * journalAccount.setId(UUID.randomUUID().toString());
		 * journalAccount.setType(JournalAccountType.Purchase);
		 * journalAccount.setOrderDetailId(detail.getId());
		 * journalAccount.setOrderCode(sell.getCode()); journalAccount.setStock(stock);
		 * journalAccount.setCkNumber(detail.getNumber());
		 * journalAccount.setCkPrice(ckPrice);
		 * journalAccount.setCkTotalMoney(totalMoney); journalAccount.setCtime(new
		 * Date()); journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.
		 * getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		 * journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		 * journalAccountService.addJournalAccount(journalAccount);
		 * 
		 * }
		 */
		
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
}
