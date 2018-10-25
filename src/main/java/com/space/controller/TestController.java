package com.space.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.space.pojo.Employee;
import com.space.service.TestService;

/**
 * @author SPACE
 *@controller ≤‚ ‘
 */
@Controller
public class TestController {
	
	@Autowired
	private TestService testService;
	

	@RequestMapping("/index")
	@ResponseBody
	public String indexTest(){
		Employee e = testService.getTestAll();
		//System.out.println(e);
		String helloString = "œÓƒø≤ø ≤‚ ‘£° Hello ,World !" ;
		return helloString;
	}
	
	@RequestMapping("/list")
	public String jspList(){
		return "list";
	}
}
