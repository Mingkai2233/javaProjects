package org.example.DaoImpl;

import org.example.Dao.StudentDao;
import org.example.pojo.Student;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Student> queryAll() {
        String sql = "select id, name, gender, class as classes, age from students";
        List<Student> res = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class));
        System.out.println(this.getClass().getSimpleName());
        return res;
    }
}
