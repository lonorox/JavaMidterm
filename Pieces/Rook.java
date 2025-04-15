package Pieces;

import Chess.Board;
import PgnAnalyzers.MoveInfo;

public class Rook extends Piece {
    final private boolean KingSide;
    final private String type;

    public Rook(String color, boolean KingSide) {
        super(color);
        this.type = "Rook";
        this.KingSide = KingSide;
    }

    public String getType() {
        return type;
    }

    public boolean isKingSide() {
        return KingSide;
    }

    @Override
    public String draw(){
        return (!this.getColor().equals("white")) ?  "♖ " : "♜ "; // Rook
    }


    @Override
    public boolean isValidMove(Board board, int row, int col, MoveInfo move, boolean isWhite, String enPassant_able) {
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';

        if (row != destRow && col != destCol) return false;

        // check move direction
        int dy = Integer.signum(destRow - row);
        int dx = Integer.signum(destCol - col);

        int r = row + dy, c = col + dx;
        while (r != destRow || c != destCol) {
            if (board.getPiece(r,c) != null) return false;
            r += dy;
            c += dx;
        }
        if(move.capture){
            // nothing to capture
            return board.getPiece(destRow,destCol) != null;
        }

        return true;
    }
}

