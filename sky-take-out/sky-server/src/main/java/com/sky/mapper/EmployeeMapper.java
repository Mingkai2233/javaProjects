package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.sky.annotation.AutoFill;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(OperationType.INSERT)
    Integer insertOne(Employee employee);

    Page<Employee> pageQuery(@Param("name") String name);

    @AutoFill(OperationType.UPDATE)
    Integer update(Employee employee);

    Employee selectOneById(Long id);
}
