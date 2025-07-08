package com.ncnl.barangayapp.model;


import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;


@Data
@Builder
public class Resident {
    @Builder.Default
    private  UUID id = UUID.randomUUID();
    private  String fullname;
    private  String sex;
    private  Integer age;
    private  String location;
    private  String category;
    private  String additional_info;
    private String status;
    private String timestamp;
}


