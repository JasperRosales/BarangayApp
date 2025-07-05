package com.ncnl.barangayapp.service;

import com.ncnl.barangayapp.database.DatabaseManager;
import com.ncnl.barangayapp.model.Resident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResidentService {

    private final DatabaseManager db;

    public ResidentService(DatabaseManager db) {
        this.db = db;
    }


    public void insertIfNotExists(Resident resident) {
        String INSERT_RESIDENT = "INSERT OR IGNORE INTO residents " +
                "(id, fullname, sex, age, location, category, additional_info, status, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_RESIDENT)) {

            pstmt.setString(1, resident.getId().toString());
            pstmt.setString(2, resident.getFullname());
            pstmt.setString(3, resident.getSex());
            pstmt.setInt(4, resident.getAge());
            pstmt.setString(5, resident.getLocation());
            pstmt.setString(6, resident.getCategory());
            pstmt.setString(7, resident.getAdditional_info());
            pstmt.setString(8, resident.getStatus());
            pstmt.setString(9, resident.getTimestamp().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Resident> generateSimpleFakeResidents(int count) {
        List<Resident> list = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Resident r = Resident.builder()
                    .fullname("Resident " + i)
                    .sex(i % 2 == 0 ? "Male" : "Female")
                    .age(18 + (i % 50))
                    .location("Barangay " + (i % 20 + 1))
                    .category(i % 3 == 0 ? "Senior" : "Student")
                    .additional_info("No remarks")
                    .status("Claimed")
                    .build();

            list.add(r);
        }

        return list;
    }

}
