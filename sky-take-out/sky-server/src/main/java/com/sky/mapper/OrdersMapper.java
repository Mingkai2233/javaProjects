package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.pojo.OrderCount;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrdersMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insertOne(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, Long id);

    /**
     * 获取指定用户的订单信息
     *
     * @param userId 用户id
     * @param status
     * @return OrderVO
     */
    Page<OrderVO> selectBatchByUserIdAndStatus(Long userId, Integer status);


    /**
     * 根据订单id获取指定订单信息
     * @param id
     * @return
     */
    OrderVO selectOneByOrderId(Long id);

    /**
     * 根据 数量，手机号，订单状态，开始时间，结束时间，用户id查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<OrderVO> selectByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    Integer countStatus(Integer status);

    List<Long> selectByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

    /**
     * 查询在指定时间段内的已经支付的订单
     * @param begin
     * @param end
     * @param status
     * @return
     */
    List<Orders> selectBatchByOrderTimeAndPayStatus(LocalDateTime begin, LocalDateTime end, Integer status);

    List<OrderCount>  countByTimeAndStatus(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    List<Long> selectIdByTime(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    /**
     * 根据给定时间，查询订单，参数均可选，如果为空，查询所有
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Orders> selectBatchByTime(LocalDateTime beginTime, LocalDateTime endTime);
}
