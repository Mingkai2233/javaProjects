package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 插入多条口味数据
     * @param flavors 口味列表
     * @return 插入了多少条
     */
    Integer insertBatch(List<DishFlavor> flavors);

    /**
     * 根据给定的菜品id删除对应的口味数据
     * @param ids 菜品id列表
     * @return 删除了多少条数据
     */
    Integer deleteBatchByDishIds(List<Long> ids);

    /**
     * 查询属于指定菜品的口味数据
     * @param id 菜品id
     * @return 菜品口味列表
     */
    List<DishFlavor> selectByDishId(Long id);
}
