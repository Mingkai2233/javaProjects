package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 查询都有哪些套餐包含了指定的菜品
     * @param dishIds 要查询的菜品id列表
     * @return 包含指定菜品id的套餐数量
     */
    List<Long> selectSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐的菜品信息
     * @param setmealDishes
     * @return
     */
    Integer insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 批量删除套餐信息
     * @param ids 套餐id
     * @return
     */
    Integer deleteBatchBySetmealIds(List<Long> ids);

    /**
     * 根据套餐id获取对应套餐和菜品的关系数据
     * @param id
     * @return
     */
    List<SetmealDish> selectBySetmealId(Long id);
}
