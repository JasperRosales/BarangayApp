package com.ncnl.barangayapp.database;

import com.ncnl.barangayapp.repository.SqlInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DB_URL = "jdbc:sqlite:data.db";

    private static volatile DatabaseManager instance;

    private DatabaseManager() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            logger.debug("Database connection test successful.");
        } catch (SQLException e) {
            logger.error("Initial DB connection test failed", e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public <T> T withConnection(SqlInterface<T> fn) {
        try (Connection conn = getConnection()) {
            return fn.apply(conn);
        } catch (SQLException e) {
            logger.error("DB operation failed", e);
            throw new RuntimeException("DB operation failed", e);
        }
    }

}