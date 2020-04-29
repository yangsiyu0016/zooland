package com.zoo.controller.system.user;



import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.user.SystemUser;
import com.zoo.service.system.user.SystemUserService;
import com.zoo.vo.RespBean;


@RestController
@RequestMapping("/user")
public class SystemUserController {
	@Autowired
	private SystemUserService systemUserService;
	@GetMapping("queryUserByPage")
	public Map<String,Object> queryUserByPage(
			@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String keywords){
		Map<String,Object> map = new HashMap<String,Object>();
		List<SystemUser> users = systemUserService.queryUserByPage(page, size,keywords);
		Long count = systemUserService.getCountByKeywords(keywords);
		map.put("users",users);
		map.put("count",count);
		return map;
	}
	@GetMapping("/all")
	public List<SystemUser> getAll(){
		return systemUserService.getAllUsers();
	}
	@PostMapping("add")
	public boolean add(@RequestBody SystemUser user){
		systemUserService.add(user);
		return true;	
	}
	@PutMapping("update")
	public RespBean update(@RequestBody SystemUser user) {
		if(systemUserService.updateUser(user)==1) {
			return new RespBean("success","更新成功");
		}
		return new RespBean("error","更新失败");
	}
	@GetMapping("query")
	public ResponseEntity<SystemUser> queryUser(
			@RequestParam("username") String username,
			@RequestParam("password") String password){
		SystemUser user = systemUserService.queryUser(username, password);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.ok(user);
	}
	@GetMapping("exportUser")
	public ResponseEntity<byte[]> exportEmp(){
		HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
		try {
			//1.创建Excel文档
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			//创建Excel表单
			HSSFSheet sheet = workbook.createSheet("猪乐道用户信息表");
			//创建日期显示格式
			HSSFCellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
			//创建标题的显示样式
			HSSFCellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			//定义列的宽度
			sheet.setColumnWidth(0, 5 * 256);
			sheet.setColumnWidth(1, 12 * 256);
			
			//5.设置表头
			HSSFRow headerRow = sheet.createRow(0);
			HSSFCell cell0 = headerRow.createCell(0);
			cell0.setCellValue("姓名");
			cell0.setCellStyle(headerStyle);
			HSSFCell cell1 = headerRow.createCell(1);
			cell1.setCellValue("用户名");
			cell1.setCellStyle(headerStyle);
			
			List<SystemUser> users = systemUserService.getAllUsers();
			for(int i=0;i<users.size();i++) {
				HSSFRow row = sheet.createRow(i + 1);
				SystemUser user = users.get(i);
				row.createCell(0).setCellValue(user.getRealName());
                row.createCell(1).setCellValue(user.getUserName());
			}
			
			headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String("用户表.xls".getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
	}
}
