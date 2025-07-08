package com.ncnl.barangayapp.model;

public enum ResidentCategory {
    JUNIOR_HIGH("Junior High School"),
    SENIOR_HIGH("Senior High School"),
    COLLEGE("College Student"),
    SENIOR_CITIZEN("Senior Citizen");

    private final String label;

    ResidentCategory(String label){ this.label =  label; }

    public String getLabel() {
        return label;
    }


}
