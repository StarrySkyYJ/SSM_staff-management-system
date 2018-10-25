package com.space.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.space.mapper.DepartmentMapper;
import com.space.pojo.Department;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper ; 
	
	public List<Department> getAllDept() {
		List<Department> list = departmentMapper.selectByExample(null);
		return list;
	}

}
