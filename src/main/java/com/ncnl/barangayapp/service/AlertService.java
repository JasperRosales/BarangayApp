package com.ncnl.barangayapp.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class AlertService {

    private static AlertService instance;

    private AlertService() {
    }

    public static AlertService getInstance() {
        if (instance == null) {
            instance = new AlertService();
        }
        return instance;
    }

    public void showWarning(String title, String message) {
        show(Alert.AlertType.WARNING, title, null, message);
    }

    public boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


    public void showInfo(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, null, message);
    }

    public void showError(String title, String message) {
        show(Alert.AlertType.ERROR, title, null, message);
    }

    public void show(Alert.AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String showChoice(String title, String message, List<String> options) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);

        ButtonType[] buttons = options.stream()
                .map(ButtonType::new)
                .toArray(ButtonType[]::new);

        alert.getButtonTypes().setAll(buttons);
        Optional<ButtonType> result = alert.showAndWait();
        return result.map(ButtonType::getText).orElse("Skip");
    }

}
