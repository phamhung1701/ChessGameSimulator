package com.chess.pieces;

import com.chess.Board;
import com.chess.Square;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Knight extends Piece {
    // Quân mã
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "knight_white.png" : "knight_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public String name() {
        return "Knight";
    }

    @Override
    public char getSymbol() {
        return 'N';
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        // Không thể di chuyển vào các ô có quân cùng màu
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int currentRow = start.getRow();
        int currentCol = start.getColumn();
        int destRow = end.getRow();
        int destCol = end.getColumn();

        // Kiểm tra các bước di chuyển hợp lệ của quân mã
        int rowDiff = Math.abs(destRow - currentRow);
        int colDiff = Math.abs(destCol - currentCol);

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    @Override
    public Piece create(boolean isWhite) {
        return new Knight(isWhite);
    }
}
