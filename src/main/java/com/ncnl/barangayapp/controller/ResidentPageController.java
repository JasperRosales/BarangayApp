package com.ncnl.barangayapp.controller;

import com.ncnl.barangayapp.database.DatabaseManager;
import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.service.DatabaseService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ResidentPageController implements Initializable {

    @FXML private TextField searchBar;
    @FXML private ComboBox<String> ageBox, sexBox, categoryBox, statusBox;
    @FXML private Button addBtn;
    @FXML private TableView<Resident> residentsTable;
    @FXML private TableColumn<Resident, String> fullnameCol, ageCol, sexCol, locationCol,
            categoryCol, addInfoCol, statusCol;


    DatabaseService databaseService = new DatabaseService(DatabaseManager.newInstance());

    ObservableList<Resident> list = FXCollections.observableArrayList(
            databaseService.getAllResidentData()
    );


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fullnameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFullname()));
        ageCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAge()).asString());
        sexCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSex()));
        locationCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLocation()));
        categoryCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCategory()));
        addInfoCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAdditional_info()));
        statusCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatus()));

        residentsTable.setItems(list);
    }
}
