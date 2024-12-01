package org.example.springbootheadlinepart.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootheadlinepart.mapper.UserMapper;
import org.example.springbootheadlinepart.pojo.User;
import org.example.springbootheadlinepart.service.UserService;
import org.example.springbootheadlinepart.util.JwtHelper;
import org.example.springbootheadlinepart.util.MD5Util;
import org.example.springbootheadlinepart.util.Result;
import org.example.springbootheadlinepart.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Result<Object> login(User user) {
//        if (user.getUsername().isBlank()) return Result.build(null, 501, "用户名有误");
//        if (user.getUserPwd().isBlank()) return Result.build(null, 502, "密码有误");
        // 检验账号是否存在
        User queryUser = userMapper.selectOneByUsername(user.getUsername());
        if (queryUser == null) return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        // 检验账号密码是否正确
        String ePwd = MD5Util.encrypt(user.getUserPwd());
        queryUser = userMapper.selectOneByUsernameAndPassword(user.getUsername(), ePwd);
        if (queryUser == null) return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        // 账号密码都正确，生成token
        String token = jwtHelper.createToken(Long.valueOf(queryUser.getUid()));
        Map<String, String> res = new HashMap<String, String>();
        res.put("token", token);
        return Result.ok(res);
    }

    @Override
    public Result<Map<String, User>> getUserInfo(String token) {
        // 判断token是否有效
        if (jwtHelper.isExpiration(token)) return Result.build(null, ResultCodeEnum.NOTLOGIN);
        // 获得用户id
        Long userId = jwtHelper.getUserId(token);
        // 查询用户信息
        User user = userMapper.selectOneById(userId);
        // 消除密码字段
        user.setUserPwd(null);
        // 返回结果
        Map<String, User> res = new HashMap<String, User>();
        res.put("loginUser", user);
        return Result.ok(res);
    }

    @Override
    public Result<String> checkUserName(String username) {
        User user = userMapper.selectOneByUsername(username);
        if (user == null) return Result.ok(null);
        return Result.build(null, ResultCodeEnum.USERNAME_USED);
    }

    @Override
    public Result<Object> regist(User user) {
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        int rows = userMapper.insertOne(user);
        if (rows > 0) return Result.ok(null);
        log.error("注册失败！！");
        return Result.build(null, ResultCodeEnum.REGIST_FAILURE);
    }

}
