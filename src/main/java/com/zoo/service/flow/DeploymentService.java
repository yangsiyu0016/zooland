package com.zoo.service.flow;

import java.io.InputStream;

import javax.transaction.Transactional;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeploymentService {
	@Autowired
	RepositoryService repositoryService;
	public InputStream readResource(String processDefinitionId,String type) {
		ProcessDefinition  processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		String resourceName="";
		if(type.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		}
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		return resourceAsStream;
	}
}
