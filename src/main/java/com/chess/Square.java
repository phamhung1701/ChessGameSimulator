package com.chess;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square {
    // Lớp biểu diễn tọa độ của quân cờ (ô)
    private int row; // Hàng ngang
    private int column; // Hàng dọc
    private Rectangle rectangle; // Chủ yếu biểu diễn màu của một ô
    private Piece piece; // Quân cờ ở trên ô hiện tại

    // Hàm dựng
    public Square(int row, int column, Piece piece) {
        this.row = row;
        this.column = column;
        this.piece = piece;
        this.rectangle = new Rectangle(50 , 50);

        // Đặt các màu mặc định cho các ô
        clearHighlight();
    }

    // Di chuyển quân cờ ở ô hiện tại đến ô destination và trả về quân cờ đã bị ăn
    public Piece movePieceTo(Square destination) {
        Piece capturedPiece = null;
        if (destination.getPiece() != null) {
            capturedPiece = destination.getPiece();
        }
        destination.setPiece(this.piece);
        this.setPiece(null);
        return capturedPiece;
    }

    // Getter & Setter
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    // Hiển thị các nước đi có thể đi được của quân được chọn
    public void highlight() {
        if (this.piece == null) {
            rectangle.setFill(Color.LIGHTGREEN);
        } else {
            rectangle.setFill(Color.RED);
        }
    }

    // Hiển thị ô hiện tại thành màu vàng nhạt
    public void highlightSelectedPiece() {
        rectangle.setFill(Color.YELLOW);
    }


    // Đặt lại màu của ô
    public void clearHighlight() {
        if ((row + column) % 2 == 0) {
            rectangle.setFill(Color.BEIGE);
        } else {
            rectangle.setFill(Color.GRAY);
        }
    }
}
