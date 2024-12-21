package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final SetmealMapper setmealMapper;
    public DishServiceImpl(DishMapper dishMapper,
                           DishFlavorMapper dishFlavorMapper,
                           SetmealDishMapper setmealDishMapper,
                           SetmealMapper setmealMapper) {
        this.dishMapper = dishMapper;
        this.dishFlavorMapper = dishFlavorMapper;
        this.setmealDishMapper = setmealDishMapper;
        this.setmealMapper = setmealMapper;
    }

    /**
     * 新增菜品，默认为停售状态
     * @param dishDTO 要新增的菜品信息
     * @return
     */
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
            dishFlavorMapper.insertBatch(dishDTO.getFlavors());
        }
        return Result.success();
    }

    @Override
    public Result<PageResult> dishPageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishVOs = dishMapper.selectPage(dishPageQueryDTO);
        PageResult res = new PageResult(dishVOs.getTotal(), dishVOs.getResult() );
        return Result.success(res);

    }

    @Transactional
    @Override
    public Result<String> deleteBatchDish(List<Long> ids) {
        // 判断要删除的菜品是否正在出售
        List<Long> isSellingDishIds = dishMapper.selectSellingDishIds(ids);
        if (isSellingDishIds != null && !isSellingDishIds.isEmpty()) throw new DeletionNotAllowedException("不能删除正在售卖的菜品");
        //判断要删除的菜品是否关联有套餐
        List<Long> setmealIds = setmealDishMapper.selectSetmealIdsByDishIds(ids);
        if (setmealIds!=null && !setmealIds.isEmpty()) throw new DeletionNotAllowedException("当前菜品属于套餐中的菜品，不能删除");
        //删除菜品
        dishMapper.deleteBatchByIds(ids);
        //删除口味
        dishFlavorMapper.deleteBatchByDishIds(ids);
        return Result.success();
    }

    @Override
    public Result<Object> getBatchByCategoryId(Long categoryId) {
        List<Dish> dishes = dishMapper.selectDishByCategory(categoryId);
        return Result.success(dishes);
    }

    @Override
    public Result<Object> getById(Long id) {
        // 关联查询
        DishVO dishVOo = dishMapper.selectDishWithCategoryNameAndFlavorsByid(id);
        // 分别查询两个表，然后组装结果
//        DishVO dishVO = dishMapper.selectOneById(id);
//        dishVO.setFlavors(dishFlavorMapper.selectByDishId(id));
        return Result.success(dishVOo);
    }


    @Override
    @Transactional
    public Result<Object> updateDishWithFlavors(DishDTO dishDTO) {
        // 更新dish表
        Dish newDish = new Dish();
        BeanUtils.copyProperties(dishDTO, newDish);
        dishMapper.update(newDish);
        // 删除原有的口味数据
        dishFlavorMapper.deleteBatchByDishIds(Collections.singletonList(dishDTO.getId()));
        // 插入新的口味数据
        dishFlavorMapper.insertBatch(dishDTO.getFlavors());
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Object> updateDishStatus(Integer status, Long id) {
        // 判断当前菜品是否在其他套餐中, 如果在套餐中，相应套餐也需要停售
        if (status == StatusConstant.DISABLE){
            List<Long> setmealIds = setmealDishMapper.selectSetmealIdsByDishIds(Arrays.asList(id));
            if (!setmealIds.isEmpty()){
                for (Long sid: setmealIds){
                    Setmeal setmeal = new Setmeal();
                    setmeal.setId(sid);
                    setmeal.setStatus(StatusConstant.DISABLE);
                    setmealMapper.update(setmeal);
                }
            }
        }
        // 更改菜品状态
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
        return Result.success();
    }

    /**
     * 根据分类id和售卖状态查询菜品和口味
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.selectDishByCategoryIdAndStatus(dish.getCategoryId(), dish.getStatus());

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
