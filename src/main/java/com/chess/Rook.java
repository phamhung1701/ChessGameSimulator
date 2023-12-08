package com.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Rook extends Piece {
    //Quân xe
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "rook_white.png" : "rook_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public void name() {
        System.out.println("Rook");
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        //Không thể di chuyển vào các ô có quân cùng màu
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }


        int currentRow = start.getRow();
        int currentCol = start.getColumn();
        int destRow = end.getRow();
        int destCol = end.getColumn();


        if (currentRow == destRow) {
            //Di chuyển theo chiều ngang
            if (currentCol < destCol) {
                for (int i = currentCol + 1; i < destCol; i++) {
                    if (board.getSquare(i, currentRow).getPiece() != null) {
                        return false;
                    }
                }
            } else {
                for (int i = destCol + 1; i < currentCol; i++) {
                    if (board.getSquare(i, currentRow).getPiece() != null) {
                        return false;
                    }
                }
            }
            return true;
        } else if (currentCol == destCol) {
            //Di chuyển theo chiều dọc
            if (currentRow < destRow) {
                for (int i = currentRow + 1; i < destRow; i++) {
                    if (board.getSquare(currentCol, i).getPiece() != null) {
                        return false;
                    }
                }
            } else {
                for (int i = destRow + 1; i < currentRow; i++) {
                    if (board.getSquare(currentCol, i).getPiece() != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
