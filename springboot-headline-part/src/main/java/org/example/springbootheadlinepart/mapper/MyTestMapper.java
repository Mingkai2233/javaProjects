package org.example.springbootheadlinepart.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springbootheadlinepart.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Mapper()
public interface MyTestMapper {
    public List<User> selectAll();
}
