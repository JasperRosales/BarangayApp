package com.ncnl.barangayapp.model;

public enum AgeRange {
    TEEN("13-19", 13, 19),
    YOUNG_ADULT("20-35", 20, 35),
    ADULT("36-59", 36, 59),
    SENIOR("60+", 60, Integer.MAX_VALUE);

    private final String label;
    private final int minAge;
    private final int maxAge;

    AgeRange(String label, int minAge, int maxAge) {
        this.label = label;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public String getLabel() {
        return label;
    }

    public boolean inRange(int age) {
        return age >= minAge && age <= maxAge;
    }
}
