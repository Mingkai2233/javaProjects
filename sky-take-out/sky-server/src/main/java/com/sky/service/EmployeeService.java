package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Result<Object> addEmployee(EmployeeDTO employee);

    Result<Object> getEmployeePage(EmployeePageQueryDTO employeeDTO);

    Result<Object> updateEmployeeStatus(Integer status, Long id);

    Result<Object> getEmployeeById(Long id);

    Result<Object> updateEmployee(EmployeeDTO employeeDTO);
}
