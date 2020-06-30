package com.zoo.controller.flow;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zoo.model.flow.CustomProcessDefinition;
import com.zoo.model.system.company.Company;
import com.zoo.service.flow.DeploymentService;
import com.zoo.service.system.company.CompanyService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/flow/deployment")
public class DeploymentController {
	@Autowired
	CompanyService companyService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	DeploymentService deploymentService;
	@GetMapping("page")
	public Map<String,Object> queryDeploymentByPage(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		long count = repositoryService.createProcessDefinitionQuery().count();
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc()//使用流程定义的版本升序排列
				.listPage((page-1)*size, size);
		List<CustomProcessDefinition> customList = new ArrayList<CustomProcessDefinition>();
        if(processDefinitionList!=null&&processDefinitionList.size()>0){
        	for(ProcessDefinition pd:processDefinitionList){
        		CustomProcessDefinition cpd = new CustomProcessDefinition();
        		cpd.setId(pd.getId());
        		cpd.setName(pd.getName());
        		cpd.setKey(pd.getKey());
        		cpd.setVersion(pd.getVersion());
        		cpd.setDeploymentId(pd.getDeploymentId());
        		cpd.setDgrmResourceName(pd.getDiagramResourceName());
        		cpd.setResourceName(pd.getResourceName());
        		cpd.setDescription(pd.getDescription());
        		Company  company = companyService.getCompanyById(pd.getTenantId());
        		if(company!=null){
        			cpd.setCompanyName(company.getName());
        		}
        		
        		customList.add(cpd);
        	}
        }
        map.put("deployments", customList);
        map.put("count", count);
        return map;
	}
	@PostMapping("upload")
	public boolean upload(@RequestParam("body")String body,@RequestParam("file") MultipartFile file) {
		try {
			JSONObject object = JSONObject.fromObject(body);
			String companyId = (String) object.get("companyId");
			String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
			ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
			repositoryService.createDeployment().name(name).tenantId(companyId).addZipInputStream(zipInputStream).deploy();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	@GetMapping("viewImage")
	public void viewImage(@RequestParam("processDefinitionId")String processDefinitionId,String type,HttpServletResponse response) {
		InputStream in = deploymentService.readResource(processDefinitionId, type);
		byte[] b = new byte[1024];
        int len = -1;
        int lenEnd = 1024;
        while (true) {
            try {
                if (!((len = in.read(b, 0, lenEnd)) != -1)) break;
                response.getOutputStream().write(b, 0, len);
            } catch (IOException e) {
               e.printStackTrace();
            }
        }

	}
}
