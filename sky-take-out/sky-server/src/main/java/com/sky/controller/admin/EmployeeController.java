package com.sky.controller.admin;

import com.google.j2objc.annotations.AutoreleasePool;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Api("员工管理模块")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登录功能")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工退出登录")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @ApiOperation("新增员工")
    @PostMapping("")
    public Result<Object> addEmployee(@RequestHeader(name="token") String token, @RequestBody EmployeeDTO employeeDTO) {

        return employeeService.addEmployee(employeeDTO);
    }

    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<Object> getEmployeePage(EmployeePageQueryDTO employeeDTO) {
        log.info("员工分页查询");
        return employeeService.getEmployeePage(employeeDTO);
    }

    @ApiOperation("启用/禁用员工账号")
    @PostMapping("status/{status}")
    public Result<Object> updateEmployeeStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        if (status == 1) log.info("启用员工账号：{}", id);
        else log.info("禁用员工账号：{}", id);
        return employeeService.updateEmployeeStatus(status, id);
    }

    @ApiOperation("根据id查询指定员工")
    @GetMapping("/{id}")
    public Result<Object> getEmployeeById(@PathVariable("id") Long id) {
        log.info("根据id查询指定员工：{}", id);
        return employeeService.getEmployeeById(id);
    }

    @ApiOperation("编辑员工信息")
    @PutMapping("")
    public Result<Object> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        return employeeService.updateEmployee(employeeDTO);
    }
}
