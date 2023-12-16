package com.chess.pieces;

import com.chess.Board;
import com.chess.Square;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Queen extends Piece {
    // Quân hậu
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "queen_white.png" : "queen_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public String name() {
        return "Queen";
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

        // Kiểm tra nếu di chuyển theo đường ngang/dọc
        if (currentRow == destRow) {
            // Di chuyển theo chiều ngang
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
            // Di chuyển theo chiều dọc
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

        //Kiểm tra nếu di chuyển theo đường chéo
        if (Math.abs(destRow - currentRow) == Math.abs(destCol - currentCol)) {
            int rowDirection = Integer.compare(destRow, currentRow);
            int colDirection = Integer.compare(destCol, currentCol);

            // Kiểm tra nếu có các quân cờ trên đường đi
            for (int i = 1; i < Math.abs(destRow - currentRow); i++) {
                int checkRow = currentRow + i * rowDirection;
                int checkCol = currentCol + i * colDirection;

                if (board.getSquare(checkCol, checkRow).getPiece() != null) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
