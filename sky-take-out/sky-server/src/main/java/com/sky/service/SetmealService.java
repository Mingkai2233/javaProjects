package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetmealService {
    Result<PageResult> getPage(SetmealPageQueryDTO setmealPageQueryDTO);

    Result<Object> addSetmeal(SetmealDTO setmealDTO);

    Result<Object> delete(List<Long> ids);

    Result<Object> getSetmealById(Long id);

    Result<Object> updateSetmeal(SetmealDTO setmealDTO);

    Result<Object> updateSetmealStatus(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

}
