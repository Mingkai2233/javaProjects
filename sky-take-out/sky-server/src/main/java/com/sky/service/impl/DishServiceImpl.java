package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.Result;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;
    private final DishFlavorMapper dishFlavorMapper;
    public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper) {
        this.dishMapper = dishMapper;
        this.dishFlavorMapper = dishFlavorMapper;
    }

    @Transactional
    @Override
    public Result<Object> save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 添加dish
        dishMapper.insertOne(dish);
        // 添加flavor
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (dishDTO.getFlavors()!=null && !dishDTO.getFlavors().isEmpty()){
            flavors.forEach(f->{f.setDishId(dish.getId());});
            dishFlavorMapper.insertOne(dishDTO.getFlavors());
        }
        return Result.success();
    }
}
