package GM;
import Chess.Board;
import Exceptions.ErrorLogger;
import Exceptions.ValidationResult;
import PgnAnalyzers.MoveInfo;
import Pieces.*;
import Exceptions.InvalidMoveException;

import java.util.ArrayList;
import java.util.List;

import static GM.moveValidators.*;

public class GameMaster {
    final private String[] game;
    private Board board;
    Castling whiteCastling = new Castling(), blackCastling = new Castling() ;
    private String enPassant_able;
    private final moveValidators validator = new moveValidators();
    List<String> moveErrors = new ArrayList<>();

    public void setBoard(Board board){
        this.board = board;
    }
    public GameMaster(String game) {

        if(!game.isEmpty()){
            this.game = game.replaceAll("\\d+\\.", "").substring(1).split("\\s+");
        }else{
            this.game = new String[0];
        }
        this.board = new Board();
    }

    public boolean analyzeGame() {
        System.out.println("Initial Setup \n");
        this.board.drawBoard();
        int turn = 0;
        for (int i = 0; i < this.game.length - 1; i++) {
            System.out.println("\n");
            boolean isWhite = i % 2 == 0;
            turn += ((i+1)%2);
            MoveInfo move = new MoveInfo();
            move.decipherMove(this.game[i]);
            String by = (isWhite)? "White" : "Black";

            System.out.println("\n Move: " + this.game[i] + ", Turn: " +  turn   + ", By: " + by  + " \n");
            try {
                if (!analyzeMove(move,isWhite)) return false;
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
            moveErrors.clear();
            this.board.drawBoard();

        }
        return true;
    }

    public void updateCastlings(int row, int col,boolean isWhite,char piece){
        if (piece == 'R') {
            if (!isWhite && row == 6 && col == 0) {
                blackCastling.setQueenSide(true);
            }
            if (!isWhite && row == 6 && col == 7) {
                blackCastling.setKingSide(true);
            }
            if (isWhite && row == 0 && col == 0) {
                whiteCastling.setQueenSide(true);
            }
            if (isWhite && row == 0 && col == 7) {
                whiteCastling.setKingSide(true);
            }
        }

        if (piece == 'K') {
            if(row == 6 && col == 4){
                blackCastling.setKingMoved(true);
            }
            if(row == 0 && col == 4){
                whiteCastling.setKingMoved(true);
            }

        }

    }

    private boolean findPiece(MoveInfo move, int row, int col,String color, boolean isWhite,int destCol,int destRow) {
        char piece = move.piece.charAt(0);
        Piece[][] tempBoard = this.board.getBoard();
        Piece temp = tempBoard[row][col];
        if (temp == null) return false;
        if (temp.getType().charAt(0) == piece) {
            if(!temp.getColor().equals(color)) return false;
            ValidationResult res = temp.isValidMove(board, row, col, move, isWhite,this.enPassant_able);
            if (res.isValid()) {
                if(!checkValidity(board, move, row, col, isWhite)) return false;
                if(!move.promotion.isEmpty()){
                    if(board.getPiece(destCol, destRow) == null && !move.capture){
                        temp = canPromote(destRow,move,color,piece);
                    }else if(board.getPiece(destCol, destRow) != null && move.capture){
                        temp = canPromote(destRow,move,color,piece);
                    }else{
                        temp = null;
                    }
                    if(temp == null) return false;
                }
                this.board.setBoard(row, col, null);
                this.board.setBoard(destRow, destCol, temp);
                if(piece == 'R' || piece == 'K'){
                    if(row == 0 || row == 7){
                        updateCastlings(row,col,isWhite,move.piece.charAt(0));
                    }
                }
                if(move.piece.charAt(0) == 'P'){
                    if(move.enPassant && this.enPassant_able != null){
                        int passantCol = enPassant_able.charAt(0) - 'a';
                        int passantRow = enPassant_able.charAt(1) - '1';
                        this.board.setBoard(passantRow, passantCol, null);
                    }
                    this.enPassant_able = enPassant(board,row,col,move,isWhite);
                }
                return true;
            }else{
                moveErrors.add(res.getReason());
                return false;
            }
        }
        return false;
    }
    public boolean analyzeMove(MoveInfo move,boolean isWhite) throws InvalidMoveException {

        String color = (isWhite)? "white" : "black";
        if (move.castlingQueen || move.castlingKing) {
            if(handleCastling(isWhite,move,color)){
                return true;
            }else{
//                ErrorLogger.log("Can not castle");
                return false;
            }
        }

        if(this.enPassant_able != null && !move.enPassant){
            this.enPassant_able = null;
        }
        int destRow = move.destination.charAt(1) - '1';
        int destCol = move.destination.charAt(0) - 'a';
        char piece = move.piece.charAt(0);
        boolean foundValid =false;
        if (move.location.isEmpty()) {
            Piece[][] tempBoard = this.board.getBoard();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if(findPiece(move,row,col,color,isWhite,destCol,destRow)) {
                        foundValid = true;
                        break;
                    }
                }
                if (foundValid) break;
            }
            if(!foundValid){
                ErrorLogger.log("No such move for piece found: " + moveErrors.toString());
//                System.out.println("Piece is ded " + move.piece);
                return false;
            }
        }else if (move.location.length() == 2) {
            int col = move.location.charAt(0) - 'a';
            int row = move.location.charAt(1) - '1';
            if(findPiece(move,row,col,color,isWhite,destCol,destRow)) {
                foundValid = true;
            }
            if(!foundValid){
                ErrorLogger.log("No such move for piece found: " + piece + " " + moveErrors.toString());
                return false;
            }

        }else {
            Piece[][] tempBoard = this.board.getBoard();
            char a = move.location.charAt(0);
            int loc;
            if (Character.isDigit(a)) {
                loc = a - '1';
            } else {
                loc = a - 'a';
            }

            for (int i = 0; i < 8; i++) {
                if(Character.isDigit(a)){
                    if(loc == destRow && i == destCol) continue;
                    if(findPiece(move,loc,i,color,isWhite,destCol,destRow)){
                        foundValid = true;
                        break;
                    }
                }else{
                    if(loc == destCol && i == destRow) continue;
                    if(findPiece(move,i,loc,color,isWhite,destCol,destRow)){
                        foundValid = true;
                        break;
                    }
                }
            }
            if(!foundValid){
                ErrorLogger.log("No such move for piece found: " + moveErrors.toString());
                return false;
            }
        }
        return !isChecked(board, 0, 0, isWhite, false);
    }

    private boolean handleCastling(boolean isWhite, MoveInfo move, String color) throws InvalidMoveException{
        int Row = isWhite ? 0 : 7;
//        boolean[] checkCastlingMoves = (isWhite)? this.whiteCastling : this.blackCastling;

        if (isChecked(board,Row,4,isWhite,true)) {
            ErrorLogger.log("can not do castling while checked");
            return false;
        }

        boolean castle = canCastle(this.board,(isWhite)? this.whiteCastling : this.blackCastling,color, move.castlingKing, Row);
        if(!castle) {
            ErrorLogger.log("Unable to castle");
            return false;
        }
        if(move.castlingQueen){
            Piece king = this.board.getPiece(Row,4);
            Piece rook = this.board.getPiece(Row,0);
            if(rook == null || king == null) return false;
            this.board.setBoard(Row,0 ,null);
            this.board.setBoard(Row,4,null);
            this.board.setBoard(Row,3,rook);
            this.board.setBoard(Row,2,king);
            if(isWhite){
                whiteCastling.setQueenSide(true);
                whiteCastling.setKingMoved(true);
            }else{
                blackCastling.setQueenSide(true);
                blackCastling.setKingMoved(true);
            }

            if(isChecked(this.board, Row, 2, isWhite, true)) {
                ErrorLogger.log("Can not move castle at checked location");
                return false;
            }
        }else{
            Piece king = this.board.getPiece(Row,4);
            Piece rook = this.board.getPiece(Row,7);
            if(rook == null || king == null) return false;
            this.board.setBoard(Row,4 ,null);
            this.board.setBoard(Row,7,null);
            this.board.setBoard(Row,5,rook);
            this.board.setBoard(Row,6,king);
            if(isWhite){
                whiteCastling.setKingSide(true);
                whiteCastling.setKingMoved(true);
            }else{
                blackCastling.setKingSide(true);
                blackCastling.setKingMoved(true);
            }
            if(isChecked(board, Row, 6, isWhite, true)){
                ErrorLogger.log("Can not move castle at checked location");
                return false;
            }
        }
        return true;
    }
}
