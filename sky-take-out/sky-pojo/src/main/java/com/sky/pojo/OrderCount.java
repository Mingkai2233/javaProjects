package com.sky.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderCount {
    // 当天订单数量
    private int count;
    // 时间
    private LocalDate time;
}
