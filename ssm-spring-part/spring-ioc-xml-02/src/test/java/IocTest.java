import com.alibaba.druid.pool.DruidDataSource;
import org.example.pojo.Student;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class IocTest {
    public static void main(String[] args) {

    }

    @Test
    void testSpringTemplate(){
        // 创建数据库连接池
        String dbUrl = "jdbc:mysql://localhost:3306/studb";
        String dbDrive = "com.mysql.cj.jdbc.Driver";
        String dbUser = "root";
        String dbPassword = "root";
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(dbDrive);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 插入
        String sql = "insert into students (name, gender, age, class) values (?,?,?,?)";
//        jdbcTemplate.update(sql, "zhangmk", "男", 11, "六年级一班");

        sql = "delete from students where name=?";
//        int n = jdbcTemplate.update(sql, "zhangmk");
//        System.out.printf("delete %d line\n", n);
    }
    @Test
    void testDqlForListPojo(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-ioc-01.xml");
        JdbcTemplate jdbct = context.getBean(JdbcTemplate.class);
        String sql = "select id, name, gender, class as classes, age from students";
//        List<Student> students = jdbct.query(sql, new RowMapper<Student>() {
//            @Override
//            public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Student res =  new Student();
//                res.setName(rs.getString("name"));
//                res.setGender(rs.getString("gender"));
//                res.setAge(rs.getInt("age"));
//                res.setClasses(rs.getString("class"));
//                res.setId(rs.getInt("id"));
//                return res;
//            }
//        });
        List<Student> students = jdbct.query(sql, new BeanPropertyRowMapper<Student>(Student.class));

        for (Student student : students) {
            System.out.println(student);
        }

    }

}
