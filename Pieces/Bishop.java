package Pieces;

import Chess.Board;
import PgnAnalyzers.MoveInfo;

public class Bishop extends Piece {
    final private boolean KingSide;
    final private String type;
    public Bishop(String color, boolean KingSide) {
        super(color);
        this.type = "Bishop";
        this.KingSide = KingSide;
    }
    public boolean isKingSide() {
        return this.KingSide;
    }

    public String getType() {
        return type;
    }

    @Override
    public String draw(){
        return (!this.getColor().equals("white")) ? "♗ " : "♝ "; // Bishop
    }
    @Override
    public boolean isValidMove(Board board, int row, int col, MoveInfo move, boolean isWhite, String enPassant_able) {
        System.out.println(row + " " + col + "\n");

        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        // check how many squares moved horizontally and vertically
        int dx = Math.abs(destCol - col);
        int dy = Math.abs(destRow - row);
        if(dx == 0 || dy == 0) return false; //did no move

        int stepRow = (destRow - row) / dx;
        int stepCol = (destCol - col) / dx;
        for (int i = 1; i < dx; i++) {
            if (board.getPiece(row + i * stepRow,col + i * stepCol) != null) return false;
        }

        return dx == dy;

    }
}

