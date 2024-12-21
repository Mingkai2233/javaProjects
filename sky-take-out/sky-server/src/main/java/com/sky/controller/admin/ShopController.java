package com.sky.controller.admin;

import com.sky.constant.ShopConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Api("店铺管理相关接口")
@RestController("adminShopeController")
@RequestMapping("/admin/shop")
public class ShopController {
    private final RedisTemplate redisTemplate;
    @Autowired
    public ShopController(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PutMapping("{status}")
    public Result<Object> changeShopStatus(@PathVariable Integer status) {
        log.info("更改营业状态为:{}", Objects.equals(status, ShopConstant.OPEN) ? "营业中":"打烊了");
        redisTemplate.opsForValue().set(ShopConstant.SHOP_STATUS_KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Object> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(ShopConstant.SHOP_STATUS_KEY);
        log.info("当前店铺营业状态为:{}", Objects.equals(status, ShopConstant.OPEN) ? "营业中":"打烊了");
        return Result.success(status);
    }
}
