package org.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @RequestMapping("/data1")
    @ResponseBody
    public String data1(){
        System.out.println("data1");
        return "Hello World";
    }
}
