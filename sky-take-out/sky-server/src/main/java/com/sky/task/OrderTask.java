package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderTask {
    private final OrdersMapper ordersMapper;
    public OrderTask(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }
    /**
     * 处理超时订单， 每分钟处理一次
     */
    @Scheduled(cron = "0 * * * * *")
//    @Scheduled(cron = "*/5 * * * * *")
    public void processTimeOutOrder(){
        log.info("处理超时订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        // 获取超时订单id
        List<Long> ids = ordersMapper.selectByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        // 更新超时订单状态
        for (Long orderId : ids) {
            Orders order = new Orders();
            order.setId(orderId);
            order.setStatus(Orders.CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason("订单超时未付款自动取消");
            ordersMapper.update(order);
        }

    }

    /**
     * 处理一直处于派送中状态的订单
     */
    @Scheduled(cron="0 0 1 * * *")
    public void processDeliveryOrder(){
        log.info("处理处于派送中的订单");
        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        // 获取派送中订单id
        List<Long> ids = ordersMapper.selectByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        // 更新派送中订单状态
        for (Long orderId : ids) {
            Orders order = new Orders();
            order.setId(orderId);
            order.setStatus(Orders.COMPLETED);
            ordersMapper.update(order);
        }
    }
}
