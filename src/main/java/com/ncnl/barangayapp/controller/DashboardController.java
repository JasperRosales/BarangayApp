package com.ncnl.barangayapp.controller;

import com.ncnl.barangayapp.database.DatabaseQuery;
import com.ncnl.barangayapp.model.Resident;
import com.ncnl.barangayapp.model.ResidentCategory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
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
        List<Resident> residents = dbquery.readAllResidents();

        updateCounters(residents);
        populateGenderChart(residents);
        populateCategoryChart(residents);
        populateClaimStatusChart(residents);
        populateOverallDistributionChart(residents);

        // Optional: Set summary descriptions
        currentLabelSummary.setText("Current record statistics based on resident data.");
        frequencyLabelSummary.setText("Most common category: College Student");
        conclusionLabelSummary.setText("The majority are students and unclaimed beneficiaries.");
    }

    private void updateCounters(List<Resident> residents) {
        totalResidentsCounter.setText(String.valueOf(residents.size()));

        long totalSeniors = residents.stream()
                .filter(r -> r.getCategory().equalsIgnoreCase("Senior Citizen"))
                .count();
        totalSeniorCitezenCounter.setText(String.valueOf(totalSeniors));

        long totalStudents = residents.stream()
                .filter(r -> r.getCategory().toLowerCase().contains("student") || r.getCategory().toLowerCase().contains("high"))
                .count();
        totalStudentCounter.setText(String.valueOf(totalStudents));

        long totalUnclaimedCount = residents.stream()
                .filter(r -> r.getStatus().equalsIgnoreCase("Unclaimed"))
                .count();
        totalUnclaimed.setText(String.valueOf(totalUnclaimedCount));
    }

    private void populateGenderChart(List<Resident> residents) {
        long maleCount = residents.stream().filter(r -> r.getSex().equalsIgnoreCase("Male")).count();
        long femaleCount = residents.stream().filter(r -> r.getSex().equalsIgnoreCase("Female")).count();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gender");
        series.getData().add(new XYChart.Data<>("Male", maleCount));
        series.getData().add(new XYChart.Data<>("Female", femaleCount));

        genderDistributionBarChart.getData().add(series);
    }

    private void populateCategoryChart(List<Resident> residents) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Student Categories");

        for (ResidentCategory category : ResidentCategory.values()) {
            long count = residents.stream()
                    .filter(r -> r.getCategory().equalsIgnoreCase(category.getLabel()))
                    .count();
            series.getData().add(new XYChart.Data<>(category.getLabel(), count));
        }

        studentDistributionBarChart.getData().add(series);
    }

    private void populateClaimStatusChart(List<Resident> residents) {
        long claimed = residents.stream().filter(r -> r.getStatus().equalsIgnoreCase("Claimed")).count();
        long unclaimed = residents.stream().filter(r -> r.getStatus().equalsIgnoreCase("Unclaimed")).count();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Claim Status");
        series.getData().add(new XYChart.Data<>("Claimed", claimed));
        series.getData().add(new XYChart.Data<>("Unclaimed", unclaimed));

        claimStatusBarChart.getData().add(series);
    }

    private void populateOverallDistributionChart(List<Resident> residents) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Residents Overview");

        series.getData().add(new XYChart.Data<>("Total", residents.size()));
        series.getData().add(new XYChart.Data<>("Students", residents.stream()
                .filter(r -> r.getCategory().toLowerCase().contains("student") || r.getCategory().toLowerCase().contains("high")).count()));
        series.getData().add(new XYChart.Data<>("Seniors", residents.stream()
                .filter(r -> r.getCategory().equalsIgnoreCase("Senior Citizen")).count()));
        series.getData().add(new XYChart.Data<>("Unclaimed", residents.stream()
                .filter(r -> r.getStatus().equalsIgnoreCase("Unclaimed")).count()));

        residentDistributionBarChart.getData().add(series);
    }
}
