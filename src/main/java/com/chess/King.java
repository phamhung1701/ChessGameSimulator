package com.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {
    // Quân Vua
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "king_white.png" : "king_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public String name() {
        return "King";
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

        // Di chuyển một ô theo chiều ngang hoặc dọc
        return Math.abs(destRow - currentRow) <= 1 && Math.abs(destCol - currentCol) <= 1;
    }
}
