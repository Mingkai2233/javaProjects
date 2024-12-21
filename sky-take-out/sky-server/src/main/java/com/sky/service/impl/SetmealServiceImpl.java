package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl implements SetmealService {
    private final SetmealMapper setmealMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final DishMapper dishMapper;

    @Autowired
    public SetmealServiceImpl(SetmealMapper setmealMapper,
                              SetmealDishMapper setmealDishMapper,
                              DishMapper dishMapper) {
        this.setmealMapper = setmealMapper;
        this.setmealDishMapper = setmealDishMapper;
        this.dishMapper = dishMapper;
    }

    /**
     * 获取指定页码，指定页大小的套餐信息（包括类型名字，不包括套餐中都有哪些菜品），查询条件包括类型id，name模糊查询，以及套餐的销售状态
     *
     * @param setmealPageQueryDTO
     * @return 封装了符合条件的套餐的Result包
     */
    @Override
    public Result<PageResult> getPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOs = setmealMapper.selectWithCategoryName(setmealPageQueryDTO);
        PageResult res = new PageResult(setmealVOs.getTotal(), setmealVOs.getResult());
        return Result.success(res);
    }

    /**
     * 新增套餐 默认为停售状态
     *
     * @param setmealDTO 套餐信息和套餐包含了哪些菜品
     * @return
     */
    @Transactional
    @Override
    public Result<Object> addSetmeal(SetmealDTO setmealDTO) {
        // 设置售卖状态为停售
        setmealDTO.setStatus(0);
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 保存套餐信息到setmeal, 并获得套餐id
        setmealMapper.insertOne(setmeal);
        // 保存套餐包含了哪些菜品到setmeal_dish表
        setmealDTO.getSetmealDishes().forEach(sd -> sd.setSetmealId(setmeal.getId()));
        setmealDishMapper.insertBatch(setmealDTO.getSetmealDishes());
        return Result.success();
    }

    /**
     * 批量删除套餐
     *
     * @param ids 要删除的套套餐id
     * @return
     */
    @Transactional
    @Override
    public Result<Object> delete(List<Long> ids) {
        // 判断是否包含正在销售的套餐
        List<Long> sellingSetmealIds = setmealMapper.selectSellingSetmealIds(ids);
        if (!sellingSetmealIds.isEmpty()) throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        // 删除setmeal表中的相关项
        setmealMapper.deleteBatchByIds(ids);
        // 删除setmeal_dish中的相关项
        setmealDishMapper.deleteBatchBySetmealIds(ids);
        return Result.success();
    }

    /**
     * 根据套餐id获得对应SetmealVO
     *
     * @param id
     * @return
     */
    @Override
    public Result<Object> getSetmealById(Long id) {
        // 获得套餐本身的信息
        SetmealVO setmealVO = setmealMapper.selectOneWithCnameById(id);
        // 获得套餐中包含了哪些菜品
        List<SetmealDish> dishes = setmealDishMapper.selectBySetmealId(id);
        setmealVO.setSetmealDishes(dishes);
        return Result.success(setmealVO);
    }

    @Transactional
    @Override
    public Result<Object> updateSetmeal(SetmealDTO setmealDTO) {
        //TODO: 判断图片是否更改,从服务器上删除原有的图片

        // 更新setmeal表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        // 删除setmeal_dish中原有的菜品
        setmealDishMapper.deleteBatchBySetmealIds(Collections.singletonList(setmeal.getId()));
        // 在setmeal_dish表中添加现有菜品
        setmealDTO.getSetmealDishes().forEach(sd -> sd.setSetmealId(setmeal.getId()));
        setmealDishMapper.insertBatch(setmealDTO.getSetmealDishes());
        return Result.success();
    }

    /**
     * 修改指定套餐的售卖状态
     * @param status 要修改为的状态
     * @param id 套餐的id
     * @return
     */
    @Override
    public Result<Object> updateSetmealStatus(Integer status, Long id) {
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            // 判断套餐是否包含停售菜品 多表查询 dish 和setmeal_dish
            List<Dish> dishes = dishMapper.selectDishBySetmealId(id);
            for (Dish dish : dishes){
                if (Objects.equals(dish.getStatus(), StatusConstant.DISABLE))
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);
        return Result.success();
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
