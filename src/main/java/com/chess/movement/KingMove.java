package com.chess.movement;

import com.chess.Square;
import com.chess.pieces.King;
import com.chess.pieces.Piece;

public class KingMove extends Move {
    private boolean isCastlingMove;

    public KingMove(Square start, Square end, Piece movedPiece, Piece capturedPiece) {
        super(start, end, movedPiece, capturedPiece);
    }

    public KingMove(Move move) {
        super(move.getStart(),
                move.getEnd(),
                move.getMovedPiece(),
                move.getCapturedPiece());
    }

    public boolean isCastlingMove() {
        return isCastlingMove;
    }

    public void setCastlingMove(boolean castlingMove) {
        isCastlingMove = castlingMove;
    }
}
