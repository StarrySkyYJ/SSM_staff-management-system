package com.space.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.space.mapper.EmployeeMapper;
import com.space.pojo.Employee;

/**
 * @author SPACE
 * @controller  service  ≤‚ ‘
 *
 */
@Service
public class TestService {

	@Autowired
	private EmployeeMapper employeeMapper ;
	
	public Employee getTestAll(){
		Employee all = employeeMapper.selectByPrimaryKey(1);
		
		return all ;
	}
}
