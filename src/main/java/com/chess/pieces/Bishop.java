package com.chess.pieces;

import com.chess.Board;
import com.chess.Square;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bishop extends Piece {
    //Quân tượng
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "bishop_white.png" : "bishop_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public String name() {
        return "Bishop";
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

        // Kiểm tra nêú đường đi là đường chéo
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
