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
    @FXML private TableColumn<Resident, String> idBinCol, fullnameBinCol, ageBinCol, sexBinCol, locationBinCol,
            categoryBinCol, addInfoBinCol,statusBinCol;

    private final ResidentService service = new ResidentService();
    private final ObservableList<Resident> binResidents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId().toString()));
        fullnameBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFullname()));
        ageBinCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getAge())));
        sexBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSex()));
        locationBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLocation()));
        categoryBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));
        addInfoBinCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAdditional_info()));
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
