package com.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends Piece {
    //Quân tốt
    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "pawn_white.png" : "pawn_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public void name() {
        System.out.println("Pawn");
    }

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        if (this.isWhite() && start.getRow() == 6 && end.getPiece() == null) {
            // Bước đi đầu tiên của quân tốt trắng
            return end.getRow() + 2 >= start.getRow()
                    && start.getColumn() == end.getColumn()
                    && end.getRow() <= start.getRow();
        } else if (this.isWhite() && end.getPiece() != null
                && end.getPiece().isWhite() != start.getPiece().isWhite()) {
            // Nước ăn chéo của quân tốt trắng
            return end.getRow() + 1 == start.getRow()
                    && (start.getColumn() + 1 == end.getColumn()
                    || start.getColumn() - 1 == end.getColumn());
        } else if (this.isWhite() && end.getPiece() == null){
            // Nước tiến một bước của quân trắng
            return end.getRow() + 1 == start.getRow() && start.getColumn() == end.getColumn();
        } else if (!this.isWhite() && start.getRow() == 1 && end.getPiece() == null) {
            // Bước đi đầu tiên của quân tốt đen
            return end.getRow() - 2 <= start.getRow()
                    && start.getColumn() == end.getColumn()
                    && end.getRow() >= start.getRow();
        } else if (!this.isWhite() && end.getPiece() != null
                && end.getPiece().isWhite() != start.getPiece().isWhite()) {
            // Nước ăn chéo của quân tốt đen
            return end.getRow() - 1 == start.getRow()
                    && (start.getColumn() + 1 == end.getColumn()
                    || start.getColumn() - 1 == end.getColumn());
        } else if (!this.isWhite() && end.getPiece() == null){
            // Nước tiến một bước của quân đen
            return end.getRow() - 1 == start.getRow() && start.getColumn() == end.getColumn();
        }
        return false; // Các trường hợp còn lại không hợp lệ
    }

    public boolean canBePromote(Square currentPosition) {
        // Kiểm tra nếu quân tốt có thể được phong cấp hay không
        if (this.isWhite() && currentPosition.getRow() == 0) {
            // Tốt trắng
            return true;
        } else if (!this.isWhite() && currentPosition.getRow() == 7) {
            // Tốt đen
            return true;
        }
        return false;
    }
}
