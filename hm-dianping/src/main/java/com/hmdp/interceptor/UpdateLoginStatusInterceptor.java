package com.hmdp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.constant.RedisConstants;
import com.hmdp.dto.UserDTO;
import com.hmdp.utils.UserHolder;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 定时刷新用户登录状态，防止用户长时间未操作导致登录状态失效
 *
 * 根据token从redis中获取UserDTO，然后保存到ThreadLocal中
 *
 */
@Slf4j
public class UpdateLoginStatusInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public UpdateLoginStatusInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("当前线程id:{}", Thread.currentThread().getId());
        log.info("刷新用户登录状态");
        // 1. 获取token
        String token = request.getHeader("authorization");
        if (StringUtils.isEmpty(token)) return true;
        // 2. 从redis中获取UserDTO
        String redisKey = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(redisKey);
        // 3. 如果User不存在，放行
        if (userMap==null || userMap.isEmpty()) return true;
        // 4. 如果UserDTO存在，保存UserDTO到ThreadLocal， 然后刷新有效时间，放行
        UserDTO loginUser = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        UserHolder.saveUser(loginUser);
        stringRedisTemplate.expire(redisKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }
}
