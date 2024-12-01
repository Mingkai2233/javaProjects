package org.example.controller;

import jakarta.data.repository.Delete;
import org.example.pojo.Schedule;
import org.example.service.ScheduleService;
import org.example.utils.PageBean;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("schedule")
@Controller
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    @ResponseBody
    @GetMapping("{pageSize}/{currentPage}")
    public R getScheduleByPage(@PathVariable(name="pageSize") int pageSize,
                               @PathVariable(name="currentPage") int currentPage) {

        return R.ok(scheduleService.getScheduleByPage(currentPage, pageSize));
    }


    @ResponseBody
    @DeleteMapping("{id}")
    public R removeSchedule(@PathVariable(name="id") int id) {
        int num = scheduleService.deleteScheduleById(id);
        if (num > 0) return R.ok("Delete schedule success");
        return R.fail("Delete schedule failed");
    }

    @ResponseBody
    @PutMapping("")
    public R modifySchedule(@Validated @RequestBody Schedule schedule) {
        if (schedule.getId() == null) return R.fail("Id cannot be null");
        int num = scheduleService.updateScheduleById(schedule);
        if (num > 0) return R.ok("Update schedule success");
        return R.fail("Update schedule failed");
    }

    @ResponseBody
    @PostMapping("")
    public R addSchedule(@Validated @RequestBody Schedule schedule) {
        int num = scheduleService.addSchedule(schedule);
        if (num > 0) return R.ok("Add schedule success");
        return R.fail("Add schedule failed");
    }
//    @ResponseBody
//    @GetMapping("test")
//    public String test() {
//        return scheduleService.test();
//    }

}
