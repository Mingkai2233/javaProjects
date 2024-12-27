package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.hmdp.constant.RedisConstants;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ShopTypeMapper shopTypeMapper;
    @Override
    public Result queryTypeList() {
        List<ShopType> res;
        // 1. 查缓存
        String jShopList = redisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_TYPE_KEY);
        // 1.1 命中直接返回
        if (StrUtil.isNotBlank(jShopList)) {
            res = JSON.parseArray(jShopList, ShopType.class);
            return Result.ok(res);
        }
        // 2. 未命中 查数据库
        res = shopTypeMapper.selectList(null);
        // 3. 存缓存
        redisTemplate.opsForValue()
                .set(RedisConstants.CACHE_SHOP_TYPE_KEY, JSON.toJSONString(res),
                RedisConstants.CACHE_SHOP_TYPE_TTL, TimeUnit.MINUTES);
        // 4. 返回
        return Result.ok(res);
    }
}
