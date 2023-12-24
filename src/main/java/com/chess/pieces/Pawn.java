package com.chess.pieces;

import com.chess.Board;
import com.chess.Square;
import com.chess.movement.PawnMove;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends Piece {
    // Quân tốt
    private boolean movedFirstTwo;
    private boolean enPassantPossibleLeft;
    private boolean enPassantPossibleRight;

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
    public String name() {
        return "Pawn";
    }

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        //Không thể di chuyển vào các ô có quân cùng màu
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int direction = isWhite() ? 1 : -1;
        // En passant
        if (enPassantPossibleLeft
            && board.getLastMove() instanceof PawnMove
            && ((PawnMove) board.getLastMove()).isEnPassantPossible()) {
            if ((start.getColumn() - 1 == end.getColumn()
                    && start.getRow() - direction == end.getRow())) {
                // En passant capture
                return true;
            }
        }
        if (enPassantPossibleRight
            && board.getLastMove() instanceof PawnMove
            && ((PawnMove) board.getLastMove()).isEnPassantPossible()) {
            if ((start.getColumn() + 1 == end.getColumn()
                    && start.getRow() - direction == end.getRow())) {
                // En passant capture
                return true;
            }
        }

        // Các bước đi cơ bản
        if (this.isWhite() && start.getRow() == 6 && end.getPiece() == null
            && board.getSquare(start.getColumn(), 5).getPiece() == null) {
            // Bước đi đầu tiên của quân tốt trắng
            movedFirstTwo = true;
            return end.getRow() + 2 >= start.getRow()
                    && start.getColumn() == end.getColumn()
                    && end.getRow() <= start.getRow();
        } else if (this.isWhite() && end.getPiece() != null
                && end.getPiece().isWhite() != start.getPiece().isWhite()) {
            // Nước ăn chéo của quân tốt trắng
            movedFirstTwo = false;
            return end.getRow() + 1 == start.getRow()
                    && (start.getColumn() + 1 == end.getColumn()
                    || start.getColumn() - 1 == end.getColumn());
        } else if (this.isWhite() && end.getPiece() == null){
            // Nước tiến một bước của quân trắng
            movedFirstTwo = false;
            return end.getRow() + 1 == start.getRow() && start.getColumn() == end.getColumn();
        } else if (!this.isWhite() && start.getRow() == 1 && end.getPiece() == null
            && board.getSquare(start.getColumn(), 2).getPiece() == null) {
            // Bước đi đầu tiên của quân tốt đen
            movedFirstTwo = true;
            return end.getRow() - 2 <= start.getRow()
                    && start.getColumn() == end.getColumn()
                    && end.getRow() >= start.getRow();
        } else if (!this.isWhite() && end.getPiece() != null
                && end.getPiece().isWhite() != start.getPiece().isWhite()) {
            // Nước ăn chéo của quân tốt đen
            movedFirstTwo = false;
            return end.getRow() - 1 == start.getRow()
                    && (start.getColumn() + 1 == end.getColumn()
                    || start.getColumn() - 1 == end.getColumn());
        } else if (!this.isWhite() && end.getPiece() == null){
            // Nước tiến một bước của quân đen
            movedFirstTwo = false;
            return end.getRow() - 1 == start.getRow() && start.getColumn() == end.getColumn();
        }
        return false;
    }

    public boolean isEnPassantPossibleLeft() {
        return enPassantPossibleLeft;
    }

    public void setEnPassantPossibleLeft(boolean enPassantPossibleLeft) {
        this.enPassantPossibleLeft = enPassantPossibleLeft;
    }

    public boolean isEnPassantPossibleRight() {
        return enPassantPossibleRight;
    }

    public void setEnPassantPossibleRight(boolean enPassantPossibleRight) {
        this.enPassantPossibleRight = enPassantPossibleRight;
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

    public boolean isMovedFirstTwo() {
        return movedFirstTwo;
    }

    public void setMovedFirstTwo(boolean movedFirstTwo) {
        this.movedFirstTwo = movedFirstTwo;
    }

    @Override
    public Piece create(boolean isWhite) {
        return new Pawn(isWhite);
    }
}
