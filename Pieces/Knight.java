package Pieces;

public class Knight extends Piece {
    final private boolean KingSide;
    public Knight(String color, boolean KingSide) {
        super(color);
        this.KingSide = KingSide;
    }

    public boolean isKingSide() {
        return KingSide;
    }
    
    @Override
    public boolean isValidMove(String start, String end,Piece[][] board) {
        return true;
    }
}
