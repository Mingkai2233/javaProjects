package org.example.Dao;

import org.example.pojo.Student;

import java.util.List;

public interface StudentDao {
    /*
        从数据库获得所有学生的数据
     */
    public List<Student> queryAll();
}
