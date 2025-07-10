package com.ncnl.barangayapp.service;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StageService {
    private static StageService instance;
    private Stage loadingStage;

    public static StageService getInstance() {
        if (instance == null) instance = new StageService();
        return instance;
    }

    public void showLoading(String message) {
        Platform.runLater(() -> {
            VBox box = new VBox(10, new ProgressIndicator(), new Label(message));
            box.setStyle("-fx-padding: 20; -fx-alignment: center;");
            loadingStage = new Stage();
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            loadingStage.setScene(new Scene(box, 250, 100));
            loadingStage.setTitle("Please Wait");
            loadingStage.show();
        });
    }

    public void hideLoading() {
        if (loadingStage != null) {
            Platform.runLater(loadingStage::close);
        }
    }
}
