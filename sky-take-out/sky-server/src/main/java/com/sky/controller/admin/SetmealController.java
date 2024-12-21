package com.sky.controller.admin;

import com.sky.constant.CacheNameConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api("套餐管理")
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @ApiOperation("以分页的形式返回套餐数据")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}", setmealPageQueryDTO);
        return setmealService.getPage(setmealPageQueryDTO);
    }

    @CacheEvict(cacheNames = CacheNameConstant.USER_SETMEAL_CACHE_NAME, allEntries = true)
    @ApiOperation("新增套餐")
    @PostMapping("")
    public Result<Object> addSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套套餐：{}", setmealDTO.getName());
        return setmealService.addSetmeal(setmealDTO);
    }

    @CacheEvict(cacheNames = CacheNameConstant.USER_SETMEAL_CACHE_NAME, allEntries = true)
    @ApiOperation("批量删除套餐")
    @DeleteMapping("")
    public Result<Object> delete(@RequestParam("ids") List<Long> ids){
        log.info("批量删除套餐：{}", ids);
        return setmealService.delete(ids);
    }

    @ApiOperation("根据id查询套餐详细信息")
    @GetMapping("/{id}")
    public Result<Object> getById(@PathVariable("id") Long id){
        log.info("根据id查询套餐：{}", id);
        return setmealService.getSetmealById(id);
    }

    @CacheEvict(cacheNames = CacheNameConstant.USER_SETMEAL_CACHE_NAME, allEntries = true)
    @ApiOperation("修改套餐")
    @PutMapping("")
    public Result<Object> updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}", setmealDTO.getName());
        return setmealService.updateSetmeal(setmealDTO);
    }

    @CacheEvict(cacheNames = CacheNameConstant.USER_SETMEAL_CACHE_NAME, allEntries = true)
    @ApiOperation("修改套餐售卖状态")
    @PostMapping("status/{status}")
    public Result<Object> changeStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        log.info("更改套餐售卖状态：{}", status);
        return setmealService.updateSetmealStatus(status, id);
    }
}
