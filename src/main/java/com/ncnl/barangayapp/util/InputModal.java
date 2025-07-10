package com.ncnl.barangayapp.util;

import com.ncnl.barangayapp.model.Resident;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class InputModal<T> {

    public void renderWithResident(Resident resident, Consumer<Resident> onSave) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Manage your Data");

        TextField lastnameField = new TextField(resident.getLastname());
        TextField firstnameField = new TextField(resident.getFirstname());
        TextField middlenameField = new TextField(resident.getMiddlename());
        TextField qualifierField = new TextField(resident.getQualifier());
        TextField numberField = new TextField(resident.getNumber());
        TextField streetField = new TextField(resident.getStreetName());
        TextField locationField = new TextField(resident.getLocation());
        TextField pobField = new TextField(resident.getPlaceOfBirth());
        TextField dobField = new TextField(resident.getDateOfBirth());
        TextField ageField = new TextField(String.valueOf(resident.getAge()));

        ComboBox<String> sexBox = new ComboBox<>();
        sexBox.getItems().addAll("Male", "Female");
        sexBox.setValue(resident.getSex());

        TextField civilStatusField = new TextField(resident.getCivilStatus());
        TextField citizenshipField = new TextField(resident.getCitizenship());
        TextField occupationField = new TextField(resident.getOccupation());
        TextField relationshipField = new TextField(resident.getRelationshipToHouseHoldHead());

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Claimed", "Unclaimed");
        statusBox.setValue(resident.getStatus());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        int row = 0;
        grid.add(new Label("Last Name:"), 0, row); grid.add(lastnameField, 1, row++);
        grid.add(new Label("First Name:"), 0, row); grid.add(firstnameField, 1, row++);
        grid.add(new Label("Middle Name:"), 0, row); grid.add(middlenameField, 1, row++);
        grid.add(new Label("Qualifier:"), 0, row); grid.add(qualifierField, 1, row++);
        grid.add(new Label("House # / Lot #:"), 0, row); grid.add(numberField, 1, row++);
        grid.add(new Label("Street Name:"), 0, row); grid.add(streetField, 1, row++);
        grid.add(new Label("Location:"), 0, row); grid.add(locationField, 1, row++);
        grid.add(new Label("Place of Birth:"), 0, row); grid.add(pobField, 1, row++);
        grid.add(new Label("Date of Birth:"), 0, row); grid.add(dobField, 1, row++);
        grid.add(new Label("Age:"), 0, row); grid.add(ageField, 1, row++);
        grid.add(new Label("Sex:"), 0, row); grid.add(sexBox, 1, row++);
        grid.add(new Label("Civil Status:"), 0, row); grid.add(civilStatusField, 1, row++);
        grid.add(new Label("Citizenship:"), 0, row); grid.add(citizenshipField, 1, row++);
        grid.add(new Label("Occupation:"), 0, row); grid.add(occupationField, 1, row++);
        grid.add(new Label("Relationship to Head:"), 0, row); grid.add(relationshipField, 1, row++);
        grid.add(new Label("Status:"), 0, row); grid.add(statusBox, 1, row++);

        Button saveBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttons, 1, row);

        Scene scene = new Scene(grid, 450, 700);
        dialog.setScene(scene);

        saveBtn.setOnAction(e -> {
            try {
                Resident updated = Resident.builder()
                        .id(resident.getId())
                        .lastname(lastnameField.getText().trim())
                        .firstname(firstnameField.getText().trim())
                        .middlename(middlenameField.getText().trim())
                        .qualifier(qualifierField.getText().trim())
                        .number(numberField.getText().trim())
                        .streetName(streetField.getText().trim())
                        .location(locationField.getText().trim())
                        .placeOfBirth(pobField.getText().trim())
                        .dateOfBirth(dobField.getText().trim())
                        .age(Integer.parseInt(ageField.getText().trim()))
                        .sex(sexBox.getValue())
                        .civilStatus(civilStatusField.getText().trim())
                        .citizenship(citizenshipField.getText().trim())
                        .occupation(occupationField.getText().trim())
                        .relationshipToHouseHoldHead(relationshipField.getText().trim())
                        .status(statusBox.getValue())
                        .build();

                onSave.accept(updated);
                dialog.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Please fill out all fields correctly.").showAndWait();
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());
        dialog.showAndWait();
    }
}
