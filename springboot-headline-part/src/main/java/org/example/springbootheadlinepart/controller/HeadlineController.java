package org.example.springbootheadlinepart.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springbootheadlinepart.pojo.Headline;
import org.example.springbootheadlinepart.service.Impl.HeadlineServiceImpl;
import org.example.springbootheadlinepart.util.JwtHelper;
import org.example.springbootheadlinepart.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin

@RestController
@RequestMapping("/headline")
public class HeadlineController {
    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    HeadlineServiceImpl headlineService;

    @RequestMapping("/test")
    public String test(){
        return "headline test";
    }


    @PostMapping("/publish")
    public Result<Object> publish(@RequestBody Headline headline, @RequestHeader(name="token") String token){
        Long userId = jwtHelper.getUserId(token);
        headline.setPublisher(Integer.valueOf(userId.toString()));
        headline.setPageViews(0);
        return headlineService.publish(headline);
    }

    @PostMapping("/findHeadlineByHid")
    public Result<Object> findHeadlineByHid(@RequestParam("hid") Integer hid){
       return headlineService.findHeadlineByHid(hid);
    }

    @PostMapping("/update")
    public Result<Object> update(@RequestBody Headline headline){
        return headlineService.update(headline);
    }

    @PostMapping("/removeByHid")
    public Result<Object> removeByHid(@RequestParam("hid") Integer hid){
        return headlineService.removeByHid(hid);
    }
}
