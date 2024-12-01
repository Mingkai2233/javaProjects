package org.example.controller;

import org.example.event.UserLonginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    ApplicationEventPublisher publisher;
    @ResponseBody
    @RequestMapping("/login/{name}/{password}")
    public String login(@PathVariable("name") String name, @PathVariable("password") String password){
        publisher.publishEvent(new UserLonginEvent(name));
        return "login success";
    }
}
