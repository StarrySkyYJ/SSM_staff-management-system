package com.space.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.space.common.Msg;
import com.space.pojo.Employee;
import com.space.service.EmployeeService;

/**
 * @author SPACE
 * @Ա��Controller
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	
	//����ɾ�� // ����ɾ��
	/*
	 * ����ɾ����1-2-3
	 * ����ɾ����1
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable String ids){
		if(ids.contains("-")){
			//����ɾ��
			String [] str_ids = ids.split("-");
			//��װ id�ļ���
			ArrayList<Integer> del_ids = new ArrayList<>();
			for (String string : str_ids) {
				//Integer integer = Integer.parseInt(string);
				//System.err.println(Integer.parseInt(string));
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		}else{
			employeeService.deleteEmp(Integer.parseInt(ids));
			
			
		}
		return Msg.success() ;
		
	}
	

	/**
	 * ���ֱ�ӷ���ajax=PUT��ʽ������
	 * ��װ������
	 * Employee 
	 * [empId=1014, empName=null, gender=null, email=null, dId=null]
	 * 
	 * ���⣺
	 * �������������ݣ�
	 * ����Employee�����װ���ϣ�
	 * update tbl_emp  where emp_id = 1014;
	 * 
	 * ԭ��
	 * Tomcat��
	 * 		1�����������е����ݣ���װһ��map��
	 * 		2��request.getParameter("empName")�ͻ�����map��ȡֵ��
	 * 		3��SpringMVC��װPOJO�����ʱ��
	 * 				���POJO��ÿ�����Ե�ֵ��request.getParamter("email");
	 * AJAX����PUT����������Ѫ����
	 * 		PUT�����������е����ݣ�request.getParameter("empName")�ò���
	 * 		Tomcatһ����PUT�����װ�������е�����Ϊmap��ֻ��POST��ʽ������ŷ�װ������Ϊmap
	 * org.apache.catalina.connector.Request--parseParameters() (3111);
	 * 
	 * protected String parseBodyMethods = "POST";
	 * if( !getConnector().isParseBodyMethod(getMethod()) ) {
                success = true;
                return;
            }
	 * 
	 * 
	 * ���������
	 * ����Ҫ��֧��ֱ�ӷ���PUT֮�������Ҫ��װ�������е�����
	 * 1��������HttpPutFormContentFilter��
	 * 2���������ã����������е����ݽ�����װ��һ��map��
	 * 3��request�����°�װ��request.getParameter()����д���ͻ���Լ���װ��map��ȡ����
	 * Ա�����·���
	 * 
	 */
	//�༭  �����޸ĺ�Ա����Ϣ
	//����� empId ������pojoһ��
	@ResponseBody
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	public Msg saveEmp(Employee employee){
		employeeService.updateEmp(employee);
		return Msg.success() ;
	}
	
	//�༭  ����Ա��id ��ȡ����
	//@PathVariable("id") ����·����ȡid
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id")Integer id){
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("employee", employee);
	}
	
	
	//Ա������
	//@Valid:JSR303 ,��ʾ����У��
	//BindingResult result: ��װУ��Ľ��
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result){
		if(result.hasErrors()){
			//У��ʧ�ܣ�����ʧ�ܣ���ģ̬������ʾУ��ʧ�ܵĴ�����Ϣ
			//��װ��map
			Map<String, Object> map = new HashMap<String, Object>();
			//��ȡ���д�����Ϣ
			List<FieldError> error = result.getFieldErrors();
			for (FieldError fieldError : error) {
				System.out.println("�����ֶΣ�"+fieldError.getField());
				System.out.println("������Ϣ��"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			
			return Msg.fail().add("fieldError", map);
		}else{
			employeeService.employee(employee);
			return Msg.success();
		}
	}
	
	//У���û����Ƿ��ظ�������  
	//״̬�� 100-�ɹ�  200-ʧ��
	@RequestMapping("/checkuser")
	@ResponseBody
	public Msg checkUser(@RequestParam("empName")String empName){
		//���ж��û����Ƿ��ǺϷ��ı��ʽ
		String regex = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if(!empName.matches(regex)){
			return Msg.fail().add("va_msg", "��̨У�飺�û���������2-5λ���Ļ���6-16λӢ�ĺ��������");
		}
		
		//���ݿ�У��
		boolean b =employeeService.checkUser(empName);
		if(b){
			return Msg.success();
		}else{
			return Msg.fail().add("va_msg", "��̨���û���������");
		}
	}
	
	
	
	
	// ajax��ѯ ת����json��ʽ����
	// @ResponseBody �Զ������صĶ���ת�����ַ���  
	// ����Ҫʹ��Model ������request�����
	@RequestMapping("/empsJson")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		// ����PageHelper��ҳ���
		// �ڲ�ѯ֮ǰֻ��Ҫ���ã���pnҳ���Լ�ÿҳ��������
		PageHelper.startPage(pn, 5);
		// startPage��������������ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		// ʹ��pageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ������ˡ�
		// ��װ����ϸ�ķ�ҳ��Ϣ,���������ǲ�ѯ���������ݣ� 5 ����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps, 5);
		//model.addAttribute("pageInfo", page);

		// ������ʾҳ��
		// page.getNavigatepageNums();
		return Msg.success().add("pageInfo", page) ;
	}

	//��ת  Ajax ҳ������    ��ȡ���� json��ʽ
	@RequestMapping("/json")
	public String getJson(){
		return "listjson";
	}
	
	// ��ҳ��ѯ=================================================
	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
		// ����PageHelper��ҳ���
		// �ڲ�ѯ֮ǰֻ��Ҫ���ã���pnҳ���Լ�ÿҳ��������
		PageHelper.startPage(pn, 5);
		// startPage��������������ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		// ʹ��pageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ������ˡ�
		// ��װ����ϸ�ķ�ҳ��Ϣ,���������ǲ�ѯ���������ݣ� 5 ����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps, 5);
		model.addAttribute("pageInfo", page);

		// ������ʾҳ��
		// page.getNavigatepageNums();

		return "list";
	}
}
