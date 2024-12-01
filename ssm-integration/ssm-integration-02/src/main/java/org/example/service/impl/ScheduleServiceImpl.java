package org.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.mapper.ScheduleMapper;
import org.example.pojo.Schedule;
import org.example.service.ScheduleService;
import org.example.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Override
    public PageBean<Schedule> getScheduleByPage(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Schedule> scheduleList = scheduleMapper.getAllSchedule();
        PageBean<Schedule> pb = new PageBean<Schedule>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        PageInfo<Schedule> schedulePageInfo = new PageInfo<>(scheduleList);
        pb.setTotal(schedulePageInfo.getTotal());
        pb.setData(schedulePageInfo.getList());
        return pb;
    }

    @Override
    public int deleteScheduleById(int id) {
        return scheduleMapper.deleteScheduleById(id);
    }

    @Override
    public int addSchedule(Schedule schedule) {
        return scheduleMapper.insertSchedule(schedule);
    }

    @Override
    public int updateScheduleById(Schedule schedule) {
        return scheduleMapper.updateScheduleById(schedule);
    }


    @Override
    public String test() {
        List<Schedule> allSchedule = scheduleMapper.getAllSchedule();
        return allSchedule.get(0).toString();
    }


}
