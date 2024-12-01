package org.example.springbootheadlinepart.controller;


import org.example.springbootheadlinepart.pojo.PortalVo;
import org.example.springbootheadlinepart.pojo.Type;
import org.example.springbootheadlinepart.service.Impl.PortalServiceImpl;
import org.example.springbootheadlinepart.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("portal")
@RestController
public class PortalController {
    @Autowired
    private PortalServiceImpl portalService;

    @GetMapping("/findAllTypes")
    public Result<List<Type>> findAllTypes(){
        return portalService.findAllTypes();
    }

    @PostMapping("/findNewsPage")
    public Result<Map<String, Object>> findNewsPage(@RequestBody PortalVo portalVo){
        return portalService.findNewsPage(portalVo);
    }

    @PostMapping("/showHeadlineDetail")
    public Result<Object> showHeadlineDetail(@RequestParam("hid")Integer hid){
        return portalService.showHeadlineDetail(hid);
    }
}
