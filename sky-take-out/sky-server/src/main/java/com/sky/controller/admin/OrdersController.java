package com.sky.controller.admin;

import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags="管理端 订单管理相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/order")
public class OrdersController {
    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @ApiOperation("条件搜索展示订单")
    @GetMapping("/conditionSearch")
    public Result<Object> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("根据条件搜索订单：{}", ordersPageQueryDTO);
        return ordersService.conditionSearch(ordersPageQueryDTO);
    }

    @ApiOperation("统计各类订单的数量")
    @GetMapping("/statistics")
    public Result<Object> statistics(){
        log.info("统计各类订单的数量");
        return ordersService.statistics();
    }

    @ApiOperation("查询订单详细信息")
    @GetMapping("/details/{id}")
    public Result<Object> getOrderDetails(@PathVariable("id") Long orderId){
        log.info("查询订单详细信息：{}", orderId);
        return ordersService.getOrderDetails(orderId);
    }

    /**
     * 接单
     *
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<Object> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单：{}", ordersConfirmDTO);

       return ordersService.confirm(ordersConfirmDTO);
    }

    /**
     * 拒单
     *
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<Object> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("商家拒单：{}", ordersRejectionDTO);
        ordersService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 派送订单
     *
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result<Object> delivery(@PathVariable("id") Long id) {
        ordersService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result<Object> complete(@PathVariable("id") Long id) {
        ordersService.complete(id);
        return Result.success();
    }
}
