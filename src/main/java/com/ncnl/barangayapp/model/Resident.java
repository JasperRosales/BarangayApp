package com.ncnl.barangayapp.model;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class Resident {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String lastname;
    private String firstname;
    private String middlename;
    private String qualifier; // Jr or Sr
    private String number;
    private String streetName;
    private String location; //Name of SUBD/ZONE/SITIO/PUROK
    private String placeOfBirth;
    private String dateOfBirth;
    private Integer age;
    private String sex;
    private String civilStatus;
    private String citizenship;
    private String occupation;
    private String relationshipToHouseHoldHead;
    private String status;
}


