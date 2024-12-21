package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;

import java.util.List;

public interface ShoppingCartService {
    Result<Object> add(ShoppingCartDTO shoppingCartDTO);

    Result<List<ShoppingCart>> list();

    Result<Object> clean();

    Result<Object> deleteOne(ShoppingCartDTO shoppingCartDTO);
}
