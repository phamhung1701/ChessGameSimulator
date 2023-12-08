package com.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Queen extends Piece {
    //Quân hậu
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean canMove(Board board, Square start, Square end) {
        return false;
    }

    @Override
    public ImageView image() {
        String imagePath = this.isWhite() ? "queen_white.png" : "queen_black.png";
        ImageView image = new ImageView(new Image(imagePath));
        double size = 30;
        image.setFitWidth(size);
        image.setFitHeight(size);
        return image;
    }

    @Override
    public void name() {
        System.out.println("Queen");
    }
}
