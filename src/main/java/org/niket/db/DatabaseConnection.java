package org.niket.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static volatile Connection connection;

    public static Connection getConnection() throws Exception {
        if (connection == null) {
            synchronized (Connection.class) {
                if (connection == null) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/demo", "root", "password");
                    connection.setAutoCommit(false);
                }
            }
        }
        return connection;
    }
}
