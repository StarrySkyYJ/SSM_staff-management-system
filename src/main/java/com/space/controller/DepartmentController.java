package com.space.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.space.common.Msg;
import com.space.pojo.Department;
import com.space.service.DepartmentService;

/**
 * @author SPACE
 * @处理和部门有关的请求
 *
 */
@Controller
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService ;
	
	//返回所有部门信息
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDept(){
		 List<Department> dept= departmentService.getAllDept();
		
		return Msg.success().add("depts", dept);
	}
}
