package org.example.mapper;

import org.example.pojo.Employee;
import org.springframework.context.annotation.Bean;

import java.util.List;


public interface EmployeeMapper {
    public List<Employee> selectAll();
}
