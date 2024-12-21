package com.sky.controller.admin;

import com.sky.constant.CacheNameConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api("菜品相关功能接口")
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    private  final DishService dishService;
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @CacheEvict(cacheNames = CacheNameConstant.USER_DISH_CACHE_NAME, allEntries = true)
    @ApiOperation("新增菜品")
    @PostMapping("")
    public Result<Object> addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}", dishDTO.getName());
        // TODO 新增菜品
        return dishService.save(dishDTO);
    }

    @ApiOperation("按页查询菜品信息")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("按页查询菜品信息：{}", dishPageQueryDTO);
        return dishService.dishPageQuery(dishPageQueryDTO);
    }

    @CacheEvict(cacheNames = CacheNameConstant.USER_DISH_CACHE_NAME, allEntries = true)
    @ApiOperation("批量删除菜品")
    @DeleteMapping("")
    public Result<String> deleteBatchDish(@RequestParam List<Long> ids){
        log.info("批量删除菜品：{}", ids);
        // TODO 批量删除菜品
        Result<String> res = dishService.deleteBatchDish(ids);
        return res;
    }

    @ApiOperation("获得分类id获取菜品")
    @GetMapping("list")
    public Result<Object> list(@RequestParam("categoryId") Long categoryId){
        log.info("传入的分类id为{}", categoryId );
        return dishService.getBatchByCategoryId(categoryId);
    }

    @ApiOperation("根据菜品id获取菜品")
    @GetMapping("/{id}")
    public Result<Object> getById(@PathVariable("id") Long id){
        log.info("根据菜品id获取菜品：{}", id);
        return dishService.getById(id);
    }

    @CacheEvict(cacheNames = CacheNameConstant.USER_DISH_CACHE_NAME, allEntries = true)
    @ApiOperation("修改菜品")
    @PutMapping("")
    public Result<Object> updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        // TODO 修改菜品
        return dishService.updateDishWithFlavors(dishDTO);
    }

    @CacheEvict(cacheNames = {CacheNameConstant.USER_DISH_CACHE_NAME,CacheNameConstant.USER_SETMEAL_CACHE_NAME}, allEntries = true)
    @ApiOperation("更改菜品售卖状态")
    @PostMapping("/status/{status}")
    public Result<Object> changeStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        log.info("更改菜品售卖状态：{}", status);
        // TODO 更改菜品售卖状态
        return dishService.updateDishStatus(status, id);
    }
}
