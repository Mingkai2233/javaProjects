package org.example.ControllerImpl;

import org.example.Controller.StudentController;
import org.example.Service.StudentService;
import org.example.pojo.Student;

import java.util.List;

public class StudentControllerImpl implements StudentController {
    private StudentService studentService;
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void getAll() {
        List<Student> all = studentService.getAll();
        System.out.println(this.getClass().getSimpleName());

        for (Student student : all) {
            System.out.println(student);
        }
    }
}
