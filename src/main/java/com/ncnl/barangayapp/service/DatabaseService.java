package com.ncnl.barangayapp.service;

import com.ncnl.barangayapp.database.DatabaseManager;
import com.ncnl.barangayapp.model.Resident;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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


    public ArrayList<Resident> getAllResidentData() {
        return db.withConnection(conn -> {
            ArrayList<Resident> res = new ArrayList<>();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM residents")) {

                while (rs.next()) {
                    res.add(Resident.builder()
                            .fullname(rs.getString("fullname"))
                            .age(rs.getInt("age"))
                            .sex(rs.getString("sex"))
                            .location(rs.getString("location"))
                            .category(rs.getString("category"))
                            .additional_info(rs.getString("additional_info"))
                            .status(rs.getString("status"))
                            .build());
                }
            }

            return res;
        });
    }


    public void insertFakeData(Integer count){
        ResidentService service = new ResidentService(db);

        List<Resident> fakeResidents = ResidentService.generateSimpleFakeResidents(count);

        for (Resident r : fakeResidents) {
            service.insertIfNotExists(r);
        }
    }



}