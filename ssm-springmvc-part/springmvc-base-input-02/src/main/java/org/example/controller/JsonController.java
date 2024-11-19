package org.example.controller;

import org.example.pojo.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("json")
public class JsonController {

    @RequestMapping("/data1")
    @ResponseBody
    public String data1(@RequestBody Person person) {
        System.out.println(person);
        return person.toString();
    }

}
