module com.example.employee {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.employee to javafx.fxml;
    exports com.example.employee;
}