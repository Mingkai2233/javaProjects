package com.hmdp.interceptor;

import cn.hutool.db.Session;
import com.hmdp.constant.ErrorMessageConstant;
import com.hmdp.constant.SessionConstant;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.exception.CommonException;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("校验登陆状态");
        // 1. 从threadlocal获取用户
        UserDTO user = UserHolder.getUser();
        // 3. 判断用户是否存在
        if (user == null){
            response.setStatus(401);
            return false;
        }
        // 4. 存在，放行
        return true;
    }
}
