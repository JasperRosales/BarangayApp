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
                    lastname TEXT,
                    firstname TEXT,
                    middlename TEXT,
                    qualifier TEXT,
                    number TEXT,
                    street_name TEXT,
                    location TEXT,
                    place_of_birth TEXT,
                    date_of_birth TEXT,
                    age INTEGER,
                    sex TEXT,
                    civil_status TEXT,
                    citizenship TEXT,
                    occupation TEXT,
                    relationship_to_household_head TEXT,
                    status TEXT
                );
            """);
            }
            return null;
        });
    }


    public void initializeBin() {
        db.withConnection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
                CREATE TABLE IF NOT EXISTS resident_bin (
                    id TEXT PRIMARY KEY,
                    lastname TEXT,
                    firstname TEXT,
                    middlename TEXT,
                    qualifier TEXT,
                    number TEXT,
                    street_name TEXT,
                    location TEXT,
                    place_of_birth TEXT,
                    date_of_birth TEXT,
                    age INTEGER,
                    sex TEXT,
                    civil_status TEXT,
                    citizenship TEXT,
                    occupation TEXT,
                    relationship_to_household_head TEXT,
                    status TEXT
                );
            """);
            }
            return null;
        });
    }


}