package org.example.springbootheadlinepart.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootheadlinepart.util.JwtHelper;
import org.example.springbootheadlinepart.util.Result;
import org.example.springbootheadlinepart.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginProtectInterceptor implements HandlerInterceptor {
    @Autowired
    JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty() || jwtHelper.isExpiration(token)){
            Result<Object> res = Result.build(null, ResultCodeEnum.NOTLOGIN);
            ObjectMapper mapper = new ObjectMapper();
            String sres = mapper.writeValueAsString(res);
            response.getWriter().print(sres);
            return false;
        }
        return true;
    }
}
