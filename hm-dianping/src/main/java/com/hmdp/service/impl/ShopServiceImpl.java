package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.hmdp.constant.ErrorMessageConstant;
import com.hmdp.constant.RedisConstants;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.exception.CommonException;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ShopMapper shopMapper;


    @Override
    public Result queryById(Long id) {
        Shop res;
        // 1. 从redis中查询店铺数据
        String jshop =  stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        // 1.1 存在且不为空 直接返回
        if(StrUtil.isNotBlank(jshop)){
            res = JSON.parseObject(jshop, Shop.class);
            return Result.ok(res);
        }
        // 1.2 存在但是是空字符串，返回错误信息
        if (jshop != null){
            throw new CommonException(ErrorMessageConstant.SHOP_NOT_EXIST);
        }
        // 2. 未命中缓存，从数据库中查询
        res = shopMapper.selectById(id);
        // 2.1 店铺不存在，返回错误信息， 并将空值存入redis中
        if(res == null){
            stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, "",
                    RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            throw new CommonException(ErrorMessageConstant.SHOP_NOT_EXIST);
        }
        // 3. 店铺存在 将数据存入redis中
        jshop = JSON.toJSONString(res);
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, jshop,
                RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        // 4. 返回数据
        return Result.ok(res);
    }



    @Transactional
    @Override
    public void update(Shop shop) {
        // 1. 检查id是否为空
        if (shop.getId() == null ) throw new CommonException(ErrorMessageConstant.SHOP_NOT_EXIST);
        // 2. 更新数据库
        shopMapper.updateById(shop);
        // 3. 删除缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY + shop.getId());
    }
}
