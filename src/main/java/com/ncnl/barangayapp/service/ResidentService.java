package com.ncnl.barangayapp.service;

import com.ncnl.barangayapp.commands.DBCommandExecute;
import com.ncnl.barangayapp.commands.ModalCommandExecute;
import com.ncnl.barangayapp.database.DatabaseQuery;
import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.util.InputModal;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ResidentService {

    private final DatabaseQuery dbQuery = new DatabaseQuery();
    private final DBCommandExecute dbCommand = DBCommandExecute.getInstance();

    public List<Resident> loadAllResidents() {
        return dbCommand.executeCommand(dbQuery::readAllResidents);
    }

    public List<Resident> loadAllResidentsInBin() {
        return dbCommand.executeCommand(dbQuery::readAllFromBin);
    }

    public void createNewResident(Consumer<Resident> onSuccess) {
        Resident newResident = Resident.builder()
                .lastname("N/A")
                .firstname("N/A")
                .middlename("N/A")
                .qualifier("N/A")
                .number("N/A")
                .streetName("N/A")
                .location("N/A")
                .placeOfBirth("N/A")
                .dateOfBirth("N/A")
                .age(0)
                .sex("Male")
                .civilStatus("N/A")
                .citizenship("N/A")
                .occupation("N/A")
                .relationshipToHouseHoldHead("N/A")
                .status("Unclaimed")
                .build();

        ModalCommandExecute.getInstance().executeCommand(() -> {
            new InputModal<Resident>().renderWithResident(newResident, result -> {
                dbCommand.executeCommand(() -> dbQuery.create(result));
                onSuccess.accept(result);
            });
            return newResident;
        });
    }

    public void restoreResidents(ObservableList<Resident> residentsToRestore) {
        if (residentsToRestore == null || residentsToRestore.isEmpty()) {
            AlertService.getInstance().showWarning("No Selection", "Please select at least one resident to restore.");
            return;
        }
        boolean confirmed = AlertService.getInstance().showConfirmation(
                "Confirm Restore",
                "Restore " + residentsToRestore.size() + " resident(s) back to main records?");
        if (confirmed) {
            for (Resident r : residentsToRestore) {
                dbQuery.create(r);
                dbQuery.deleteFromBin(r.getId());
            }
        }
    }

    public void temporaryDeleteResident(ObservableList<Resident> residentsToDelete) {
        if (residentsToDelete == null || residentsToDelete.isEmpty()) {
            AlertService.getInstance().showWarning("No Selection", "Please select at least one resident to delete.");
            return;
        }
        boolean confirmed = AlertService.getInstance().showConfirmation(
                "Confirm Deletion",
                "Are you sure you want to delete " + residentsToDelete.size() + " resident(s)?");

        if (!confirmed) return;
        for (Resident r : residentsToDelete) {
            if (dbQuery.existsInBin(String.valueOf(r.getId()))) {
                String choice = AlertService.getInstance().showChoice(
                        "Duplicate in Bin",
                        "Resident with ID " + r.getId() + " already exists in the bin. What do you want to do?",
                        List.of("Skip", "Overwrite", "Delete & Re-Add")
                );
                switch (choice) {
                    case "Skip" -> {
                        dbQuery.delete(r.getId());
                        continue;
                    }
                    case "Overwrite", "Delete & Re-Add" -> {
                        dbQuery.deleteFromBin(r.getId());
                    }
                }
            }
            dbQuery.moveToBin(r);
            dbQuery.delete(r.getId());
        }
    }

    public void permanentlyDeleteResidents(ObservableList<Resident> binResidentsToDelete) {
        if (binResidentsToDelete == null || binResidentsToDelete.isEmpty()) {
            AlertService.getInstance().showWarning("No Selection", "Please select at least one resident to delete permanently.");
            return;
        }
        boolean confirmed = AlertService.getInstance().showConfirmation(
                "Confirm Permanent Deletion",
                "This action cannot be undone. Are you sure you want to permanently delete "
                        + binResidentsToDelete.size() + " resident(s)?");
        if (confirmed) {
            for (Resident r : binResidentsToDelete) {
                dbQuery.deleteFromBin(r.getId());
            }
        }
    }

    public void toggleResidentStatus(ObservableList<Resident> selectedResidents) {
        if (selectedResidents == null || selectedResidents.isEmpty()) {
            AlertService.getInstance().showWarning("No Selection", "Please select at least one resident to toggle status.");
            return;
        }
        if (AlertService.getInstance().showConfirmation(
                "Confirm Status Change",
                "Are you sure you want to toggle the status of " + selectedResidents.size() + " resident(s)?")) {

            for (Resident resident : selectedResidents) {
                String newStatus = resident.getStatus().equalsIgnoreCase("Claimed") ? "Unclaimed" : "Claimed";
                dbQuery.updateResidentStatus(resident.getId(), newStatus);
                resident.setStatus(newStatus);
            }
        }
    }

    public void updateResident(Resident selectedResident) {
        if (selectedResident == null) {
            AlertService.getInstance().showWarning("No Selection", "Please select a resident to update.");
            return;
        }

        ModalCommandExecute.getInstance().executeCommand(() -> {
            new InputModal<Resident>().renderWithResident(selectedResident, updated -> {
                dbQuery.update(selectedResident.getId(), updated);
                selectedResident.setLastname(updated.getLastname());
                selectedResident.setFirstname(updated.getFirstname());
                selectedResident.setMiddlename(updated.getMiddlename());
                selectedResident.setQualifier(updated.getQualifier());
                selectedResident.setNumber(updated.getNumber());
                selectedResident.setStreetName(updated.getStreetName());
                selectedResident.setLocation(updated.getLocation());
                selectedResident.setPlaceOfBirth(updated.getPlaceOfBirth());
                selectedResident.setDateOfBirth(updated.getDateOfBirth());
                selectedResident.setAge(updated.getAge());
                selectedResident.setSex(updated.getSex());
                selectedResident.setCivilStatus(updated.getCivilStatus());
                selectedResident.setCitizenship(updated.getCitizenship());
                selectedResident.setOccupation(updated.getOccupation());
                selectedResident.setRelationshipToHouseHoldHead(updated.getRelationshipToHouseHoldHead());
                selectedResident.setStatus(updated.getStatus());
            });
            return selectedResident;
        });
    }

    public boolean exists(UUID id) {
        return dbQuery.existsInMain(String.valueOf(id));
    }

    public void addImportedResident(Resident resident) {
        dbCommand.executeCommand(() -> dbQuery.create(resident));
    }
}
