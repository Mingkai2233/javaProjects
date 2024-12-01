package org.example.springbootheadlinepart.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.springbootheadlinepart.pojo.User;

@Mapper
public interface UserMapper {
    User selectOneByUsername(@Param("name")String username);

    User selectOneByUsernameAndPassword(@Param("name") String username, @Param("password")String password);

    User selectOneById(@Param("id") Long userId);

    int  insertOne(User user);
}
