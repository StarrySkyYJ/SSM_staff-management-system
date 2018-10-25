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
 * @员工Controller
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	
	//单个删除 // 批量删除
	/*
	 * 批量删除：1-2-3
	 * 单个删除：1
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable String ids){
		if(ids.contains("-")){
			//批量删除
			String [] str_ids = ids.split("-");
			//组装 id的集合
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
	 * 如果直接发送ajax=PUT形式的请求
	 * 封装的数据
	 * Employee 
	 * [empId=1014, empName=null, gender=null, email=null, dId=null]
	 * 
	 * 问题：
	 * 请求体中有数据；
	 * 但是Employee对象封装不上；
	 * update tbl_emp  where emp_id = 1014;
	 * 
	 * 原因：
	 * Tomcat：
	 * 		1、将请求体中的数据，封装一个map。
	 * 		2、request.getParameter("empName")就会从这个map中取值。
	 * 		3、SpringMVC封装POJO对象的时候。
	 * 				会把POJO中每个属性的值，request.getParamter("email");
	 * AJAX发送PUT请求引发的血案：
	 * 		PUT请求，请求体中的数据，request.getParameter("empName")拿不到
	 * 		Tomcat一看是PUT不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
	 * org.apache.catalina.connector.Request--parseParameters() (3111);
	 * 
	 * protected String parseBodyMethods = "POST";
	 * if( !getConnector().isParseBodyMethod(getMethod()) ) {
                success = true;
                return;
            }
	 * 
	 * 
	 * 解决方案；
	 * 我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
	 * 1、配置上HttpPutFormContentFilter；
	 * 2、他的作用；将请求体中的数据解析包装成一个map。
	 * 3、request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
	 * 员工更新方法
	 * 
	 */
	//编辑  保存修改后员工信息
	//传入的 empId 必须与pojo一致
	@ResponseBody
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	public Msg saveEmp(Employee employee){
		employeeService.updateEmp(employee);
		return Msg.success() ;
	}
	
	//编辑  根据员工id 获取数据
	//@PathVariable("id") 根据路径获取id
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id")Integer id){
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("employee", employee);
	}
	
	
	//员工保存
	//@Valid:JSR303 ,表示进行校验
	//BindingResult result: 封装校验的结果
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result){
		if(result.hasErrors()){
			//校验失败，返回失败，在模态框中显示校验失败的错误信息
			//封装到map
			Map<String, Object> map = new HashMap<String, Object>();
			//获取所有错误信息
			List<FieldError> error = result.getFieldErrors();
			for (FieldError fieldError : error) {
				System.out.println("错误字段："+fieldError.getField());
				System.out.println("错误信息："+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			
			return Msg.fail().add("fieldError", map);
		}else{
			employeeService.employee(employee);
			return Msg.success();
		}
	}
	
	//校验用户名是否重复，可用  
	//状态码 100-成功  200-失败
	@RequestMapping("/checkuser")
	@ResponseBody
	public Msg checkUser(@RequestParam("empName")String empName){
		//先判断用户名是否是合法的表达式
		String regex = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if(!empName.matches(regex)){
			return Msg.fail().add("va_msg", "后台校验：用户名可以是2-5位中文或者6-16位英文和数字组合");
		}
		
		//数据库校验
		boolean b =employeeService.checkUser(empName);
		if(b){
			return Msg.success();
		}else{
			return Msg.fail().add("va_msg", "后台：用户名不可用");
		}
	}
	
	
	
	
	// ajax查询 转换成json格式数据
	// @ResponseBody 自动将返回的对象转换成字符串  
	// 不需要使用Model 来返回request域对象
	@RequestMapping("/empsJson")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		// 引入PageHelper分页插件
		// 在查询之前只需要调用，第pn页，以及每页数据数量
		PageHelper.startPage(pn, 5);
		// startPage后面紧跟的这个查询就是一个分页查询
		List<Employee> emps = employeeService.getAll();
		// 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		// 封装了详细的分页信息,包括有我们查询出来的数据， 5 传入连续显示的页数
		PageInfo page = new PageInfo(emps, 5);
		//model.addAttribute("pageInfo", page);

		// 连续显示页数
		// page.getNavigatepageNums();
		return Msg.success().add("pageInfo", page) ;
	}

	//跳转  Ajax 页面请求    获取数据 json格式
	@RequestMapping("/json")
	public String getJson(){
		return "listjson";
	}
	
	// 分页查询=================================================
	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
		// 引入PageHelper分页插件
		// 在查询之前只需要调用，第pn页，以及每页数据数量
		PageHelper.startPage(pn, 5);
		// startPage后面紧跟的这个查询就是一个分页查询
		List<Employee> emps = employeeService.getAll();
		// 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		// 封装了详细的分页信息,包括有我们查询出来的数据， 5 传入连续显示的页数
		PageInfo page = new PageInfo(emps, 5);
		model.addAttribute("pageInfo", page);

		// 连续显示页数
		// page.getNavigatepageNums();

		return "list";
	}
}
