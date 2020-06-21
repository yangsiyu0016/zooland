package com.zoo.model.erp.productsplit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zoo.model.erp.warehouse.GoodsAllocation;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class ProductSplit {

	private String id;
	private String code;//拆分单编号
	private Warehouse warehouse;//仓库
	private Date splitTime;//拆分时间
	private BigDecimal number;//拆分数量
	private String batchNumber;//批号
	private SystemUser splitMan;//拆分人
	private Date ctime;//创建时间
	private Date etime;//结束时间
	private String status;//状态
	private String description;//备注
	private String companyId;//公司id
	private String processInstanceId;//流程id
	private BigDecimal notOutNumber;//未出库数量
	private GoodsAllocation goodsAllocation;//出库货位id
	private List<ProductSplitDetail> details;//详情表
	
	private String codeGeneratorType;//编号生成方式
	
}
