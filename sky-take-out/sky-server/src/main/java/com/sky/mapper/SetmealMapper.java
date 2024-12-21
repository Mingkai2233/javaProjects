package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    Integer countByCategoryId(Long id);

    /**
     * 根据套餐类型，售卖状态，名字 查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> selectWithCategoryName(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 插入一个套餐
     * @param setmeal
     * @return
     */
    @AutoFill(OperationType.INSERT)
    Integer insertOne(Setmeal setmeal);

    /**
     * 根据给定的套餐，更新对应套餐
     * @param setmeal
     * @return
     */
    @AutoFill(OperationType.UPDATE)
    Integer update(Setmeal setmeal);

    /**
     * 查询给定套餐id中 都有哪些正在出售
     * @param ids 套餐id列表
     * @return
     */
    List<Long> selectSellingSetmealIds(List<Long> ids);

    /**
     * 批量删除套餐
     * @param ids 套餐id列表
     * @return
     */
    Integer deleteBatchByIds(List<Long> ids);

    /**
     * 获得指定id的套餐信息,以及类别名称
     * @param id
     * @return
     */
    SetmealVO selectOneWithCnameById(Long id);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);


    /**
     * 根据套餐id查询套餐
     * @param setmealId
     * @return
     */
    Setmeal selectOneById(Long setmealId);


    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
