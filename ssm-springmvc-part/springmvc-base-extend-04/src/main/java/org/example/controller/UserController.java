package org.example.controller;

import org.example.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("user")
@Controller
public class UserController {
    @RequestMapping("test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @PostMapping("regist")
    @ResponseBody
    public String regist(@Validated @RequestBody User user, BindingResult bs) {
        if (bs.hasErrors()) {
            return "error, please try again";
        }
        return "ok";
    }
}
