import org.example.ControllerImpl.StudentControllerImpl;
import org.example.StudentConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StudentTest {
    @Test
    public void getAllTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(StudentConfiguration.class);
        context.refresh();
        StudentControllerImpl studentController = context.getBean( StudentControllerImpl.class);
        studentController.getAll();
    }

    @Test
    public void myTest(){

    }
}
