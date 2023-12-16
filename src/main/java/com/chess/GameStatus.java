package com.chess;

import com.chess.pieces.King;
import com.chess.pieces.Piece;

public class GameStatus {
    private final Board board;

    public GameStatus(Board board) {
        this.board = board;
    }

    public boolean isCheck(boolean isWhite) {
        Square kingSquare = getKing(isWhite);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board.getSquare(i, j);
                Piece piece = square.getPiece();
                if (piece != null
                && piece.isWhite() != isWhite
                && piece.canMove(board, square, kingSquare)) {
                    return true;
                }
            }
        }
        return false;
    } 
    
    // 
    private Square getKing(boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j).getPiece() instanceof King
                && board.getSquare(i, j).getPiece().isWhite() == isWhite) {
                    return board.getSquare(i, j);
                }
            }
        }
        return null;
    }

    public boolean isCheckmate(boolean isWhite) {
        int totalValidMove = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        Square start = board.getSquare(i, j);
                        Square end = board.getSquare(k, l);
                        if (start.getPiece() != null
                                && start.getPiece().isWhite() == isWhite
                                && start.getPiece().canMove(board, start, end)
                                && isMoveValidWithoutCheck(start, end, isWhite)) {
                            totalValidMove++;
                        }
                    }
                }
            }
        }
        return totalValidMove == 0;
    }

    // Kiểm tra nếu nước đi phù hợp để không bị chiếu
    protected boolean isMoveValidWithoutCheck(Square start, Square end, boolean whiteTurn) {
        Piece movedPiece = start.getPiece();
        Piece capturedPiece = end.getPiece();

        // Thực hiện di chuyển tạm thời để kiểm tra
        end.setPiece(movedPiece);
        start.setPiece(null);

        // Kiểm tra nếu sau nước đi tướng không bị chiếu
        boolean isValid = !isCheck(whiteTurn);

        // Đặt lại tình trạng ban đầu
        start.setPiece(movedPiece);
        end.setPiece(capturedPiece);

        return isValid;
    }
}
