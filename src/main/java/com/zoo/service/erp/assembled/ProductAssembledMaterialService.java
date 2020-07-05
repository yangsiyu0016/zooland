package com.zoo.service.erp.assembled;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.assembled.ProductAssembledMapper;
import com.zoo.mapper.erp.assembled.ProductAssembledMaterialMapper;
import com.zoo.mapper.erp.outbound.OutboundDetailMapper;
import com.zoo.mapper.erp.outbound.OutboundMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.assembled.ProductAssembledMaterial;
import com.zoo.model.erp.outbound.Outbound;
import com.zoo.model.erp.outbound.OutboundDetail;
import com.zoo.model.erp.warehouse.GoodsAllocation;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.warehouse.StockService;

@Service
public class ProductAssembledMaterialService {

	@Autowired
	private ProductAssembledMaterialMapper materialMapper;
	@Autowired
	private ProductAssembledMapper productAssembledMapper;
	@Autowired
	StockMapper stockMapper;
	@Autowired
	StockDetailMapper stockDetailMapper;
	@Autowired
	OutboundMapper outboundMapper;
	@Autowired
	OutboundDetailMapper outboundDetailMapper;
	@Autowired
	GoodsAllocationMapper gaMapper;
	@Autowired
	private JournalAccountService journalAccountService;
	
	public int updateNotOutNumber(BigDecimal notOutNumber, String id) {
		return materialMapper.updateNotOutNumber(notOutNumber, id);
	}
	
	public ProductAssembledMaterial getMaterialById(String id) {
		return materialMapper.getMaterialById(id);
	}
	
	/*
	 * public void addOutbound(Outbound outbound, String goodsAllocationId,
	 * BigDecimal number) { Outbound ob =
	 * outboundMapper.getOutboundByForeignKey(outbound.getForeignKey());
	 * ProductAssembledMaterial material =
	 * materialMapper.getMaterialById(outbound.getForeignKey()); ProductAssembled
	 * assembled =
	 * productAssembledMapper.getProductAssembledById(material.getProductAssembledId
	 * ());
	 * 
	 * GoodsAllocation goodsAllocation =
	 * gaMapper.getGoodsAllocationById(goodsAllocationId);
	 * 
	 * OutboundDetail outboundDetail = new OutboundDetail(); if(ob != null) {
	 * for(OutboundDetail detail: ob.getDetails()) {
	 * if(detail.getGoodsAllocation().getId().equals(goodsAllocationId)) {
	 * detail.setNumber(detail.getNumber().add(number));
	 * outboundDetailMapper.update(detail); break; }else
	 * if(!detail.getGoodsAllocation().getId().equals(goodsAllocationId)){
	 * outboundDetail.setId(UUID.randomUUID().toString());
	 * outboundDetail.setCtime(new Date());
	 * outboundDetail.setGoodsAllocation(goodsAllocation);
	 * outboundDetail.setNumber(number); outboundDetail.setOutboundId(ob.getId());
	 * outboundDetail.setProduct(material.getProduct());
	 * outboundDetailMapper.addDetail(outboundDetail); break; } } }else {
	 * outbound.setId(UUID.randomUUID().toString());
	 * outbound.setCode(assembled.getCode()); outbound.setType("ZZ");
	 * outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
	 * outbound.setCtime(new Date());
	 * outbound.setWarehouse(assembled.getWarehouse());
	 * outboundMapper.addOutbound(outbound);
	 * outboundDetail.setId(UUID.randomUUID().toString());
	 * outboundDetail.setCtime(new Date());
	 * outboundDetail.setGoodsAllocation(goodsAllocation);
	 * outboundDetail.setNumber(number);
	 * outboundDetail.setOutboundId(outbound.getId());
	 * outboundDetail.setProduct(material.getProduct());
	 * outboundDetailMapper.addDetail(outboundDetail); }
	 * 
	 * 
	 * Stock stock = stockMapper.getStock(material.getProduct().getId(),
	 * assembled.getWarehouse().getId()); if(stock != null) {
	 * if(stock.getUsableNumber().subtract(number).compareTo(BigDecimal.ZERO) == 1)
	 * {
	 * 
	 * BigDecimal after_usableNumber = stock.getUsableNumber().subtract(number);
	 * 
	 * BigDecimal after_totalMoney =
	 * stock.getTotalMoney().subtract(number.multiply(stock.getCostPrice()));
	 * 
	 * stock.setUsableNumber(after_usableNumber);
	 * stock.setTotalMoney(after_totalMoney); stockMapper.updateStock(stock); }else
	 * { throw new ZooException(ExceptionEnum.STOCK_NOT_ENOUGH); } }else { throw new
	 * ZooException(ExceptionEnum.STOCK_NOT_FOUND); } StockDetail stockDetail =
	 * stockDetailMapper.getDetail(stock.getId(), goodsAllocationId); if(stockDetail
	 * != null) {
	 * if(stockDetail.getUsableNumber().subtract(number).compareTo(BigDecimal.ZERO)
	 * == 1) {
	 * stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(number));
	 * stockDetailMapper.updateStockDetail(stockDetail); }else { throw new
	 * ZooException(ExceptionEnum.STOCK_DETAIL_NO_ENOUGH); } }else { throw new
	 * ZooException(ExceptionEnum.STOCK_DETAIL_NOT_FOUND); } JournalAccount account
	 * = new JournalAccount(); account.setId(UUID.randomUUID().toString());
	 * account.setType(JournalAccountType.ASSEMBLED);
	 * account.setOrderCode(assembled.getCode());
	 * account.setOrderDetailId(material.getId()); account.setStock(stock);
	 * account.setCkNumber(stock.getUsableNumber());
	 * account.setCkPrice(stock.getCostPrice());
	 * account.setTotalMoney(stock.getTotalMoney()); account.setCtime(new Date());
	 * account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==
	 * null?new BigDecimal("0"):stock.getLockedNumber()));
	 * account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
	 * journalAccountService.addJournalAccount(account); }
	 */

	public void addOutbound(Outbound outbound) {
		ProductAssembled assembled = productAssembledMapper.getProductAssembledById(outbound.getForeignKey());
		/*新增出库单*/
		outbound.setId(UUID.randomUUID().toString());
		outbound.setCode(assembled.getCode());
		outbound.setCtime(new Date());
		outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		outbound.setWarehouse(assembled.getWarehouse());
		outbound.setType("ZZ");
		outboundMapper.addOutbound(outbound);
		
		for(OutboundDetail detail: outbound.getDetails()) {
			ProductAssembledMaterial material = materialMapper.getMaterialById(detail.getOrderDetailId());
			//设置出库详情单
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setOutboundId(outbound.getId());
			detail.setProduct(material.getProduct());
			//获取库存单
			Stock stock = stockMapper.getStock(material.getProduct().getId(), assembled.getWarehouse().getId());
			
			if(stock != null) {
				if(stock.getUsableNumber().subtract(detail.getNumber()).compareTo(BigDecimal.ZERO) == -1) {//库存不足
					throw new ZooException(material.getProduct().getName() + "库存不足");
				}else {
					StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), detail.getGoodsAllocation().getId());
					if(stockDetail != null) {
						if(stockDetail.getUsableNumber().subtract(detail.getNumber()).compareTo(BigDecimal.ZERO) == -1) {//货位库存不足
							throw new ZooException(detail.getGoodsAllocation().getName() + "货位库存不足");
						}else {
							stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(detail.getNumber()));
							stockDetailMapper.updateStockDetail(stockDetail);
							
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
							stockMapper.updateStock(stock);
							detail.setPrice(ckPrice);
							detail.setTotalMoney(ckTotalMoney);
							//添加出库详情单
							outboundDetailMapper.addDetail(detail);
							
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
							BigDecimal notOutNumber = material.getNotOutNumber().subtract(detail.getNumber());
							this.updateNotOutNumber(notOutNumber, material.getId());
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
	
	public List<Outbound> getOutboundByProductAssembledId(String id) {
		// TODO Auto-generated method stub
		ProductAssembled assembled = productAssembledMapper.getProductAssembledById(id);
		List<Outbound> list = new ArrayList<Outbound>();
		for(ProductAssembledMaterial material: assembled.getMaterials()) {
			Outbound outbound = outboundMapper.getOutboundByForeignKey(material.getId());
			if(outbound != null) {
				//ProductAssembledMaterial assembledMaterial = materialMapper.getMaterialById(outbound.getForeignKey());
				//outbound.setProductAssembledMaterial(material);
				list.add(outbound);
			}
		}
		return null;
	}
}
