package com.chess;

public class Move {
    private Square start;
    private Square end;
    private Piece movedPiece;
    private Piece capturedPiece;

    public Move(Square start, Square end, Piece movedPiece, Piece capturedPiece) {
        this.start = start;
        this.end = end;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
    }

    public Square getStart() {
        return start;
    }

    public void setStart(Square start) {
        this.start = start;
    }

    public Square getEnd() {
        return end;
    }

    public void setEnd(Square end) {
        this.end = end;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public void describeMove() {
        String capturedPiece = getCapturedPiece() != null ? getCapturedPiece().name() : "none";
        System.out.println();
    }
}
