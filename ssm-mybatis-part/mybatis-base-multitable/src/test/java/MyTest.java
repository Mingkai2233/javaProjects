import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.OrderMapper;
import org.example.pojo.Customer;
import org.example.pojo.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class MyTest {
    SqlSession sqlSession;
    @BeforeEach
    public void setUp() throws Exception {
        String configFilePaht = "mybatisConfig.xml";
        InputStream configStream = Resources.getResourceAsStream(configFilePaht);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configStream);
        sqlSession = factory.openSession(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        sqlSession.close();
    }

    @Test
    public void getOrderWithCustomerTest(){
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        Order order = mapper.getOrderWithCustomer(2);
        System.out.println(order);
    }

    @Test
    public void getCustomerWithOrderTest(){
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        Customer customer = mapper.getCustomerWithOrder(1);
        System.out.println(customer);
        for (Order order : customer.getOrders()) {
            System.out.println(order);
        }
    }
}
