import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import npu.edu.Application;
import npu.edu.entity.User;
import npu.edu.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class MyTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void testSelectList(){
        userMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    public void testSelectMapById(){
        System.out.println(userMapper.selectMapById(1L));
    }

    @Test
    public void test01(){
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.gt(User::getAge, 20).or().isNull(User::getEmail);
        userMapper.selectList(qw).forEach(System.out::println);
        userMapper.update
    }
}
