package com.ncnl.barangayapp;


import com.ncnl.barangayapp.database.DatabaseManager;
import com.ncnl.barangayapp.service.DatabaseService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class BarangayApplication extends Application{
    @Override
    public void start(Stage stage) throws IOException {

        DatabaseService databaseService = new DatabaseService(DatabaseManager.getInstance());
        databaseService.initializeTable();
        databaseService.initializeBin();

        FXMLLoader fxmlLoader = new FXMLLoader(BarangayApplication.class.getResource("MainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Barangay App");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args){ launch(); }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}