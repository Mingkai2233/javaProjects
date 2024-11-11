package org.example.DaoImpl;

import org.example.Dao.StudentDao;
import org.example.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDaoImpl implements StudentDao {
    //@Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
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
