package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.http.HttpResponse;

@Controller
@RequestMapping("cookie")
public class CookieController {
    @RequestMapping("data1")
    @ResponseBody
    public String data1(@CookieValue(name = "mycookie") String value){
        String res = "your cookie is "+value;
        return res;
    }
    @RequestMapping("save")
    @ResponseBody
    public String save(HttpServletResponse response){

        Cookie cookie = new Cookie("mycookie", "ergouzi");
        response.addCookie(cookie);
        return "ok";
    }
}
