import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.UserMapper;
import org.example.poji.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

public class CrudTest {
    SqlSession sqlSession;
    @BeforeEach
    public void setUp() throws Exception {
        String configFilePaht = "mybatis-config.xml";
        InputStream configStream = Resources.getResourceAsStream(configFilePaht);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configStream);
        sqlSession = factory.openSession(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        sqlSession.close();
    }

    @Test
    public void TestInsert(){
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setName("ergouzi");
        user.setPassword("root");
        mapper.insert(user);
        System.out.println(user);
    }

    @Test
    public void TestUpdate(){
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setName("sangouzi");
        user.setPassword("root");
        user.setId(1);
        mapper.update(user);
    }

    @Test
    public void TestDelete(){
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.delete(1);
    }

    @Test
    public void TestSelectAll(){
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.selectAll();
        for (User user : users) {
            System.out.println(user);
        }
    }
    @Test
    public void TestSelectById(){
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById(2);
        System.out.println(user);
    }
}
