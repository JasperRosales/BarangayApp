package com.ncnl.barangayapp.service;

import com.ncnl.barangayapp.database.DatabaseManager;

import java.sql.*;

public class DatabaseService{
    private final DatabaseManager db;
    private Connection conn;
    private Statement stmt;

    public DatabaseService(DatabaseManager db) {
        this.db = db;
    }

    public void initializeTable() {
        db.withConnection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
                CREATE TABLE IF NOT EXISTS residents (
                    id TEXT PRIMARY KEY,
                    fullname TEXT NOT NULL,
                    sex TEXT,
                    age INTEGER,
                    location TEXT,
                    category TEXT,
                    additional_info TEXT,
                    status TEXT,
                    timestamp TEXT
                );
            """);
            }
            return null;
        });
    }

    public void initializeBin(){
        db.withConnection(conn -> {
            try(Statement stmt = conn.createStatement()){
                stmt.execute("""
                        CREATE TABLE IF NOT EXISTS resident_bin (
                            id TEXT PRIMARY KEY,
                            fullname TEXT,
                            sex TEXT,
                            age INTEGER,
                            location TEXT,
                            category TEXT,
                            additional_info TEXT,
                            status TEXT,
                            timestamp TEXT
                        );
                        """);
            }
            return null;
        });
    }

}