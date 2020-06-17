package com.zoo.model.erp.aftersales.replenishmentnote;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.crm.Customer;
import com.zoo.model.crm.Receiving;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.model.erp.purchase.Supplier;
import com.zoo.model.erp.sell.Sell;
import com.zoo.model.system.base.Express;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

/**
 * 补货实体
 * @author aa
 *
 */
@Data
public class ReplenishmentNote {

	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date initDate;
	private String customerId;
	private String supplierId;
	private String status;
	private String cuserId;
	private String processInstanceId;
	private String companyId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date etime;
	private String isClaimed;
	private SystemUser cuser;
	private Customer customer;
	private Supplier supplier;
	private List<ReplenishmentNoteDetail> details;
	private String codeGeneratorType;
	private String reason;
	private String problemDescription;
	private Express express;//物流信息
	private Purchase purchase;//采购单
	private Sell sell;//销售单
	private Receiving receiving;//收货地址
	private String freightType;//运费类型
	private String freightPayType;//运费支付方式
	private BigDecimal freight;//运费
	private String paymentMode;//付款方式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date paytime;//付款时间
	private String paymentType;//付款类型
	private BigDecimal totalMoney;//补货商品总额
	private BigDecimal payable;//应付
	private BigDecimal actualPayment;//实际支付
}
