package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.pojo.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("jsp")
@Controller
public class JspController {
    // 返回一个jsp试视图
    @RequestMapping("index")
    public String index(HttpServletRequest request){
        request.setAttribute("data", "wocaonima");
        return "index";
    }

    // 重定向
    @RequestMapping("redirect")
    public String redirect(HttpServletRequest request){
        return "redirect:https://www.baidu.com";
    }
    // 转发
    @RequestMapping("forward")
    public String forward(HttpServletRequest request){
        request.setAttribute("data", "FORWARD");
        return "forward:/jsp/index";
    }

    // 返回json数据
    @GetMapping("person")
    @ResponseBody
    public Person getPerson(){
        Person person = new Person();
        person.setName("wocaonima");
        person.setAge(20);
        return person;
    }

}
