package com.hmdp.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用于封装逻辑过期时间和对象，用于实现缓存逻辑过期
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
