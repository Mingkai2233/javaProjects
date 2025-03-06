package npu.edu.shortlink.admin.common.interceptor;


import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npu.edu.shortlink.admin.common.biz.user.UserContext;
import npu.edu.shortlink.admin.common.biz.user.UserInfoDTO;
import npu.edu.shortlink.admin.common.convention.exception.ClientException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

import static npu.edu.shortlink.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static npu.edu.shortlink.admin.common.convention.errorcode.BaseErrorCode.USER_TOKEN_FAIL;

@RequiredArgsConstructor
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    // 需要排除的路径
    private static final List<String> EXCLUDED_POST_PATHS = Arrays.asList("/api/short-link/admin/v1/user");
    // 在请求处理之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("POST") && EXCLUDED_POST_PATHS.contains(request.getRequestURI())){
            return true;
        }
        String username = request.getHeader("username");
        String token = request.getHeader("token");
        Object userInfo;
        try{
            userInfo = stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token);
            if (userInfo == null){
                throw new ClientException(USER_TOKEN_FAIL);
            }
        }
        catch (Exception e){
            throw new ClientException(USER_TOKEN_FAIL);

        }
        String userInfoJsonStr = userInfo.toString();
        UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr, UserInfoDTO.class);
        UserContext.setUser(userInfoDTO);
        log.info("UserInfoInterceptor: {}", userInfoDTO);
        return true;
    }

}