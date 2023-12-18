package com.chess.movement;

import com.chess.Square;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;

public class PawnMove extends Move {
    private boolean enPassantPossible;
    private boolean isEnPassantMove;
    private boolean isLeftEnPassant;
    private boolean isFirst2Move;
    private boolean isPromotionMove;
    public PawnMove(Square start, Square end, Piece movedPiece, Piece capturedPiece) {
        super(start, end, movedPiece, capturedPiece);
    }

    public PawnMove(Move move) {
        super(move.getStart(),
                move.getEnd(),
                move.getMovedPiece(),
                move.getCapturedPiece());
    }

    public boolean isEnPassantMove() {
        return isEnPassantMove;
    }

    public void setEnPassantMove(boolean enPassantMove) {
        isEnPassantMove = enPassantMove;
    }

    public boolean isLeftEnPassant() {
        return isLeftEnPassant;
    }

    public void setLeftEnPassant(boolean leftEnPassant) {
        isLeftEnPassant = leftEnPassant;
    }

    public boolean isFirst2Move() {
        return isFirst2Move;
    }

    public void setFirst2Move(boolean first2Move) {
        isFirst2Move = first2Move;
    }

    public boolean isEnPassantPossible() {
        return enPassantPossible;
    }

    public void setEnPassantPossible(boolean enPassantPossible) {
        this.enPassantPossible = enPassantPossible;
    }

    public boolean isPromotionMove() {
        return isPromotionMove;
    }

    public void setPromotionMove(boolean promotionMove) {
        isPromotionMove = promotionMove;
    }
}
