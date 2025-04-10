package Pieces;

public class Pawn extends Piece {
    final private int id;
    public Pawn(String color, int id) {
        super(color);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    
    @Override
    public boolean isValidMove(String start, String end, Piece[][] board) {
        return true;
    }
}
