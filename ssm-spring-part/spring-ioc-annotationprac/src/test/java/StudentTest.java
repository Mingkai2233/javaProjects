import org.example.ControllerImpl.StudentControllerImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class StudentTest {
    @Test
    public void getAllTest(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-students.xml");
        StudentControllerImpl studentController = context.getBean( StudentControllerImpl.class);
        studentController.getAll();
    }

    @Test
    public void myTest(){

    }
}
