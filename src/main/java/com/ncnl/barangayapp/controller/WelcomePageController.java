package com.ncnl.barangayapp.controller;

import com.ncnl.barangayapp.commands.ModalCommandExecute;
import com.ncnl.barangayapp.model.DeskInfo;
import com.ncnl.barangayapp.util.InfoModal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomePageController {

    @FXML
    Button heroBtnMain;

    InfoModal infoModal = new InfoModal();
    ModalCommandExecute cmd = ModalCommandExecute.getInstance();



    public void showTutorial(ActionEvent actionEvent) {
        cmd.executeCommand(() -> infoModal.showInfoModal("What is this Application", DeskInfo.appDeskInfo));
    }

    public void showResidentFeature(ActionEvent actionEvent) {
        cmd.executeCommand(() -> infoModal.showInfoModal("Resident Page Features", DeskInfo.residentDeskInfo));
    }

    public void showDashboardFeature(ActionEvent actionEvent) {
        cmd.executeCommand(() -> infoModal.showInfoModal("Dashboard Page Features", DeskInfo.dashboardDeskInfo));
    }

    public void showExtractionFeature(ActionEvent actionEvent) {
        cmd.executeCommand(() -> infoModal.showInfoModal("Import/Export Page Features", DeskInfo.extractionDeskInfo));

    }

}