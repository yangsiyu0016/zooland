package com.zoo.service.erp.aftersales.replenishmentnote;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.ReplenishmentNoteStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.aftersales.replenishmentnote.ReplenishmentNoteDetailMapper;
import com.zoo.mapper.erp.aftersales.replenishmentnote.ReplenishmentNoteMapper;
import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNote;
import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNoteDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.utils.OrderCodeHelper;

@Service
public class ReplenishmentNoteService {

	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	ReplenishmentNoteDetailMapper detailMapper;
	
	@Autowired
	ReplenishmentNoteMapper replenishmentNoteMapper;
	
	/**
	 * 添加
	 */
	public void	addReplenishmentNote(ReplenishmentNote note) {
		String id = UUID.randomUUID().toString();
		
		note.setId(id);
		
		if("AUTO".equals(note.getCodeGeneratorType())) {
			try {
				note.setCode(OrderCodeHelper.GenerateId("BH"));
			} catch (Exception e) {
				// TODO: handle exception
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		note.setStatus(ReplenishmentNoteStatus.WTJ);
		note.setCuserId(LoginInterceptor.getLoginUser().getId());
		note.setCtime(new Date());
		note.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		note.setIsClaimed("N");
		
		for(ReplenishmentNoteDetail detail: note.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setReplenishmentNoteId(id);
			detail.setCtime(new Date());
			detailMapper.addDetail(detail);
		}
	}
	
	/**
	 * 分页查询
	 */
	public List<ReplenishmentNote> getReplenishmentNote(Integer page, Integer size) {
		Integer start = null;
		if(page != null) {
			start = (page -1) * size;
		}
		return replenishmentNoteMapper.getReplenishmentNoteByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 获取总条数
	 */
	public Long getCount () {
		return replenishmentNoteMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 更新补货单流程状态
	 */
	public void updateReplenishmentNoteStatus(Map<String, Object> condition) {
		replenishmentNoteMapper.updateReplenishmentNoteStatus(condition);
	}
	
	/**
	 * 根据id获取补货单
	 */
	public ReplenishmentNote getReplenishmentNoteById(String id) {
		ReplenishmentNote note = replenishmentNoteMapper.getReplenishmentNoteById(id);
		return note;
	}
	
	/**
	 * 开启流程
	 */
	public void startProcess(String id) {
		ReplenishmentNote note = this.getReplenishmentNoteById(id);
		if(StringUtil.isNotEmpty(note.getCode())) {
			throw new ZooException(ExceptionEnum.FLOWSTATED);
		}
		
		UserInfo info = LoginInterceptor.getLoginUser();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(info.getId());
		String businessKey = id;
		variables.put("CODE", note.getCode());
		processEngine.getRuntimeService().startProcessInstanceByKeyAndTenantId("replenishmentNote", businessKey, variables, info.getCompanyId());
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ReplenishmentNoteStatus.KG);
		this.updateReplenishmentNoteStatus(condition);
	}
	
	/**
	 * 更新补货单
	 */
	public int updateReplenishmentNote(ReplenishmentNote note) {
		return replenishmentNoteMapper.updateReplenishmentNote(note);
	}
	
	/**
	 * 作废
	 */
	public void destroy(String id) {
		ReplenishmentNote note = replenishmentNoteMapper.getReplenishmentNoteById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ReplenishmentNoteStatus.DESTROY);
		condition.put("etime", new Date());
		
		replenishmentNoteMapper.updateReplenishmentNoteStatus(condition);
		processEngine.getRuntimeService().deleteProcessInstance(note.getProcessInstanceId(), "待定");
		
		replenishmentNoteMapper.updateProcessInstanceId(null, id);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("isClaimed", "N");
		
		replenishmentNoteMapper.updateReplenishmentNoteIsClaimed(map);
	}
	
	/**
	 * 更新是否签收
	 */
	public int updateReplenishmentNoteIsClaimed(Map<String, Object> variables) {
		Map<String, Object> condition =	new HashMap<String, Object>();
		
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		return replenishmentNoteMapper.updateReplenishmentNoteIsClaimed(condition);
	}
	
	/**
	 * 流程取回
	 */
	public void reset(String id) {
		ReplenishmentNote note = this.getReplenishmentNoteById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ReplenishmentNoteStatus.WTJ);
		condition.put("isClaimed", "N");
		replenishmentNoteMapper.updateReplenishmentNoteStatus(condition);
		//删除流程
		processEngine.getRuntimeService().deleteProcessInstance(note.getProcessInstanceId(),  "待定");
		
		replenishmentNoteMapper.updateProcessInstanceId(null, id);
		
	}
	
}
