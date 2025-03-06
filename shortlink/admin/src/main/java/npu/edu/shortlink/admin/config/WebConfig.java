package npu.edu.shortlink.admin.config;

import npu.edu.shortlink.admin.common.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public UserInfoInterceptor userInfoInterceptor( StringRedisTemplate redisTemplate) {
        return new UserInfoInterceptor(redisTemplate);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoInterceptor(null))
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/api/short-link/admin/v1/user/has-username")
                .excludePathPatterns("/api/short-link/admin/v1/user/login");
    }
}