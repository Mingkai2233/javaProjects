import org.example.Caculator;
import org.example.MyConfiguration;
import org.example.impl.MyCaculator;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {
    public static void main(String[] args) {

    }

    @Test
    public void test() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfiguration.class);
        Caculator bean = context.getBean(Caculator.class);
        bean.add(1,2);
    }
}
