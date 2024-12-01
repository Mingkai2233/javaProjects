package org.example.springbootheadlinepart.controller;

import com.alibaba.druid.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.example.springbootheadlinepart.pojo.User;
import org.example.springbootheadlinepart.service.Impl.UserServiceImpl;
import org.example.springbootheadlinepart.service.Impl.UserServiceImpl;
import org.example.springbootheadlinepart.util.JwtHelper;
import org.example.springbootheadlinepart.util.Result;
import org.example.springbootheadlinepart.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@CrossOrigin
@RequestMapping("/user")
@RestController()
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/login")
    public Result<Object> login( @RequestBody User user){
        log.debug("username:{}, password:{}", user.getUsername(), user.getUserPwd());
        return userService.login(user);
    }

    @GetMapping("/getUserInfo")
    public Result<Map<String, User>> getUserInfo(@RequestHeader(name="token") String token){
        return userService.getUserInfo(token);
    }
    @PostMapping("/checkUserName")
    public Result<String> checkUserName(@RequestParam(name="username") String username){
        return userService.checkUserName(username);
    }

    @PostMapping("/regist")
    public Result<Object> regist(@RequestBody User user){
        return userService.regist(user);
    }

    @GetMapping("/checkLogin")
    public Result<Object> checkLogin(@RequestHeader(name="token") String token){
        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)){
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        return Result.ok(null);
    }
}
