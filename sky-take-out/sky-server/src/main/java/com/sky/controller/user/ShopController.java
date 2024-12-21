package com.sky.controller.user;

import com.sky.constant.ShopConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Api(tags="店铺管理相关接口")
@RestController("userShopeController")
@RequestMapping("/user/shop")
public class ShopController {
    private final RedisTemplate redisTemplate;
    @Autowired
    public ShopController(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }


    @GetMapping("/status")
    public Result<Object> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(ShopConstant.SHOP_STATUS_KEY);
        log.info("当前店铺营业状态为:{}", Objects.equals(status, ShopConstant.OPEN) ? "营业中":"打烊了");
        return Result.success(status);
    }
}
