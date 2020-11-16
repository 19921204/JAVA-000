package com.geek.week_05.jdbc;

import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");

        // 查询
        String query = "select * from user";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String id = resultSet.getString("USER_ID");
            String name = resultSet.getString("USER_NAME");
            System.out.println("USER_ID:" + id + ",USER_NAME:" + name);
        }

        // 新增一条记录
        String insert = "insert into user (USER_ID, USER_NAME, PASSWORD, REAL_NAME) VALUES ('2', 'kevin', '123456', 'wdd')";
        Statement insertStatement = connection.createStatement();
        insertStatement.execute(insert);

        // 修改一条数据
        String update = "update user set PASSWORD = '654321' where USER_NAME = 'kevin'";
        Statement updateStatement = connection.createStatement();
        updateStatement.execute(update);

        // 删除数据
        String delete = "delete from user where USER_NAME = 'kevin'";
        Statement deleteStatement = connection.createStatement();
        deleteStatement.executeUpdate(delete);

        connection.close();
    }
}
