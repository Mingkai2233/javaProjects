package org.example;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SelectPrac {
    public static void main(String[] args) {
        String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "root";

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);){
            List<Integer> studentIds = new LinkedList<Integer>();
            // 查询
            try(PreparedStatement preparedStatement = connection.prepareStatement("select * from students")){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while (resultSet.next()) {
                        studentIds.add(resultSet.getInt("id"));
                        System.out.print(resultSet.getString("id") + " ");
                        System.out.print(resultSet.getString("name") + " ");
                        System.out.println(resultSet.getString("score"));
                    }
                }
            }
            // 插入
//            try(PreparedStatement ps = connection.prepareStatement("insert into students(grade, name, gender, score) values(?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
//                ps.setInt(1, 2);
//                ps.setString(2, "托马斯");
//                ps.setBoolean(3, true);
//                ps.setInt(4, 0);
//                int n = ps.executeUpdate();
//                try(ResultSet resultSet = ps.getGeneratedKeys()) {
//                    resultSet.next();
//                    System.out.println(resultSet.getInt(1));
//                }
//            }
//
//            // 删除
//            try(PreparedStatement ps = connection.prepareStatement("delete from students where name = ?")){
//                ps.setString(1, "托马斯");
//                ps.executeUpdate();
//            }
            // 批处理
            // 给每个学生减10分
            try (PreparedStatement ps = connection.prepareStatement("update students set score=score-10 where id=?")){
                for (var id : studentIds) {
                    ps.setInt(1, id);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
