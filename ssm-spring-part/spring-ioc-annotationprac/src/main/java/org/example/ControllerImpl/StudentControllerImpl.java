package org.example.ControllerImpl;

import org.example.Controller.StudentController;
import org.example.Service.StudentService;
import org.example.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StudentControllerImpl implements StudentController {
    @Autowired
    private StudentService studentService;

    @Override
    public void getAll() {
        List<Student> all = studentService.getAll();
        System.out.println(this.getClass().getSimpleName());

        for (Student student : all) {
            System.out.println(student);
        }
    }
}
