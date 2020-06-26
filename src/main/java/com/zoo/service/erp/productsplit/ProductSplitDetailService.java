package com.zoo.service.erp.productsplit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.mapper.erp.inbound.InboundMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitDetailMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.erp.warehouse.GoodsAllocation;

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
	}
	
	
}
