package Pieces;

import Chess.Board;
import GM.GameMaster;
import PgnAnalyzers.MoveInfo;

public class King extends Piece {
    final private String type;
    public King(String color) {
        super(color);
        this.type = "King";
    }

    public String getType() {
        return type;
    }

    @Override
    public String draw(){
        return (!this.getColor().equals("white")) ? "♔ " : "♚ "; // King
    }
    @Override
    public boolean isValidMove(Board board, int row, int col, MoveInfo move, boolean isWhite, String enPassant_able) {
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        // check how many squares moved horizontally and vertically
        int dy = Math.abs(destRow - row);
        int dx = Math.abs(destCol - col);
        if(GameMaster.isChecked(board,destRow,destCol ,isWhite,true)) return false;

        return dy <= 1 && dx <= 1;
    }
//
//
}
