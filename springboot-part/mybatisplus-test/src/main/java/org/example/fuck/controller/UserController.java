package org.example.fuck.controller;

import org.example.fuck.mapper.UserMapper;
import org.example.fuck.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("show")
    public List<User> getUsers() {
        List<User> allUsers = userMapper.selectList(null);
        System.out.println(allUsers);
        return allUsers;
    }
    @GetMapping("show/{id}")
    public User showUserById(@PathVariable("id") int id) {
        User user = userMapper.selectById(id);
        System.out.println(user);
        return user;
    }
}
