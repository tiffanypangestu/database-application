package com.example.employee;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connectionDatabase () {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection
                    ("jdbc:mysql:://localhost/", "root", "");
            return connect;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
