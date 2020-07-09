package com.zoo.controller.erp.constant;

public class JournalAccountType {

	public static final String QC = "QC";
	public static final String SELL = "SELL";
	public static final String PURCHASE = "PURCHASE";
	public static final String LOSSES = "LOSSES";
	public static final String OVERFLOW = "OVERFLOW";
	public static final String QCDESTROY = "QCDESTROY";
	public static final String SELLDESTROY = "SELLDESTROY";//销售单作废
	public static final String SELLDETAILDELETE = "SELLDETAILDELETE";//销售单出库撤销
	public static final String SPLIT = "SPLIT";//销售单出库撤销
	public static final String CFDESTROY = "CFDESTROY";//拆分单作废
	public static final String ASSEMBLEDCK = "ASSEMBLEDCK";//组装单出库
	public static final String ASSEMBLEDRK = "ASSEMBLEDRK";//组装单入库
	public static final String ASSEMBLEDDESTROY = "ASSEMBLEDDESTROY";//组装单作废
	public static final String ASSEMBLEDCKDELETE = "ASSEMBLEDCKDELETE";//组装单出库删除
	public static final String ASSEMBLEDRKDELETE = "ASSEMBLEDRKDELETE";//组装单入库删除
	public static final String PURCHASEDESTROY = "PURCHASEDESTROY";//采购单作废
	public static final String SPLITCK = "SPLITCK";//拆分单出库
	public static final String SPLITCKDELETE = "SPLITCKDELETE";//拆分单出库删除
	public static final String SPLITRK = "SPLITRK";//拆分单入库
	public static final String CFCANCELRK = "CFCANCELRK";//拆分单取消入库
	public static final String SPLITRKDELETE = "SPLITRKDELETE";//拆分单入库删除
}
