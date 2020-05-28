package com.zoo.controller.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.flow.HistoryAction;
import com.zoo.service.flow.HitoryActionService;

@RestController
@RequestMapping("/flow/history/action")
public class HistoryActionController {
	@Autowired
	HitoryActionService haService;
	@GetMapping("getHistory")
	public List<HistoryAction> getHistoryAction(@RequestParam("processInstanceId") String processInstanceId){
		return haService.getHistoryActionByProcessInstanceId(processInstanceId);
	}
}
