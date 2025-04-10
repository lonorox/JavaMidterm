package Pieces;

public abstract class Piece {
    final private String color;

    public Piece(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;     
    }


    public abstract boolean isValidMove(String start, String end, Piece[][] board);
    
}
