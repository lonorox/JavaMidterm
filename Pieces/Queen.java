package Pieces;

import Chess.Board;
import PgnAnalyzers.MoveInfo;

public class Queen extends Piece {
    final private String type;
    public Queen(String color) {
        super(color);
        this.type = "Queen";
    }
    public String getType() {
        return type;
    }

    @Override
    public String draw(){
        return (!this.getColor().equals("white")) ? "♕ " : "♛ "; // Queen
    }
    @Override
    public  boolean isValidMove(Board board, int row, int col, MoveInfo move, boolean isWhite, String enPassant_able) {
        return new Rook(this.getColor(),false).isValidMove(board, row, col, move, isWhite,"")
                || new Bishop(this.getColor(),false).isValidMove(board, row, col, move, isWhite,"");
    }
}


