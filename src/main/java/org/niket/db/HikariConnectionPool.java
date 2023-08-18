package org.niket.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionPool {
    private static HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3307/demo");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("password");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private HikariConnectionPool() {
    }
}
