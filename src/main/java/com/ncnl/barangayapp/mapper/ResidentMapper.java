package com.ncnl.barangayapp.mapper;

import com.ncnl.barangayapp.model.Resident;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ResidentMapper {

    public static Resident toInsertedResident(Resident source) {
        return Resident.builder()
                .id(source.getId())
                .fullname(source.getFullname())
                .sex(source.getSex())
                .age(source.getAge())
                .location(source.getLocation())
                .category(source.getCategory())
                .additional_info(source.getAdditional_info())
                .status(source.getStatus())
                .timestamp(source.getTimestamp())
                .build();
    }

    public static Resident mapResultSetToResident(ResultSet rs) throws SQLException{
        return Resident.builder()
                .id(UUID.fromString(rs.getString("id")))
                .fullname( rs.getString("fullname"))
                .sex( rs.getString("sex"))
                .age(rs.getInt("age"))
                .location(rs.getString("location"))
                .category(rs.getString("category"))
                .additional_info(rs.getString("additional_info"))
                .status(rs.getString("status"))
                .timestamp(rs.getString("timestamp"))
                .build();
    }
}
