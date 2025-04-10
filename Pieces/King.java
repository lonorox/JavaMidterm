package Pieces;

public class King extends Piece {
    public King(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(String start, String end,Piece[][] board) {
        return true;
    }
}
