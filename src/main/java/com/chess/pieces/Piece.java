package com.chess.pieces;

import com.chess.Board;
import com.chess.Square;
import javafx.scene.image.ImageView;

public abstract class Piece {
    //Lớp trừu tượng biểu diễn một quân cờ
    private boolean isWhite; // Kiểm tra có là quân trắng hay không

    //Hàm dựng
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    //Getter & Setter
    public boolean isWhite() {
        return isWhite;
    }
    public void setWhite(boolean white) {
        isWhite = white;
    }

    //Hàm trừu tượng kiểm tra quân cờ ở ô hiện tại có thể di chuyển đến ô được chọn
    public abstract boolean canMove(Board board, Square start, Square end);

    //Trả về hình ảnh của quân cờ
    public abstract ImageView image();

    //In ra tên của quân cờ
    public abstract String name();

    public abstract char getSymbol();
}
