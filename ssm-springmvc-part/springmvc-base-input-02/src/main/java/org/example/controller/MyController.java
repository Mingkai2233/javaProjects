package org.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("base")
public class MyController {

    @RequestMapping("/data1")
    @ResponseBody
    public String data1(){
        System.out.println("data1");
        return "Hello World";
    }

    // param形式接收参数
    @RequestMapping("/data2")
    @ResponseBody
    public String data2(@RequestParam(name="name") String name){
        System.out.println("data2");
        return "Hello World "+name;
    }
    // 参数可选，且有默认
    @RequestMapping("/data3")
    @ResponseBody
    public String data3(@RequestParam(name="name", defaultValue = "zzz", required = false)String name){
        System.out.println("data3");
        return "Hello World "+name;
    }
    // 集合接收多个参数
    @RequestMapping("/data4")
    @ResponseBody
    public String data4(@RequestParam(name="names") List<String> names){
        System.out.println("data4");
        return "Hello World "+names.toString();
    }
    // 路径传值
    @RequestMapping("/data5/{name}/{password}")
    @ResponseBody
    public String data5(@PathVariable(name = "name") String name1, @PathVariable(name="password") String password1){
        System.out.println("data4");
        return "Hello World !"+ name1 +" "+password1;
    }
}
