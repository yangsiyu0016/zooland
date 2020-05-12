package com.zoo.controller.annex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zoo.model.annex.Annex;
import com.zoo.service.annex.AnnexService;
import com.zoo.vo.RespBean;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/annex")
@PropertySource("classpath:downloadAddress.properties")
public class AnnexController {
	
	@Value("${downloadIp}")
	private String downloadIp;
	
	@Autowired
	private AnnexService annexService;
	
	//上传功能
	@PostMapping("upload")
	public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
		Map<String,Object> map = new HashMap<String, Object>();
			try {
				String fileName = file.getOriginalFilename();
				//获取title
				//拼接下载url
				String url = downloadIp;
				
				String projectPath = System.getProperty("user.dir");//获取当前项目路径
				//拼接上传路径
				String uploadUrl = projectPath + "/src/annexFile/" + fileName;
				
				//判断该文件是否存在，
				File uploadFile = new File(uploadUrl);
				if(file != null) {
					file.transferTo(uploadFile);
				}
				Annex annex = new Annex();
				String annexId = UUID.randomUUID().toString();
				annex.setId(annexId);
				annex.setTitle(fileName);
				annex.setUrl(url);
				annex.setUtime(new Date());
				annexService.addAnnex(annex);
				map.put("annex", annex);
				map.put("status", "200");
				map.put("msg", "上传成功");
				return map;
			} catch (Exception e) {
				// TODO: handle exception
				map.put("status", "200");
				map.put("msg", e.getMessage());
				return map;
			}
		
	}
	
	//下载功能
	@GetMapping("download")
	public void downloadAnnexFile(@RequestParam("title") String title, HttpServletResponse response, HttpServletRequest request) {
		OutputStream os = null;

        InputStream is= null;
        
        String projectPath = System.getProperty("user.dir");//获取当前项目路径
		//拼接上传路径
		String uploadUrl = projectPath + "/src/annexFile/" + title;
        try {
			
        	// 取得输出流
        	os = response.getOutputStream();
        	// 清空输出流
        	response.reset();
        	response.setContentType("application/x-download;charset=utf-8");
        	//Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，           
        	// 保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。            
        	//把文件名按UTF-8取出，并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
        	//response.setHeader("Content-Disposition", "attachment;filename="+ new String(title.getBytes("utf-8"),"ISO-8859-1"));
        	String userAgent = request.getHeader("User-Agent");//获取浏览器名（IE/Chome/firefox）
        	if (userAgent.contains("firefox")) {    
        		title = new String(title.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器    
        	} else if (userAgent.contains("CHROME")) {    
        		title = new String(title.getBytes("UTF-8"), "ISO8859-1");// 谷歌    
        	} else {    
        		title = URLEncoder.encode(title, "UTF-8");// IE浏览器    
        	}
        	response.setHeader("Content-Disposition", "attachment;filename="+ title);
        	//读取流
        	File f = new File(uploadUrl);
        	is = new FileInputStream(f);
        	
        	IOUtils.copy(is, response.getOutputStream());
        	response.getOutputStream().flush();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
					e.printStackTrace();
			}
			try {
				if (os != null) {
					os.close();
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//删除功能
	@PostMapping("delete")
	public RespBean delAnnexFile(@RequestBody Annex annex) {
		try {
			int delNum = annexService.delAnnexById(annex.getId());
			String projectPath = System.getProperty("user.dir");//获取当前项目路径
			//拼接上传路径
			String uploadUrl = projectPath + "/src/annexFile/" + annex.getTitle();
			boolean isDel = new File(uploadUrl).delete();
			if (isDel) {
				return new RespBean("200", "删除成功");
			}else {
				return new RespBean("000", "未删除");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
}
