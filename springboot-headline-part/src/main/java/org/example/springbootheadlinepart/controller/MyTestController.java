package org.example.springbootheadlinepart.controller;

import org.example.springbootheadlinepart.mapper.MyTestMapper;
import org.example.springbootheadlinepart.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/mytest")
@RestController()
public class MyTestController {
    @Autowired
    private MyTestMapper myTestMapper;

    @GetMapping("/")
    public String get(){
        return "This is a test controller.";
    }
    @GetMapping("/test")
    public List<User> getAllUsers(){
        return myTestMapper.selectAll();
    }
}
