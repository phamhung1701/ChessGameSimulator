module com.example.demo7 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.chess to javafx.fxml;
    exports com.chess;
}