package org.example.controller;

import org.example.pojo.Employee;
import org.example.service.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("emp")
public class EmployeeController {
    @Autowired
    EmployeeServiceImpl employeeService;

    @RequestMapping("test")
    @ResponseBody
    public List<Employee> test(){
       return employeeService.getAllEmployees();
    }



}
