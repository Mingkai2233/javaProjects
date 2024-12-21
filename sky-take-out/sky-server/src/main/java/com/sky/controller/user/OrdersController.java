package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "C端订单接口")
@Slf4j
@RestController("userOrdersController")
@RequestMapping("/user/order")
public class OrdersController {
    private final OrdersService ordersService;
    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @ApiOperation("提交订单接口")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("提交订单：{}", ordersSubmitDTO);
        return ordersService.submit(ordersSubmitDTO);
    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = ordersService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    @ApiOperation("用户查询历史订单")
    @GetMapping("/historyOrders")
    public Result<Object> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("查询历史订单：{}", ordersPageQueryDTO);
        return ordersService.getHistoryOrders(ordersPageQueryDTO);
    }

    @ApiOperation("查询单个订单")
    @GetMapping("/orderDetail/{id}")
    public Result<Object> getOrderDetail(@PathVariable("id") Long id) {
        log.info("查询单个订单：{}", id);
        return ordersService.getOrder(id);
    }

    @ApiOperation("用户取消订单")
    @PutMapping("/cancel/{id}")
    public Result<Object> cancel(@PathVariable("id") Long id) {
        log.info("取消订单：{}", id);
        return ordersService.cancel(id);
    }

    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result<Object> confirm(@PathVariable("id") Long id) {
        log.info("再来一单：{}", id);
        return ordersService.repetition(id);
    }

    @ApiOperation("用户催单")
    @GetMapping("/reminder/{id}")
    public Result<Object> reminder(@PathVariable("id") Long id) {
        log.info("用户催单：{}", id);
        ordersService.reminder(id);
        return Result.success();
    }
}
