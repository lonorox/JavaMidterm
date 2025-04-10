package Pieces;

public class Bishop extends Piece {
    final private boolean KingSide;
    public Bishop(String color, boolean KingSide) {
        super(color);
        this.KingSide = KingSide;
    }

    public boolean isKingSide() {
        return this.KingSide;
    }

    @Override
    public boolean isValidMove(String start, String end,Piece[][] board) {
        return true;
    }
}
