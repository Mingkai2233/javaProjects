package org.example.ServiceImpl;

import org.example.Dao.StudentDao;
import org.example.Service.StudentService;
import org.example.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;

    @Override
    public List<Student> getAll() {
        List<Student> students = studentDao.queryAll();
        System.out.println(this.getClass().getSimpleName());
        return students;
    }
}
