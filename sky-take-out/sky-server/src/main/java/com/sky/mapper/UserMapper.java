package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    User selectUserByOpenid(String openid);

    Integer insertUser(User user);

    User selectOneById(Long userId);

    Long countBefore(LocalDateTime beginTime);

    List<User> selectBatchByCreateTime(LocalDateTime beginTime, LocalDateTime endTime);
}
