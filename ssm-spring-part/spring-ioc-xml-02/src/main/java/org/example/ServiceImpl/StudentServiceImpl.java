package org.example.ServiceImpl;

import org.example.Dao.StudentDao;
import org.example.Service.StudentService;
import org.example.pojo.Student;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private StudentDao studentDao;
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = studentDao.queryAll();
        System.out.println(this.getClass().getSimpleName());
        return students;
    }
}
