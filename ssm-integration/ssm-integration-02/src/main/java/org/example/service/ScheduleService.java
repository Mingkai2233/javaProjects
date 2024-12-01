package org.example.service;

import org.example.pojo.Schedule;
import org.example.utils.PageBean;

public interface ScheduleService {
    public PageBean<Schedule> getScheduleByPage(int currentPage, int pageSize);
    public int deleteScheduleById(int id);
    public int addSchedule(Schedule schedule);
    public int updateScheduleById(Schedule schedule);
    public String test();
}
