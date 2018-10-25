package com.space.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.space.mapper.EmployeeMapper;
import com.space.pojo.Employee;
import com.space.pojo.EmployeeExample;
import com.space.pojo.EmployeeExample.Criteria;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeMapper employeeMapper;

	//所有员工信息获取
	public List<Employee> getAll() {
		// TODO Auto-generated method stub
		return employeeMapper.selectByExampleWithDept(null);
	}

	//员工保存
	public void employee(Employee employee) {
		employeeMapper.insert(employee);
		
	}

	//校验用户名是否可用，检验重复
	//true：可用  ； false：不可用
	public boolean checkUser(String empName) {
		//统计 名称次数
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);
		
		return count == 0;
	}

	//根据id 查询员工
	public Employee getEmp(Integer id) {
		Employee employee = employeeMapper.selectByPrimaryKey(id);
		return employee;
	}

	//员工更新
	public void updateEmp(Employee employee) {
		// TODO Auto-generated method stub
		//有选择的更新
		employeeMapper.updateByPrimaryKeySelective(employee);
	}

	//单个员工删除
	public void deleteEmp(Integer id) {

		employeeMapper.deleteByPrimaryKey(id);
	}

	//批量删除
	public void deleteBatch(List<Integer> ids) {
		// TODO Auto-generated method stub
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpIdIn(ids);
		// delete from xxx where emp_id in(1,2,3);
		employeeMapper.deleteByExample(example);
	}

}
