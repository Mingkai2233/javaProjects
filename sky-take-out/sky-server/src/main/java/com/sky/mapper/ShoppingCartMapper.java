package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据dishID和dishFlavor 或 setmealId获取对应购物车记录， 利用动态sql
     * @param cart
     * @return
     */
    ShoppingCart select(ShoppingCart cart);

    /**
     * 插入一个商品的购物车记录
     * @param cart
     */
    void insertOne(ShoppingCart cart);

    /**
     * 根据购物车记录id更新商品的数量
     * @param cart
     */
    void updateNumberById(ShoppingCart cart);

    /**
     * 返回属于指定用户的购物车记录列表
     * @param userId
     * @return
     */
    List<ShoppingCart> selectBatchByUserId(Long userId);

    Integer deleteAll(Long userId);

    /**
     * 删除一条购物车记录
     * @param cart
     */
    void deleteOne(ShoppingCart cart);

    void insertBatch(List<ShoppingCart> shoppingCarts);
}
