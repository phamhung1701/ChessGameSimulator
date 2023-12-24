package com.chess.pieces;

import com.chess.Board;
import com.chess.Square;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {
    // Quân Vua
    private int timesMoved = 0;
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
    public char getSymbol() {
        return 'K';
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

        // Check for castling
        if (timesMoved == 0 && destRow == currentRow && Math.abs(destCol - currentCol) == 2) {

            // King-side castling
            if (destCol > currentCol) {

                Square rookSquare = board.getSquare(7, currentRow);
                if (rookSquare != null && rookSquare.getPiece() instanceof Rook
                        && ((Rook) rookSquare.getPiece()).getTimesMoved() == 0) {

                    // Check if the squares between the king and rook are empty
                    if (board.getSquare(currentCol + 1, currentRow).getPiece() == null
                            && board.getSquare(currentCol + 2, currentRow).getPiece() == null) {
                        return true;
                    }
                }
            }
            // Queen-side castling
            else if (destCol < currentCol) {

                Square rookSquare = board.getSquare(0, currentRow);
                if (rookSquare != null && rookSquare.getPiece() instanceof Rook
                        && ((Rook) rookSquare.getPiece()).getTimesMoved() == 0) {

                    // Check if the squares between the king and rook are empty
                    if (board.getSquare(currentCol - 1, currentRow).getPiece() == null
                            && board.getSquare(currentCol - 2, currentRow).getPiece() == null
                            && board.getSquare(currentCol - 3, currentRow).getPiece() == null) {
                        return true;
                    }
                }
            }
        }

        // Di chuyển một ô theo chiều ngang hoặc dọc
        return Math.abs(destRow - currentRow) <= 1 && Math.abs(destCol - currentCol) <= 1;
    }

    public int getTimesMoved() {
        return timesMoved;
    }

    public void setTimesMoved(int timesMoved) {
        this.timesMoved = timesMoved;
    }

    public void moved() {
        timesMoved++;
    }

    public void undo() {
        timesMoved--;
    }

    @Override
    public Piece create(boolean isWhite) {
        return new King(isWhite);
    }
}
