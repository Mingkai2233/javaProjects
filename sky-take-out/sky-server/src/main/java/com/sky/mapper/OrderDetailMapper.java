package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.pojo.Top10Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 插入多条订单详细信息
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单id查询多条订单详细信息
     * @param orderId
     * @return
     */
    List<OrderDetail> selectBatchByOrderId(Long orderId);

    List<Top10Data> selectTop10(List<Long> orderIds);
}
