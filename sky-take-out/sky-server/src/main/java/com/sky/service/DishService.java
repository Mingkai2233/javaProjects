package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    Result<Object> save(DishDTO dishDTO);

    Result<PageResult> dishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    Result<String> deleteBatchDish(List<Long> ids);

    Result<Object> getBatchByCategoryId(Long categoryId);

    Result<Object> getById(Long id);

    Result<Object> updateDishWithFlavors(DishDTO dishDTO);

    Result<Object> updateDishStatus(Integer status, Long id);
    List<DishVO> listWithFlavor(Dish dish);
}
