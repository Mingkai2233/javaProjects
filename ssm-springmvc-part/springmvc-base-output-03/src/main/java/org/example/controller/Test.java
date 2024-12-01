package org.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class Test {
    // 触发异常
    @RequestMapping("exe")
    @ResponseBody
    public String exe(){
        int a = 1/0;
        return "ok";
    }
}
