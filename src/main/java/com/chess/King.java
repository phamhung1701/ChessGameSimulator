package com.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {
    //Quân vua
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        return false;
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
    public void name() {
        System.out.println("King");
    }
}
