package com.sky.service;

import com.sky.dto.*;
import com.sky.result.Result;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

public interface OrdersService {
    Result<OrderSubmitVO> submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    Result<Object> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    Result<Object> getOrder(Long id);

    Result<Object> cancel(Long id);

    Result<Object> repetition(Long id);

    Result<Object> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    Result<Object> statistics();

    Result<Object> getOrderDetails(Long orderId);

    Result<Object> confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void delivery(Long id);

    void complete(Long id);

    /**
     * 检查客户的收货地址是否超出配送范围
     * @param address
     */
    void checkOutOfRange(String address);

    void reminder(Long id);
}
