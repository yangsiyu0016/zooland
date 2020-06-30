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
	
	public void addOutbound(Outbound outbound, String goodsAllocationId, BigDecimal number) {
		//获取出库单
		Outbound ob = outboundMapper.getOutboundByForeignKey(outbound.getForeignKey());
		//获取组装单详情表
		ProductAssembledMaterial material = materialMapper.getMaterialById(outbound.getForeignKey());
		//获取组装单
		ProductAssembled assembled = productAssembledMapper.getProductAssembledById(material.getProductAssembledId());
		
		GoodsAllocation goodsAllocation = gaMapper.getGoodsAllocationById(goodsAllocationId);
		
		OutboundDetail outboundDetail = new OutboundDetail();
		if(ob != null) {
			for(OutboundDetail detail: ob.getDetails()) {
				if(detail.getGoodsAllocation().getId().equals(goodsAllocationId)) {
					detail.setNumber(detail.getNumber().add(number));
					outboundDetailMapper.update(detail);
					break;
				}else if(!detail.getGoodsAllocation().getId().equals(goodsAllocationId)){
					outboundDetail.setId(UUID.randomUUID().toString());
					outboundDetail.setCtime(new Date());
					outboundDetail.setGoodsAllocation(goodsAllocation);
					outboundDetail.setNumber(number);
					outboundDetail.setOutboundId(ob.getId());
					outboundDetail.setProduct(material.getProduct());
					outboundDetailMapper.addDetail(outboundDetail);
					break;
				}
			}
		}else {
			outbound.setId(UUID.randomUUID().toString());
			outbound.setCode(assembled.getCode());
			outbound.setType("ZZ");
			outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
			outbound.setCtime(new Date());
			outbound.setWarehouse(assembled.getWarehouse());
			outboundMapper.addOutbound(outbound);
			outboundDetail.setId(UUID.randomUUID().toString());
			outboundDetail.setCtime(new Date());
			outboundDetail.setGoodsAllocation(goodsAllocation);
			outboundDetail.setNumber(number);
			outboundDetail.setOutboundId(outbound.getId());
			outboundDetail.setProduct(material.getProduct());
			outboundDetailMapper.addDetail(outboundDetail);
		}
		
		/*--------------更新仓库库存开始----------------*/
		Stock stock = stockMapper.getStock(material.getProduct().getId(), assembled.getWarehouse().getId());
		if(stock != null) {
			if(stock.getUsableNumber().subtract(number).compareTo(BigDecimal.ZERO) == 1) {
				//更新后可用数量
				BigDecimal after_usableNumber = stock.getUsableNumber().subtract(number);
				//更新后总额
				BigDecimal after_totalMoney = stock.getTotalMoney().subtract(number.multiply(stock.getCostPrice()));
				
				stock.setUsableNumber(after_usableNumber);
				stock.setTotalMoney(after_totalMoney);
				stockMapper.updateStock(stock);
			}else {
				throw new ZooException(ExceptionEnum.STOCK_NOT_ENOUGH);
			}
		}else {
			throw new ZooException(ExceptionEnum.STOCK_NOT_FOUND);
		}
		/*--------------更新仓库库存结束----------------*/
		/*--------------更新货位库存开始----------------*/
		//获取货位库存
		StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), goodsAllocationId);
		if(stockDetail != null) {
			if(stockDetail.getUsableNumber().subtract(number).compareTo(BigDecimal.ZERO) == 1) {
				stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(number));
				stockDetailMapper.updateStockDetail(stockDetail);
			}else {
				throw new ZooException(ExceptionEnum.STOCK_DETAIL_NO_ENOUGH);
			}
		}else {
			throw new ZooException(ExceptionEnum.STOCK_DETAIL_NOT_FOUND);
		}
		/*--------------更新货位库存结束----------------*/
		
		/*------------------库存变动明细开始----------------------*/
		JournalAccount account = new JournalAccount();
		account.setId(UUID.randomUUID().toString());
		account.setType(JournalAccountType.ASSEMBLED);
		account.setOrderCode(assembled.getCode());
		account.setOrderDetailId(material.getId());
		account.setStock(stock);
		account.setCkNumber(stock.getUsableNumber());
		account.setCkPrice(stock.getCostPrice());
		account.setTotalMoney(stock.getTotalMoney());
		account.setCtime(new Date());
		account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		journalAccountService.addJournalAccount(account);
		/*------------------库存变动明细结束----------------------*/
	}

	public List<Outbound> getOutboundByProductAssembledId(String id) {
		// TODO Auto-generated method stub
		ProductAssembled assembled = productAssembledMapper.getProductAssembledById(id);
		List<Outbound> list = new ArrayList<Outbound>();
		for(ProductAssembledMaterial material: assembled.getMaterials()) {
			Outbound outbound = outboundMapper.getOutboundByForeignKey(material.getId());
			if(outbound != null) {
				//ProductAssembledMaterial assembledMaterial = materialMapper.getMaterialById(outbound.getForeignKey());
				outbound.setProductAssembledMaterial(material);
				list.add(outbound);
			}
		}
		return list;
	}
}
