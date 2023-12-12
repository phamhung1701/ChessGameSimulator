package com.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Stack;


public class ChessApp extends Application {
    //Tạo bàn cờ và giao diện sử dụng GridPane
    Board board = new Board();
    GridPane chessboard = board.chessboard();

    //Dùng Stack để lưu trữ các nước đi
    Stack<Move> history = new Stack<>();

    //Ô đang được chọn
    private Square currentSquare = null;

    //Ô đã chọn trước đó
    private Square previousSquare = null;

    //Lượt đi
    private boolean whiteTurn = true;


    public void start(Stage stage) throws Exception {
        chessboard.setOnMouseClicked(this::handleMouseClick);

        // Create the undo button
//        Button undoButton = new Button("Undo");
//        undoButton.setOnAction(event -> undoMove());

        // Create a BorderPane and add the chessboard and the undo button
//        BorderPane borderPane = new BorderPane();
//        borderPane.setCenter(chessboard);
//        borderPane.setBottom(undoButton);


        Scene scene = new Scene(chessboard, 400, 400);
        stage.setTitle("Chess Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //Xử lý đầu vào dùng chuột
    public void handleMouseClick(MouseEvent event) {
        //Xác định tọa độ chuột click vào
        int row = (int) (event.getX() / 50);
        int column = (int) (event.getY() / 50);

        handleMove(row, column);
    }

    //Xử lý các bước đi
    public void handleMove(int row, int column) {
        //Xác định ô được chọn và ô đã chọn trước đó
        previousSquare = currentSquare;
        currentSquare = this.board.getSquare(row, column);

        //Remove previous highlights
        if (previousSquare != null) {
            previousSquare.clearHighlight();
        }

        //Kiểm tra nếu quân tốt được phong cấp hay không
        if (currentSquare.getPiece() instanceof Pawn) {
            if (((Pawn) currentSquare.getPiece()).canBePromote(currentSquare)) {
                currentSquare.setPiece(new Rook(currentSquare.getPiece().isWhite()));
                board.resetGUI(chessboard);
            }
        }

        //Hiển thị các nước đi hợp lệ cho quân cờ được chọn
        highlightValidMoves(currentSquare);

        System.out.println("row :" + currentSquare.getRow() + ", column :" + currentSquare.getColumn());

        //Thực hiện di chuyển các quân cờ
        if (previousSquare != null
                && previousSquare.getPiece() != null
                && previousSquare != currentSquare
                && previousSquare.getPiece().canMove(board, previousSquare, currentSquare)
                && ((whiteTurn && previousSquare.getPiece().isWhite())
                    || (!whiteTurn && !previousSquare.getPiece().isWhite()))) {

            //Di chuyển quân cờ và lưu lại nước đi
            Piece capturedPiece = previousSquare.movePieceTo(currentSquare);
            Move move = new Move(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
            history.push(move);

            //Đặt lại giá trị cho các biến
            previousSquare = null;
            currentSquare = null;

            //Đổi lượt
            whiteTurn = !whiteTurn;

            //Đặt lại giao diện cho bàn cờ
            board.resetGUI(chessboard);
        }
    }


    //Hàm hiển thị các nước đi hợp lệ của một quân cờ trong một ô xác định
    public void highlightValidMoves(Square start) {
        board.resetGUI(chessboard);
        hightlightSelectedPiece(start.getColumn(), start.getRow());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square end = board.getSquare(i, j);
                if (currentSquare != null && currentSquare.getPiece() != null
                        && currentSquare.getPiece().canMove(board, start, end)
                        && currentSquare.getPiece().isWhite() == this.whiteTurn) {
                    // Đổi màu của các ô được đánh dấu bằng màu xanh nhạt
                    end.highlight();
                }
            }
        }
    }


    //Hàm hiển thị ô được chọn
    public void hightlightSelectedPiece(int x, int y) {
        board.resetGUI(chessboard);
        board.getSquare(x, y).highlightSelectedPiece();
    }

    //Hoàn tác lại nước đi
    public void undoMove() {
        if (!history.isEmpty()) {
            Move lastMove = history.pop();
            lastMove.getEnd().movePieceTo(lastMove.getStart());
            if (lastMove.getCapturedPiece() != null) {
                lastMove.getEnd().setPiece(lastMove.getCapturedPiece());
            }
            // Cập nhật giao diện
            board.resetGUI(chessboard);
        }
    }

}
