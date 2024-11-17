import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mappers.EmployeeMapper;
import org.example.pojo.Employee;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisTest {
    @Test
    public void test() throws IOException {
        // 指定配置文件
        String mybatisConfigFilePath = "mybatis-config.xml";
        // 将配置文件以流的形式读入
        InputStream inputStream = Resources.getResourceAsStream(mybatisConfigFilePath);
        // 根据配置文件创建session工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 开启一个会话
        SqlSession sqlSession = sessionFactory.openSession();
        // 获取对应的Mapper对象
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
        // 获取对应的查询结果
        Employee employee = employeeMapper.getEmployeeById(1);

        // 插入一个实体
        Employee employee2 = new Employee();
        // mployee2.setEmpId(11);
        employee2.setEmpName("kkk");
        employee2.setEmpSalary(99.1);
        System.out.println(employee2);
        employeeMapper.insertEmployee(employee2);
        System.out.println(employee2);

//        // 插入一个Map
//        Map<String, String> map = new HashMap<>();
//        map.put("name", "fff");
//        map.put("id", "23");
//        map.put("salary", "999");
//        employeeMapper.insertEmployeeMap(map);

//        List<Employee> employees = employeeMapper.queryAll();
//        for (Employee emp : employees) {
//            System.out.println(emp);
//        }


        sqlSession.commit();
        sqlSession.close();

    }
}
