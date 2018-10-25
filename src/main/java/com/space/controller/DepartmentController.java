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
 * @����Ͳ����йص�����
 *
 */
@Controller
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService ;
	
	//�������в�����Ϣ
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDept(){
		 List<Department> dept= departmentService.getAllDept();
		
		return Msg.success().add("depts", dept);
	}
}
