package org.example.springbootheadlinepart.service;

import org.example.springbootheadlinepart.pojo.User;
import org.example.springbootheadlinepart.util.Result;

import java.util.Map;

public interface UserService {
    Result<Object> login(User user);

    Result<Map<String, User>> getUserInfo(String token);

    Result<String> checkUserName(String username);
    Result<Object> regist(User user);


}
