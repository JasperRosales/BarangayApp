package com.ncnl.barangayapp.service;

import com.ncnl.barangayapp.model.AgeRange;
import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.model.ResidentOccupation;

import java.util.Arrays;
import java.util.function.Predicate;

public class FilterService {

    public Predicate<Resident> createFilter(String selectedAge, String selectedCategory, String selectedSex, String selectedStatus, String searchText) {
        String search = searchText == null ? "" : searchText.toLowerCase().trim();

        return resident -> {
            boolean matchesAge = selectedAge == null || selectedAge.startsWith("—") ||
                    getAgeRangeLabel(resident.getAge()).equalsIgnoreCase(selectedAge);

            boolean matchesCategory = selectedCategory == null || selectedCategory.startsWith("—") ||
                    ResidentOccupation.fromLabel(selectedCategory).map(occ -> occ.getLabel().equalsIgnoreCase(selectedCategory)).orElse(false);

            boolean matchesSex = selectedSex == null || selectedSex.startsWith("—") ||
                    resident.getSex().equalsIgnoreCase(selectedSex);

            boolean matchesStatus = selectedStatus == null || selectedStatus.startsWith("—") ||
                    resident.getStatus().equalsIgnoreCase(selectedStatus);

            boolean matchesSearch = search.isEmpty()
                    || resident.getLastname().toLowerCase().contains(search)
                    || resident.getFirstname().toLowerCase().contains(search)
                    || resident.getMiddlename().toLowerCase().contains(search)
                    || resident.getQualifier().toLowerCase().contains(search)
                    || resident.getNumber().toLowerCase().contains(search)
                    || resident.getStreetName().toLowerCase().contains(search)
                    || resident.getLocation().toLowerCase().contains(search)
                    || resident.getPlaceOfBirth().toLowerCase().contains(search)
                    || resident.getDateOfBirth().toLowerCase().contains(search)
                    || resident.getCivilStatus().toLowerCase().contains(search)
                    || resident.getCitizenship().toLowerCase().contains(search)
                    || resident.getOccupation().toLowerCase().contains(search)
                    || resident.getRelationshipToHouseHoldHead().toLowerCase().contains(search)
                    || resident.getStatus().toLowerCase().contains(search)
                    || String.valueOf(resident.getAge()).contains(search);

            return matchesAge && matchesCategory && matchesSex && matchesStatus && matchesSearch;
        };
    }

    private String getAgeRangeLabel(int age) {
        return Arrays.stream(AgeRange.values())
                .filter(range -> range.inRange(age))
                .map(AgeRange::getLabel)
                .findFirst()
                .orElse("— Select Age Range —");
    }
}
