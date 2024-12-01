package org.example.mapper;

import org.example.pojo.Schedule;
import org.example.utils.PageBean;

import java.util.List;

public interface ScheduleMapper {
    public List<Schedule> getAllSchedule();
    public int deleteScheduleById(int id);
    public int updateScheduleById(Schedule schedule);
    public int insertSchedule(Schedule schedule);
}
