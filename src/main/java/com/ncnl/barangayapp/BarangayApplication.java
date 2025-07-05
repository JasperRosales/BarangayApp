package com.ncnl.barangayapp;


import com.ncnl.barangayapp.database.DatabaseManager;
import com.ncnl.barangayapp.service.DatabaseService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;


public class BarangayApplication extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        DatabaseService databaseService = new DatabaseService(DatabaseManager.newInstance());

        databaseService.initializeTable();
        databaseService.insertFakeData(5);

        FXMLLoader fxmlLoader = new FXMLLoader(BarangayApplication.class.getResource("HomePage.fxml"));
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