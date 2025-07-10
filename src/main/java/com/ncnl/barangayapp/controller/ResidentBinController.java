package com.ncnl.barangayapp.controller;

import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.service.ResidentService;
import com.ncnl.barangayapp.service.AlertService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class ResidentBinController {

    @FXML private TableView<Resident> binTable;
    @FXML private TableColumn<Resident, String> idBinCol, lastnameBinCol, firstnameBinCol, middlenameBinCol,
            qualifierBinCol, numberBinCol, streetnameCol, locationBinCol, placeofbirthBinCol, dateofbirthBinCol,
            ageBinCol, sexBinCol, civilstatusBinCol, citizenshipBinCol, occupationBinCol,
            relationshipBinCol, statusBinCol;

    private final ResidentService service = new ResidentService();
    private final ObservableList<Resident> binResidents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId().toString()));
        lastnameBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastname()));
        firstnameBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirstname()));
        middlenameBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMiddlename()));
        qualifierBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getQualifier()));
        numberBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumber()));
        streetnameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStreetName()));
        locationBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLocation()));
        placeofbirthBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPlaceOfBirth()));
        dateofbirthBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDateOfBirth()));
        ageBinCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getAge())));
        sexBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSex()));
        civilstatusBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCivilStatus()));
        citizenshipBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCitizenship()));
        occupationBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOccupation()));
        relationshipBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRelationshipToHouseHoldHead()));
        statusBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        List<Resident> fromBin = service.loadAllResidentsInBin();
        binResidents.setAll(fromBin);
        binTable.setItems(binResidents);
        binTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void handleDeleteSelected() {
        var selected = binTable.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            AlertService.getInstance().showWarning("No Selection", "Select at least one record to delete.");
            return;
        }

        boolean confirm = AlertService.getInstance().showConfirmation("Permanent Deletion", "Delete selected permanently?");
        if (confirm) {
            service.permanentlyDeleteResidents(selected);
            binResidents.removeAll(selected);
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) binTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleRestoreSelect(ActionEvent actionEvent) {
        var selected = binTable.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            AlertService.getInstance().showWarning("No Selection", "Select at least one record to restore.");
            return;
        }

        boolean confirm = AlertService.getInstance().showConfirmation("Restore Records", "Restore selected residents?");
        if (confirm) {
            service.restoreResidents(selected);
            binResidents.removeAll(selected);
        }
    }

    @FXML
    public void refreshData(ActionEvent actionEvent) {
        List<Resident> fromBin = service.loadAllResidentsInBin();
        binResidents.setAll(fromBin);
        binTable.getSelectionModel().clearSelection();
    }

}
