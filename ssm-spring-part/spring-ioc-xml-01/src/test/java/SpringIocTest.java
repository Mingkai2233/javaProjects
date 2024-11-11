import org.example.ioc_01.Car;
import org.example.ioc_04.JavaBean;
import org.example.ioc_04.JavaBean2;
import org.example.ioc_05.Component;
import org.example.ioc_05.ComponentFactoryBean;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringIocTest {
    public static void main(String[] args) {}

    @Test
    void createIoc(){
        // 创建的同时传入配置文件
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-ioc-01.xml");

        // 先创建在传入配置文件
        ClassPathXmlApplicationContext context1 = new ClassPathXmlApplicationContext();
        context1.setConfigLocation("spring-ioc-01.xml");
        context1.refresh();

    }

    @Test
    void getBeanFromIoc(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-ioc-01.xml");
        // 根据id直接获取
        Car car = (Car)context.getBean("car1");
        System.out.println(car);
        // 根据id和class获取
        Car car1 = context.getBean("car2", Car.class);
        System.out.println(car1);
        // 根据类型获取
        ClassPathXmlApplicationContext context2 = new ClassPathXmlApplicationContext();
        context2.setConfigLocation("spring-ioc-03.xml");
        context2.refresh(); //构造之后再传入配置文件，必须刷新
        Car car2 = context2.getBean(Car.class);
        System.out.println(car2);
    }

    @Test
    void test_04(){
        // 组件的周期方法中的init会在ioc容器创建时自动调用
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-ioc-04.xml");
        JavaBean jb = context.getBean("javaBean", JavaBean.class);
        // 组件周期方法中的destroy会在ioc容器正常销毁时自动调用
        context.close(); //不close就不会调用组件的destroy方法

        context = new ClassPathXmlApplicationContext("spring-ioc-04.xml");
        JavaBean jb1 = context.getBean("javaBean", JavaBean.class);
        JavaBean jb2 = context.getBean("javaBean", JavaBean.class);
        System.out.println(jb1==jb2);

        JavaBean jb3 = context.getBean("javaBeanMulti", JavaBean.class);
        JavaBean jb4 = context.getBean("javaBeanMulti", JavaBean.class);
        System.out.println(jb3==jb4);
    }

    @Test
    void test_05(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("spring-ioc-05.xml");
        Component com = app.getBean("component", Component.class);
        ComponentFactoryBean factoryBean = app.getBean("&component", ComponentFactoryBean.class);
        Component com1 = factoryBean.getObject();
        System.out.println(com);
        System.out.println(com1);
    }
}
