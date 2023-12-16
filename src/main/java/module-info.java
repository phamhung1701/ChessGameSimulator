module com.example.demo7 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.chess to javafx.fxml;
    exports com.chess;
    exports com.chess.pieces;
    opens com.chess.pieces to javafx.fxml;
    exports com.chess.movement;
    opens com.chess.movement to javafx.fxml;
}