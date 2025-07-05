package com.ncnl.barangayapp.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class Resident {

    @Builder.Default
    private final UUID id = UUID.randomUUID();

    private final String fullname;
    private final String sex;
    private final Integer age;
    private final String location;
    private final String category;
    private final String additional_info;
    private final String status;

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();


}


