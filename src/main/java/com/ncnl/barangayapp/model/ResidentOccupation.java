package com.ncnl.barangayapp.model;

import java.util.Arrays;
import java.util.Optional;

public enum ResidentOccupation {
    STUDENT("Student"),
    SENIOR_CITIZEN("Senior Citizen");

    private final String label;

    ResidentOccupation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Optional<ResidentOccupation> fromLabel(String label) {
        return Arrays.stream(values())
                .filter(o -> o.label.equalsIgnoreCase(label))
                .findFirst();
    }
}
