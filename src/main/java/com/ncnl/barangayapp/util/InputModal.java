package com.ncnl.barangayapp.util;

import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.model.ResidentCategory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InputModal<T> {

    public void renderWithResident(Resident resident, Consumer<Resident> onSave) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Manage your Data");

        TextField fullnameField = new TextField(resident.getFullname());

        ComboBox<String> sexBox = new ComboBox<>();
        sexBox.getItems().addAll("Male", "Female");
        sexBox.setValue(resident.getSex());

        TextField ageField = new TextField(String.valueOf(resident.getAge()));
        TextField locationField = new TextField(resident.getLocation());

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(
                Arrays.stream(ResidentCategory.values())
                        .map(ResidentCategory::getLabel)
                        .toList()
        );

        categoryBox.setValue(resident.getCategory());

        TextField infoField = new TextField(resident.getAdditional_info());

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Claimed", "Unclaimed");
        statusBox.setValue(resident.getStatus());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Full Name:"), 0, 0);     grid.add(fullnameField, 1, 0);
        grid.add(new Label("Sex:"), 0, 1);           grid.add(sexBox, 1, 1);
        grid.add(new Label("Age:"), 0, 2);           grid.add(ageField, 1, 2);
        grid.add(new Label("Location:"), 0, 3);      grid.add(locationField, 1, 3);
        grid.add(new Label("Category:"), 0, 4);      grid.add(categoryBox, 1, 4);
        grid.add(new Label("Additional Info:"), 0, 5); grid.add(infoField, 1, 5);
        grid.add(new Label("Status:"), 0, 6);        grid.add(statusBox, 1, 6);

        Button saveBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttons, 1, 7);

        Scene scene = new Scene(grid, 400, 400);
        dialog.setScene(scene);

        saveBtn.setOnAction(e -> {
            try {
                String name = fullnameField.getText().trim();
                String sex = sexBox.getValue();
                int age = Integer.parseInt(ageField.getText());
                String location = locationField.getText().trim();
                String category = categoryBox.getValue();
                String info = infoField.getText().trim();
                String status = statusBox.getValue();

                Resident updated = Resident.builder()
                        .id(resident.getId())
                        .fullname(name)
                        .sex(sex)
                        .age(age)
                        .location(location)
                        .category(category)
                        .additional_info(info)
                        .status(status)
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
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
