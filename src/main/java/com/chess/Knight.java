package com.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Knight extends Piece {
    //Quân mã
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        return false;
    }

    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "knight_white.png" : "knight_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public void name() {
        System.out.println("Knight");
    }
}
