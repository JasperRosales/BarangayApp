package com.ncnl.barangayapp.mapper;

import com.ncnl.barangayapp.model.Resident;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ResidentMapper {

    public static Resident toInsertedResident(Resident source) {
        return Resident.builder()
                .id(source.getId())
                .lastname(source.getLastname())
                .firstname(source.getFirstname())
                .middlename(source.getMiddlename())
                .qualifier(source.getQualifier())
                .number(source.getNumber())
                .streetName(source.getStreetName())
                .location(source.getLocation())
                .placeOfBirth(source.getPlaceOfBirth())
                .dateOfBirth(source.getDateOfBirth())
                .age(source.getAge())
                .sex(source.getSex())
                .civilStatus(source.getCivilStatus())
                .citizenship(source.getCitizenship())
                .occupation(source.getOccupation())
                .relationshipToHouseHoldHead(source.getRelationshipToHouseHoldHead())
                .status(source.getStatus())
                .build();
    }


    public static Resident mapResultSetToResident(ResultSet rs) throws SQLException {
        return Resident.builder()
                .id(UUID.fromString(rs.getString("id")))
                .lastname(rs.getString("lastname"))
                .firstname(rs.getString("firstname"))
                .middlename(rs.getString("middlename"))
                .qualifier(rs.getString("qualifier"))
                .number(rs.getString("number"))
                .streetName(rs.getString("street_name"))
                .location(rs.getString("location"))
                .placeOfBirth(rs.getString("place_of_birth"))
                .dateOfBirth(rs.getString("date_of_birth"))
                .age(rs.getInt("age"))
                .sex(rs.getString("sex"))
                .civilStatus(rs.getString("civil_status"))
                .citizenship(rs.getString("citizenship"))
                .occupation(rs.getString("occupation"))
                .relationshipToHouseHoldHead(rs.getString("relationship_to_household_head"))
                .status(rs.getString("status"))
                .build();
    }
}
