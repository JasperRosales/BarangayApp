package com.ncnl.barangayapp.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InfoModal {

    public Boolean showInfoModal(String title, String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle(title);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#333"));

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Segoe UI", 14));
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(TextAlignment.JUSTIFY);
        messageLabel.setTextFill(Color.web("#555"));

        Button okButton = new Button("OK");
        okButton.setMinSize(200, 30);
        okButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        okButton.setStyle("-fx-background-color: #212121; -fx-text-fill: white; -fx-background-radius: 6;");
        okButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 8; -fx-background-radius: 8;");
        layout.getChildren().addAll(titleLabel, messageLabel, okButton);

        Scene scene = new Scene(layout, 600, 280);
        dialog.setScene(scene);
        dialog.showAndWait();

        return true;
    }
}
