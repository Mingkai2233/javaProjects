package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;
    @Autowired
    ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper,
                            DishMapper dishMapper,
                            SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
        this.dishMapper = dishMapper;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    @Override
    public Result<Object> add(ShoppingCartDTO shoppingCartDTO) {
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, cart);
        // 先判断购物车中是否已经存在对应商品
        ShoppingCart queryCart = shoppingCartMapper.select(cart);
        // 不存在，收集数据，然后插入
        if (queryCart == null){
            // 当前插入的是菜品
            if(cart.getDishId() != null){

                Dish dish = dishMapper.selectDishById(cart.getDishId());
                cart.setName(dish.getName());
                cart.setImage(dish.getImage());
                cart.setAmount(dish.getPrice());
            }
            // 当前插入的是套餐
            else{
                Setmeal setmeal = setmealMapper.selectOneById(cart.getSetmealId());
                cart.setName(setmeal.getName());
                cart.setImage(setmeal.getImage());
                cart.setAmount(setmeal.getPrice());
            }
            cart.setUserId(currentId);
            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insertOne(cart);
        }
        // 存在对应商品， 数量加一
        else{
            BeanUtils.copyProperties(queryCart,cart);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumberById(cart);
        }
        return Result.success();
    }

    @Override
    public Result<List<ShoppingCart>> list() {
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> res = shoppingCartMapper.selectBatchByUserId(userId);
        return Result.success(res);
    }

    @Override
    public Result<Object> clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteAll(userId);
        return Result.success();
    }

    @Override
    public Result<Object> deleteOne(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, cart);
        ShoppingCart queryCart = shoppingCartMapper.select(cart);
        queryCart.setNumber(queryCart.getNumber()-1);
        if (queryCart.getNumber() == 0) {
            shoppingCartMapper.deleteOne(queryCart);
        }else{
            shoppingCartMapper.updateNumberById(queryCart);
        }
        return Result.success();
    }
}
