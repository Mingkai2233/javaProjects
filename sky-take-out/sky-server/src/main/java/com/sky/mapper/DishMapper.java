package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return 当前类型菜品的数量
     */
    Integer countByCategoryId(Long categoryId);

    /**
     * 新加一个菜品
     * @param dish 要插入的菜品
     * @return 数据库插入了多少条数据
     */
    @AutoFill(OperationType.INSERT)
    Integer insertOne(Dish dish);

    /**
     *
     * @param dishPageQueryDTO
     * @return 菜品的VO类型
     */
    Page<DishVO> selectPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     *
     * @param ids 要查询的菜品id列表
     * @return ids中status为1的菜品数量
     */
    List<Long> selectSellingDishIds(List<Long> ids);

    /**
     *
     * @param ids 要删除菜品的id列表
     * @return 删除的行数
     */
    Integer deleteBatchByIds(List<Long> ids);

    /**
     *
     * @param category 所查询的菜品类型
     * @return 该类型的菜品列表
     */
    List<Dish> selectDishByCategory(Long category);

    /**
     *
     * @param id 菜品id
     * @return 菜品的全部信息，包括类型名称
     */
    DishVO selectOneById(Long id);

    /**
     * 多表查询，一次性将菜品信息，品类名称和口味全部查出来，涉及三个表
     * @param id 要查询的菜品id
     * @return 视图对象
     */
    DishVO selectDishWithCategoryNameAndFlavorsByid(Long id);

    /**
     * 根据给定dish数据，更改对应dish
     * @param newDish
     * @return 更新条数
     */
    @AutoFill(OperationType.UPDATE)
    Integer update(Dish newDish);

    /**
     * 根据套餐id获得套餐包含的菜品都有哪些
     * @param setmeal_id 套餐id
     * @return
     */
    List<Dish> selectDishBySetmealId(Long setmeal_id);

    /**
     * 获取指定类型和指定状态的菜品
     * @param categoryId
     * @param status
     * @return
     */
    List<Dish> selectDishByCategoryIdAndStatus(Long categoryId, Integer status);

    /**
     * 按照id查询单个菜品
     * @param dishId
     * @return
     */
    Dish selectDishById(Long dishId);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
