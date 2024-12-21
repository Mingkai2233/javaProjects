package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final WeChatProperties wxProperties;
    private final String WX_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    public UserServiceImpl(UserMapper userMapper, WeChatProperties wxProperties) {
        this.userMapper = userMapper;
        this.wxProperties = wxProperties;
    }


    @Override
    public User login(UserLoginDTO userLoginDTO) {
        // 使用微信登录接口获取openid
        Map<String, String> params = new HashMap<>();
        params.put("appid", wxProperties.getAppid());
        params.put("secret", wxProperties.getSecret());
        params.put("js_code", userLoginDTO.getCode());
        params.put("grant_type", "authorization_code");
        String wxRes = HttpClientUtil.doGet(this.WX_URL, params);
        JSONObject json = JSONObject.parseObject(wxRes);
        String openid = json.getString("openid");

        // openid如果为空，直接抛出异常
        if (openid == null) throw new LoginFailedException("获取openid失败");
        // 判断当前用户是否第一次使用该小程序
        User user = userMapper.selectUserByOpenid(openid);
        if (user == null) {
            // 如果是第一次使用，将该用户插入数据库
            log.info("为用户注册");
            // TODO 补充用户信息
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insertUser(user);
        }
        return user;
    }
}
