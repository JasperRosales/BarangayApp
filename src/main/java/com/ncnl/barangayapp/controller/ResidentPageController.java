package com.ncnl.barangayapp.controller;

import com.ncnl.barangayapp.model.AgeRange;
import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.model.ResidentOccupation;
import com.ncnl.barangayapp.service.AlertService;
import com.ncnl.barangayapp.service.FilterService;
import com.ncnl.barangayapp.service.ImportExportService;
import com.ncnl.barangayapp.service.ResidentService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ResidentPageController implements Initializable {

    @FXML private TextField searchBar;
    @FXML private ComboBox<String> ageBox, sexBox, categoryBox, statusBox;
    @FXML private Button clearFiltersBtn, refreshBtn, addResidentBtn, updateResidentBtn, updateStatusBtn,
            deleteResidentBtn, importDataBtn, exportDataBtn, binDataBtn;

    @FXML private TableView<Resident> residentsTable;

    @FXML private TableColumn<Resident, String> idCol, lastnameCol, firstnameCol, middlenameCol, qualifierCol,
            numberCol, streetnameCol, locationCol, placeofbirthCol, dateofbirthCol,
            sexCol, civilstatusCol, citizenshipCol, occupationCol, relationshipCol, statusCol;
    @FXML private TableColumn<Resident, Integer> ageCol;

    private final ResidentService residentService = new ResidentService();
    private final FilterService filterService = new FilterService();

    private final ObservableList<Resident> residentList = FXCollections.observableArrayList();
    private FilteredList<Resident> filteredList;

    private final ObservableList<String> ageList = FXCollections.observableArrayList();
    private final ObservableList<String> categoryList = FXCollections.observableArrayList();
    private final ObservableList<String> sexList = FXCollections.observableArrayList("— Select Sex —", "Male", "Female");
    private final ObservableList<String> statusList = FXCollections.observableArrayList("— Select Status —", "Claimed", "Unclaimed");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId().toString()));
        lastnameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastname()));
        firstnameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirstname()));
        middlenameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMiddlename()));
        qualifierCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getQualifier()));
        numberCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumber()));
        streetnameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStreetName()));
        locationCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLocation()));
        placeofbirthCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPlaceOfBirth()));
        dateofbirthCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDateOfBirth()));
        ageCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAge()).asObject());
        sexCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSex()));
        civilstatusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCivilStatus()));
        citizenshipCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCitizenship()));
        occupationCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOccupation()));
        relationshipCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRelationshipToHouseHoldHead()));
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        ageList.add("— Select Age Range —");
        ageList.addAll(Arrays.stream(AgeRange.values()).map(AgeRange::getLabel).toList());

        categoryList.add("— Select Category —");
        categoryList.addAll(Arrays.stream(ResidentOccupation.values()).map(ResidentOccupation::getLabel).toList());

        ageBox.setItems(ageList);
        categoryBox.setItems(categoryList);
        sexBox.setItems(sexList);
        statusBox.setItems(statusList);

        ageBox.getSelectionModel().selectFirst();
        categoryBox.getSelectionModel().selectFirst();
        sexBox.getSelectionModel().selectFirst();
        statusBox.getSelectionModel().selectFirst();

        residentList.addAll(residentService.loadAllResidents());
        filteredList = new FilteredList<>(residentList, p -> true);
        residentsTable.setItems(filteredList);
        residentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ageBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        categoryBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        sexBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        statusBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void applyFilters() {
        filteredList.setPredicate(
                filterService.createFilter(
                        ageBox.getValue(),
                        categoryBox.getValue(),
                        sexBox.getValue(),
                        statusBox.getValue(),
                        searchBar.getText()
                )
        );
    }

    @FXML
    public void clearFilters() {
        searchBar.clear();
        ageBox.getSelectionModel().selectFirst();
        categoryBox.getSelectionModel().selectFirst();
        sexBox.getSelectionModel().selectFirst();
        statusBox.getSelectionModel().selectFirst();
        applyFilters();
    }

    public void showInputModal(ActionEvent actionEvent) {
        residentService.createNewResident(resident -> {
            residentList.add(resident);
            applyFilters();
        });
    }

    @FXML
    public void handleDeleteResident(ActionEvent event) {
        var selected = residentsTable.getSelectionModel().getSelectedItems();
        residentService.temporaryDeleteResident(selected);
        residentList.removeAll(selected);
        residentsTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleRefreshTable(ActionEvent actionEvent) {
        residentList.clear();
        residentList.addAll(residentService.loadAllResidents());
        applyFilters();
    }

    @FXML
    public void handleChangeStatus(ActionEvent actionEvent) {
        var selected = residentsTable.getSelectionModel().getSelectedItems();
        residentService.toggleResidentStatus(selected);
        residentsTable.refresh();
    }

    @FXML
    public void handleUpdateData(ActionEvent actionEvent) {
        var selected = residentsTable.getSelectionModel().getSelectedItem();
        residentService.updateResident(selected);
        residentsTable.refresh();
    }

    @FXML
    public void handleDataImport(ActionEvent actionEvent) {
        ImportExportService.handleDataImport();
        residentList.clear();
        residentList.addAll(residentService.loadAllResidents());
        applyFilters();
    }

    @FXML
    public void handleDataExport(ActionEvent actionEvent) {
        ImportExportService.handleDataExport(residentList);
    }

    @FXML
    public void handleBin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ncnl/barangayapp/ResidentBin.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Deleted Residents");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertService.getInstance().showWarning("Failed", "Could not open Bin view.");
        }
    }
}
