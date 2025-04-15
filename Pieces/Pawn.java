package Pieces;
import Chess.Board;
import PgnAnalyzers.MoveInfo;
public class Pawn extends Piece {
    final private int id;
    final private String type;

    public Pawn(String color, int id) {
        super(color);
        this.id = id;
        this.type = "Pawn";
    }
    public String getType() {
        return type;
    }

    public boolean isKingSide() {
        return false;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String draw(){
        return (!this.getColor().equals("white")) ? "♙ " : "♟ "; // Pawn
    }
    @Override
    public boolean isValidMove(Board board, int row, int col , MoveInfo move, boolean isWhite, String enPassant_able) {
        String dest = move.destination;
        int destCol = dest.charAt(0) - 'a';
        int destRow = dest.charAt(1) - '1';
        int dy = Math.abs(destRow - row);
        int dx = Math.abs(destCol - col);
        if(!move.capture && col != destCol) {
            return false;
        }
        if (move.enPassant){
            if(enPassant_able == null) return false;
            // none passantable
//
            int passantCol = enPassant_able.charAt(0) - 'a';
            int passantRow = enPassant_able.charAt(1) - '1';
            int pdx = Math.abs(passantCol - col);
            int pdy = Math.abs(passantRow - row);
            return isWhite ? pdy == 0 && pdx == 1 && destRow == row + 1 : pdy == 0 && destRow == row - 1 && pdx == 1;
        }
        if(isWhite) {
            if (move.capture){
//                if square is not empty, check if pawn moves diagonally and if square is in reach
                return dx == 1 && destRow == row + 1;
            }else{
                return (row == 1 && destRow == 3) || (row + 1 == destRow);
            }
        }else{
            if (move.capture){
                return dx == 1 && destRow == row - 1;
            }else{
                return (row == 6 && destRow == 4) || (row - 1 == destRow);
            }

        }
    }
}
