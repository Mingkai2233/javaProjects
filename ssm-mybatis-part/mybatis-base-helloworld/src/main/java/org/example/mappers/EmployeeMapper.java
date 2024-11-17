package org.example.mappers;

import org.example.pojo.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {
    public Employee getEmployeeById(int empId);

    public int insertEmployee(Employee employee);
    public void insertEmployeeMap(Map<String, String> m);
    public void updateEmployeeName(int empId, String name);
    public void updateEmployeeSalary(int empId, double salary);
    public List<Employee> queryAll();

}
