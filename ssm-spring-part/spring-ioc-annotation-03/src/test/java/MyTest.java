import org.example.ioc_01.components.CommonComponent;
import org.example.ioc_01.components.XxxController;
import org.example.ioc_01.components.XxxDao;
import org.example.ioc_01.components.XxxService;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    @Test
    public void testIocAnnotation01() {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-01.xml");
        CommonComponent bean1 = context.getBean(CommonComponent.class);
        XxxController bean2 = context.getBean(XxxController.class);
        XxxService bean3 = (XxxService) context.getBean("xxxService");
        XxxDao bean4 = context.getBean("xxxDao", XxxDao.class);
        System.out.println(bean4);

        context.close();
    }
    public void testIocAnnotation03() {

    }
}
