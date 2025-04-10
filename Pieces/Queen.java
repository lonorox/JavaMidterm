package Pieces;

public class Queen extends Piece {
    public Queen(String color) {
        super(color);
    }
    @Override
    public boolean isValidMove(String start, String end,Piece[][] board) {
        return true;
    }
}
