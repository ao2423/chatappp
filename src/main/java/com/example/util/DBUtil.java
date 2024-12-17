package com.example.util; // 追加
import java.sql.Connection; // データベース接続を表す
import java.sql.DriverManager; // JDBCドライバの管理を行う

public class DBUtil {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/chatapp";
        String user = "root";
        String password = "0812";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}

