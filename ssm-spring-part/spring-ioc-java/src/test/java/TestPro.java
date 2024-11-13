import org.example.ControllerImpl.StudentControllerImpl;
import org.example.StudentConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

// @SpringJUnitConfig(value=, location=) 一个指定配置类，一个指定配置文件
@SpringJUnitConfig(value = StudentConfiguration.class)
public class TestPro {
    @Autowired
    private StudentControllerImpl studentController;

    @Test
    public void test() {
        studentController.getAll();
    }
}
