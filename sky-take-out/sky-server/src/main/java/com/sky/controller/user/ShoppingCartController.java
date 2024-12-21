package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags="C端 购物车相关接口")
@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }


    @ApiOperation("向购物车加入一个商品(菜品或者套餐)")
    @PostMapping("/add")
    public Result<Object> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加到购物车：{}", shoppingCartDTO);
        return shoppingCartService.add(shoppingCartDTO);
    }

    @ApiOperation("显示购物车内容")
    @GetMapping("list")
    public Result<List<ShoppingCart>> list(){
        log.info("显示购物车内容");
        return shoppingCartService.list();
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result<Object> delete(){
        log.info("清空购物车");
        return shoppingCartService.clean();
    }

    @ApiOperation("删除购物车中的某一项")
    @PostMapping("/sub")
    public Result<Object> deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中的某一项：{}", shoppingCartDTO);
        return shoppingCartService.deleteOne(shoppingCartDTO);
    }
}
