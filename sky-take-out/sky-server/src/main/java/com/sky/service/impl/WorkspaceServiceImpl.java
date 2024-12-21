package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     *
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /**
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 订单完成率：有效订单数 / 总订单数
         * 平均客单价：营业额 / 有效订单数
         * 新增用户：当日新增用户的数量
         */

        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        // 获取所有订单
        List<Orders> orders = ordersMapper.selectBatchByTime(begin, end);
        //查询总订单数
        Integer totalOrderCount = orders.size();

        map.put("status", Orders.COMPLETED);
        //营业额
        Double turnover = orders.stream().map(Orders::getAmount).map(BigDecimal::doubleValue).reduce(Double::sum).orElse(0.0);
        turnover = turnover == null ? 0.0 : turnover;

        //有效订单数
        Integer validOrderCount = (int) orders.stream().filter(o -> o.getStatus().equals(Orders.COMPLETED)).count();

        Double unitPrice = 0.0;

        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0 && validOrderCount != 0) {
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }

        //新增用户数
        List<User> users = userMapper.selectBatchByCreateTime(begin, end);
        Integer newUsers = users.size();

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    /**
     * 查询订单管理数据
     *
     * @return
     */
    public OrderOverViewVO getOrderOverView() {
//        Map map = new HashMap();
//        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
//        map.put("status", Orders.TO_BE_CONFIRMED);

        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        List<Orders> orders = ordersMapper.selectBatchByTime(begin, null);
        //待接单
        Integer waitingOrders = (int) orders.stream().filter(o -> o.getStatus().equals(Orders.TO_BE_CONFIRMED)).count();
        //待派送
        Integer deliveredOrders = (int) orders.stream().filter(o -> o.getStatus().equals(Orders.CONFIRMED)).count();
        //已完成
        Integer completedOrders = (int) orders.stream().filter(o -> o.getStatus().equals(Orders.COMPLETED)).count();
        //已取消
        Integer cancelledOrders = (int) orders.stream().filter(o -> o.getStatus().equals(Orders.CANCELLED)).count();
        //全部订单
        Integer allOrders = orders.size();

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询菜品总览
     *
     * @return
     */
    public DishOverViewVO getDishOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
