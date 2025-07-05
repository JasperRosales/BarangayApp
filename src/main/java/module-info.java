module com.ncnl.barangayapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires org.controlsfx.controls;
    requires static lombok;
    requires jdk.jfr;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires org.xerial.sqlitejdbc;
    opens com.ncnl.barangayapp.controller to javafx.fxml;
    exports com.ncnl.barangayapp;
    exports com.ncnl.barangayapp.controller;
}