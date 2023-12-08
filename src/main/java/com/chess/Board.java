package com.chess;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Board {
    //Lớp bàn cờ
    //Mảng 2D biểu diễn các ô trên bàn cờ
    Square[][] board = new Square[8][8]; //Cột trước, hàng sau


    //Hàm dựng
    public Board() {
        initializeChessboard();
    }

    //Khởi tạo bàn cờ
    public void initializeChessboard() {
        //Khởi tạo các ô của bàn cờ
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(j, i, null); // Corrected indices
            }
        }

        //Thêm vào bàn cờ các quân tốt
        for (int i = 0; i < 8; i++) {
            board[i][6].setPiece(new Pawn(true));
            board[i][1].setPiece(new Pawn(false));
        }

        //Thêm vào bàn cờ các quân xe
        board[0][7].setPiece(new Rook(true));
        board[0][0].setPiece(new Rook(false));
        board[7][0].setPiece(new Rook(false));
        board[7][7].setPiece(new Rook(true));

        //Thêm vào bàn cờ các quân tượng
        board[2][7].setPiece(new Bishop(true));
        board[5][7].setPiece(new Bishop(true));
        board[2][0].setPiece(new Bishop(false));
        board[5][0].setPiece(new Bishop(false));
    }

    //Khởi tạo giao diện cho bàn cờ
    public GridPane chessboard() {
        GridPane chessboard = new GridPane();
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                chessboard.add(board[i][j].getRectangle(), i, j);
                if (board[i][j].getPiece() != null) {
                    ImageView pieceImage = board[i][j].getPiece().image();

                    // Chỉnh cho ảnh ở trung tâm của ô
                    double offsetX = (board[i][j].getRectangle().getWidth() - pieceImage.getImage().getWidth()) / 2;
                    double offsetY = (board[i][j].getRectangle().getHeight() - pieceImage.getImage().getHeight()) / 2;

                    // Thêm ảnh vào hình và điều chỉnh lại
                    chessboard.add(pieceImage, i, j);
                    chessboard.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(pieceImage, HPos.CENTER);
                    GridPane.setValignment(pieceImage, VPos.CENTER);
                }
            }
        }
        return chessboard;
    }

    //Đặt lại giao diện của bàn cờ
    void resetGUI(GridPane sourceBoard) {
        sourceBoard.getChildren().clear(); // Clear existing children from the sourceBoard
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sourceBoard.add(board[i][j].getRectangle(), i, j);
                if (board[i][j].getPiece() != null) {
                    ImageView pieceImage = board[i][j].getPiece().image();

                    // Center the piece image in the square
                    double offsetX = (board[i][j].getRectangle().getWidth() - pieceImage.getImage().getWidth()) / 2;
                    double offsetY = (board[i][j].getRectangle().getHeight() - pieceImage.getImage().getHeight()) / 2;

                    // Add the piece image to the sourceBoard and set its alignment
                    sourceBoard.add(pieceImage, i, j);
                    sourceBoard.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(pieceImage, HPos.CENTER);
                    GridPane.setValignment(pieceImage, VPos.CENTER);
                }
                board[i][j].clearHighlight();
            }
        }
    }

    //Getter
    public Square getSquare(int x, int y) {
        return board[x][y];
    }
}
