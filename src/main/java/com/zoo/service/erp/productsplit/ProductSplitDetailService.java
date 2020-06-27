package com.zoo.service.erp.productsplit;

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
import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.mapper.erp.inbound.InboundMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitDetailMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.erp.warehouse.GoodsAllocation;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;

@Service
public class ProductSplitDetailService {

	@Autowired
	private ProductSplitDetailMapper detailMapper;
	
	@Autowired
	StockMapper stockMapper;
	@Autowired
	StockDetailMapper stockDetailMapper;
	@Autowired
	InboundMapper inboundMapper;
	@Autowired
	InboundDetailMapper inboundDetailMapper;
	@Autowired
	GoodsAllocationMapper gaMapper;
	@Autowired
	ProductSplitMapper splitMapper;
	
	@Autowired
	private JournalAccountService journalAccountService;
	
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

	public void addInbound(Inbound inbound, String goodsAllocationId, BigDecimal number) {
		// TODO Auto-generated method stub
		//获取入库单
		Inbound ib = inboundMapper.getInboundByForeignKey(inbound.getForeignKey());	
		//获取拆分详情单
		ProductSplitDetail splitDetail = detailMapper.getDetailById(inbound.getForeignKey());
		//获取拆分单
		ProductSplit split = splitMapper.getProductSplitById(splitDetail.getProductSplitId());
		//获取货位
		GoodsAllocation goodsAllocation = gaMapper.getGoodsAllocationById(goodsAllocationId);
		InboundDetail inboundDetail = new InboundDetail();
		if(ib != null) {
			for(InboundDetail detail : ib.getDetails()) {
				if(detail.getGoodsAllocation().getId().equals(goodsAllocationId)) {//
					detail.setNumber(detail.getNumber().add(number));
					inboundDetailMapper.update(detail);
					break;
				}else if (!detail.getGoodsAllocation().getId().equals(goodsAllocationId)){
					inboundDetail.setId(UUID.randomUUID().toString());
					inboundDetail.setCtime(new Date());
					inboundDetail.setGoodsAllocation(goodsAllocation);
					inboundDetail.setNumber(number);
					inboundDetail.setInboundId(ib.getId());
					inboundDetail.setProduct(splitDetail.getProduct());
					inboundDetailMapper.addDetail(inboundDetail);
				}	
			}
		}else {
			inbound.setId(UUID.randomUUID().toString());
			inbound.setCode(split.getCode());
			inbound.setType("CF");
			inbound.setCuserId(LoginInterceptor.getLoginUser().getId());
			inbound.setCtime(new Date());
			inbound.setWarehouse(split.getWarehouse());
			inboundMapper.addInbound(inbound);
			inboundDetail.setId(UUID.randomUUID().toString());
			inboundDetail.setCtime(new Date());
			inboundDetail.setGoodsAllocation(goodsAllocation);
			inboundDetail.setNumber(number);
			inboundDetail.setInboundId(inbound.getId());
			inboundDetail.setProduct(splitDetail.getProduct());
			inboundDetailMapper.addDetail(inboundDetail);
		}
		/*--------------更新仓库库存开始----------------*/
		Stock stock = stockMapper.getStock(split.getProduct().getId(), split.getWarehouse().getId());
		
		if(stock != null) {
			//更新后使用数量
			BigDecimal after_usableNumber = stock.getUsableNumber().subtract(number);
			//更新后总额
			BigDecimal after_totalMoney = stock.getTotalMoney().subtract(after_usableNumber.multiply(stock.getCostPrice()));
			
			stock.setUsableNumber(after_usableNumber);
			stock.setTotalMoney(after_totalMoney);
			stockMapper.updateStock(stock);
		}else {
			throw new ZooException(ExceptionEnum.STOCK_NOT_FOUND);
		}
		/*--------------更新仓库库存结束----------------*/
		/*--------------更新货位库存开始----------------*/
		//获取货位库存
		StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), goodsAllocationId);
		if(stockDetail != null) {
			//更新可用数量
			stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(number));
			stockDetailMapper.updateStockDetail(stockDetail);
		}
		/*--------------更新货位库存结束----------------*/
		
		/*------------------库存变动明细开始----------------------*/
		JournalAccount account = new JournalAccount();
		account.setId(UUID.randomUUID().toString());
		account.setType(JournalAccountType.SPLIT);
		account.setOrderDetailId(splitDetail.getId());
		account.setOrderCode(split.getCode());
		account.setStock(stock);
		account.setCkNumber(stock.getUsableNumber());
		account.setCkPrice(stock.getCostPrice());
		account.setCkTotalMoney(stock.getTotalMoney());
		account.setCtime(new Date());
		account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		journalAccountService.addJournalAccount(account);
		/*------------------库存变动明细结束----------------------*/
	}
	
	public List<Inbound> getInboundByProductSplitId(String id){
		ProductSplit split = splitMapper.getProductSplitById(id);
		List<Inbound> list = new ArrayList<Inbound>();
		for(ProductSplitDetail detail : split.getDetails()) {
			Inbound inbound = inboundMapper.getInboundByForeignKey(detail.getId());
			if(inbound != null) {
				ProductSplitDetail splitDetail = detailMapper.getDetailById(inbound.getForeignKey());
				inbound.setSplitDetail(splitDetail);
				list.add(inbound);
			}
		}
		return list;
	}
}
