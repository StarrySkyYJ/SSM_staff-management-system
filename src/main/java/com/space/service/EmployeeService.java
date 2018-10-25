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

	//����Ա����Ϣ��ȡ
	public List<Employee> getAll() {
		// TODO Auto-generated method stub
		return employeeMapper.selectByExampleWithDept(null);
	}

	//Ա������
	public void employee(Employee employee) {
		employeeMapper.insert(employee);
		
	}

	//У���û����Ƿ���ã������ظ�
	//true������  �� false��������
	public boolean checkUser(String empName) {
		//ͳ�� ���ƴ���
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);
		
		return count == 0;
	}

	//����id ��ѯԱ��
	public Employee getEmp(Integer id) {
		Employee employee = employeeMapper.selectByPrimaryKey(id);
		return employee;
	}

	//Ա������
	public void updateEmp(Employee employee) {
		// TODO Auto-generated method stub
		//��ѡ��ĸ���
		employeeMapper.updateByPrimaryKeySelective(employee);
	}

	//����Ա��ɾ��
	public void deleteEmp(Integer id) {

		employeeMapper.deleteByPrimaryKey(id);
	}

	//����ɾ��
	public void deleteBatch(List<Integer> ids) {
		// TODO Auto-generated method stub
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpIdIn(ids);
		// delete from xxx where emp_id in(1,2,3);
		employeeMapper.deleteByExample(example);
	}

}
