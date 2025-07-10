package com.ncnl.barangayapp.controller;

import com.ncnl.barangayapp.database.DatabaseQuery;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Text totalResidentsCounter, totalSeniorCitezenCounter, totalStudentCounter, totalUnclaimed;
    @FXML private Text currentLabelSummary, frequencyLabelSummary, conclusionLabelSummary;

    @FXML private BarChart<String, Number> residentDistributionBarChart;
    @FXML private BarChart<String, Number> genderDistributionBarChart;
    @FXML private BarChart<String, Number> studentDistributionBarChart;
    @FXML private BarChart<String, Number> claimStatusBarChart;

    private final DatabaseQuery dbquery = new DatabaseQuery();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
