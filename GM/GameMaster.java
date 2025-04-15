package GM;
import Chess.Board;
import PgnAnalyzers.MoveInfo;
import Pieces.*;
import Exceptions.InvalidMoveException;

public class GameMaster {
    final private String[] game;
    private Board board;
    private boolean[] whiteCastling = {false,false,false};
    private boolean[] blackCastling = {false,false,false};
    private String enPassant_able;
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
            this.board.drawBoard();

        }
        return true;
    }

    public void updateCastlings(int row, int col,boolean isWhite,char piece){
        if (piece == 'R') {
            if (!isWhite && row == 6 && col == 0) {
                this.blackCastling[0] = true;
            }
            if (!isWhite && row == 6 && col == 7) {
                this.blackCastling[1] = true;
            }
            if (isWhite && row == 0 && col == 0) {
                this.whiteCastling[0] = true;
            }
            if (isWhite && row == 0 && col == 7) {
                this.whiteCastling[1] = true;
            }
        }

        if (piece == 'K') {
            if(row == 6 && col == 4){
                this.blackCastling[2] = true;
            }
            if(row == 0 && col == 4){
                this.whiteCastling[2] = true;
            }

        }

    }
    public boolean checkValidity(Board board, MoveInfo move, int row, int col, boolean isWhite) {
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        if(board.getPiece(row,col) == null) return false;
        if(move.capture){
            if(board.getPiece(destRow,destCol) == null && !move.enPassant){
                System.out.println("Can not capture empty square from xx xx");
                return false;
                //capture unavailable since square is empty
            }
            if(board.getPiece(destRow,destCol) != null){

                if(board.getPiece(destRow,destCol).getColor().equals("black") && !isWhite){
                    System.out.println("Can not capture your own piece");
                    return false;
                }
                if(board.getPiece(destRow, destCol).getColor().equals("white") && isWhite){
                    System.out.println("Can not capture your own piece");
                    return false;
                };


            }
            return true;
        }else{
            //there should not be anything on square, since nothing was captured by pgn
//            System.out.println("can not move on occupied square without capturing");
            return board.getPiece(destRow, destCol) == null;
        }
    }

    public boolean analyzeMove(MoveInfo move,boolean isWhite) throws InvalidMoveException {

        String color = (isWhite)? "white" : "black";
        if (move.castlingQueen || move.castlingKing) {
            int Row = isWhite ? 0 : 7;
            boolean[] checkCastlingMoves = (isWhite)? this.whiteCastling : this.blackCastling;

            if (isChecked(board,Row,4,isWhite,true)) {
                System.out.println("can not do castling while checked");
                return false;
            }

            boolean castle = canCastle(this.board,checkCastlingMoves,color, move.castlingKing, Row);
            if(!castle) {
                System.out.println("Unable to castle");
                return false;
            };
            if(move.castlingQueen){
                Piece king = this.board.getPiece(Row,4);
                Piece rook = this.board.getPiece(Row,0);
                this.board.setBoard(Row,0 ,null);
                this.board.setBoard(Row,4,null);
                this.board.setBoard(Row,3,rook);
                this.board.setBoard(Row,2,king);
                if(isWhite){
                    this.whiteCastling[0] = true;
                    this.whiteCastling[2] = true;
                }else{
                    this.blackCastling[0] = true;
                    this.blackCastling[2] = true;
                }

                if(isChecked(this.board, Row, 2, isWhite, true)) {
                    throw new InvalidMoveException("Can not move castle at checked location");
                }
            }else{
//                System.out.println("sadfasfsa");
                Piece king = this.board.getPiece(Row,4);
                Piece rook = this.board.getPiece(Row,7);
                this.board.setBoard(Row,4 ,null);
                this.board.setBoard(Row,7,null);
                this.board.setBoard(Row,5,rook);
                this.board.setBoard(Row,6,king);
                if(isWhite){
                    this.whiteCastling[1] = true;
                    this.whiteCastling[2] = true;
                }else{
                    this.blackCastling[1] = true;
                    this.blackCastling[2] = true;
                }
                if(isChecked(board, Row, 6, isWhite, true)){
                    throw new InvalidMoveException("Can not move castle at checked location");

                }
            }
            return true;
//              can not make move since it gets checked
        }
        if(this.enPassant_able != null && !move.enPassant){
            this.enPassant_able = null;
        }
        int destRow = move.destination.charAt(1) - '1';
        int destCol = move.destination.charAt(0) - 'a';
        char piece = move.piece.charAt(0);
        if (move.location.isEmpty()) {
            Piece[][] tempBoard = this.board.getBoard();
            boolean foundValid = false;
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Piece temp = tempBoard[row][col];
                    if (temp == null) continue;
                    if(!temp.getColor().equals(color)) continue;
                    if ( temp.getType().charAt(0) == piece) {
                        if(!checkValidity(board, move, row, col, isWhite)) continue;
                        if (temp.isValidMove(board, row, col, move, isWhite,this.enPassant_able)) {
                            System.out.println("can not do castling while checked");
                            this.board.setBoard(row, col, null);
                            this.board.setBoard(destRow, destCol, temp);
                            foundValid = true;
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
                        }
                    }

                }
            }
            if (!foundValid) {

                System.out.println("Piece is ded " + move.piece);
                return false;
            }
        }else if (move.location.length() == 2) {
            int col = move.location.charAt(0) - 'a';
            int row = move.location.charAt(1) - '1';
            Piece temp = board.getPiece(row,col);
            if(!temp.getColor().equals(color)) return false;
            if (temp.isValidMove(board, row, col, move, isWhite,this.enPassant_able)) {
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
            } else {
                System.out.println("Invalid move");
                return false;
            }
        }else {
            Piece[][] tempBoard = this.board.getBoard();
            char a = move.location.charAt(0);
            if (Character.isDigit(a)) {
                boolean foundValid = false;
                int row = a - '1';
                for (int col = 0; col < 8; col++) {
                    Piece temp = tempBoard[row][col];
                    if (temp == null) continue;
                    if(!temp.getColor().equals(color)) continue;
                    if (temp.getType().charAt(0) == piece) {
                        if(!checkValidity(board,move,row,col,isWhite)) continue;
                        if (temp.isValidMove(board, row, col, move, isWhite,this.enPassant_able)) {

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
                        }
                    }
                }
                if (!foundValid) {

                    System.out.println("Piece is ded " + move.piece);
                    return false;
                }
            } else {
                boolean foundValid = false;
                int col = a - 'a';
                for (int row = 0; row < 8; row++) {
                    Piece temp = tempBoard[row][col];
                    if (temp == null) continue;
                    if(!temp.getColor().equals(color)) continue;
                    if (temp.getType().charAt(0) == piece) {
                        if(!checkValidity(board,move,row,col,isWhite)) continue;
                        if (temp.isValidMove(board, row, col, move, isWhite,this.enPassant_able)) {
                            this.board.setBoard(row, col, null);
                            this.board.setBoard(destRow, destCol, temp);
                            if(move.piece.charAt(0) == 'R' || move.piece.charAt(0) == 'K'){
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
                            foundValid = true;
                        }
                    }
                }
                if (!foundValid) {

                    System.out.println("Piece is de1 " + move.piece);
                    return false;
                }
            }
        }
        if(isChecked(board,0,0 ,isWhite,false)) return false;
        return true;
    }

    public static boolean isChecked(Board board, int row, int col, boolean isWhite, boolean kingLoc) {
        String color = isWhite ? "white" : "black";
        Piece kingPiece = new King(color);
        int kingRow = 0;
        int kingCol = 0;
        // find king
        if(!kingLoc){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece piece = board.getPiece(i,j);
                    if(piece == null) continue;
                    if(piece.getType().equals("King") && piece.getColor().equals(color)){
                        kingRow = i;
                        kingCol = j;
                    }
                }
            }
        }else{
            kingRow = row;
            kingCol = col;
        }
        //
        return isPinned(board,kingRow,kingCol,isWhite);
    }
    public static boolean isCheckMate(Board board, int row, int col, MoveInfo move, boolean isWhite) {
        if(!isChecked(board,row,col,isWhite,true)) return false;

        int[][] moves = {{1,-1},{1,0},{1,1},{0,-1},{0,+1},{-1,-1},{-1,0},{-1,1}};
        for (int[] dir : moves){
            int rdir = row + dir[0];
            int cdir = col + dir[1];
            if(rdir >= 0 && rdir < 8 && cdir >= 0 && cdir < 8){
                if(!isChecked(board,rdir,cdir,isWhite,true)) return false;
            }
        }
        return true;
    }
    public static boolean isPinned(Board board, int row, int col, boolean isWhite) {
        String enemyColor = (isWhite) ? "black" : "white";

        /* check if king is threatened by either rook or queen vertically or horizontally */
        // vertical
        for (int i = row-1; i >= 0; i--) {
            Piece pieceRow = board.getPiece(i,col);
            if(pieceRow == null) continue;
            if (!pieceRow.getType().equals("Queen") && !pieceRow.getType().equals("Rook")) break;
            if ((pieceRow.getType().equals("Queen") || pieceRow.getType().equals("Rook")) && !pieceRow.getColor().equals(enemyColor)) break;
            return true;
        }
        for (int i = row+1; i < 8; i++) {
            Piece pieceRow = board.getPiece(i,col);
            if(pieceRow == null) continue;
            if (!pieceRow.getType().equals("Queen")  && !pieceRow.getType().equals("Rook")) break;
            if ((pieceRow.getType().equals("Queen") || pieceRow.getType().equals("Rook")) && !pieceRow.getColor().equals(enemyColor)) break;
            return true;
        }
        // horizontal
        for (int i = col-1; i >= 0; i--) {
            Piece pieceCol = board.getPiece(row,i);
            if(pieceCol == null) continue;
            if (!pieceCol.getType().equals("Queen")  && !pieceCol.getType().equals("Rook")) break;
            if ((pieceCol.getType().equals("Queen") || pieceCol.getType().equals("Rook")) && !pieceCol.getColor().equals(enemyColor)) break;
            return true;
        }
        for (int i = col+1; i < 8; i++) {
            Piece pieceCol = board.getPiece(row,i);
            if(pieceCol == null) continue;
            if (!pieceCol.getType().equals("Queen")  && !pieceCol.getType().equals("Rook")) break;
            if ((pieceCol.getType().equals("Queen") || pieceCol.getType().equals("Rook")) && !pieceCol.getColor().equals(enemyColor)) break;
            return true;
        }

        /*check if king is threatened by either bishop or queen diagonally */
        int[][] movesDiag = {{-1,-1},{-1,1},{1,1},{1,-1}};
        for (int[] dir : movesDiag) {
            int k = dir[0];
            int j = dir[1];

            int rows = row;
            int cols = col;

            while (rows >= 0 && rows < 8 && cols >= 0 && cols < 8) {
                Piece piece = board.getPiece(rows, cols);
                if(piece == null) {
                    rows += k;
                    cols += j;
                    continue;
                }

                if((isWhite && piece.getColor().equals("white")) || (!isWhite && piece.getColor().equals("black"))) break;
                if (!piece.getType().equals("Queen") && !piece.getType().equals("Bishop")) break;

                return true;

            }
        }

        /* check if king is threatened by a pawn */
        int pawnDirection = (isWhite) ? 1 : -1;
        for (int i = 0; i < 2; i++){
            int moveRow = row + pawnDirection;
            int moveCol = col  + (i % 2 == 0 ? -1 : 1);
            if(moveRow >=0 && moveRow < 8 && moveCol >= 0 && moveCol < 8){
                Piece piece = board.getPiece(moveRow,moveCol);
                if (piece != null) {
                    if(isWhite){
                        if(piece.getType().equals("Pawn") && piece.getColor().equals("black")) return true;
                    }else{
                        if(piece.getType().equals("Pawn") && piece.getColor().equals("white")) return true;
                    }
                }
            }
        }

        /* check if king is threatened by knight */
        int[][] moves = {{-2,-1},{-2,+1},{2,1},{2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
        for(int[] move :moves){
            int newRow = row + move[1];
            int newCol = col + move[0];
            if(newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8){
                Piece piece = board.getPiece(newRow,newCol);
                if(piece == null) continue;
                if(piece.getType().equals("Knight")) return true;
            }
        }
        return false;
    }
    public static boolean canCastle(Board board, boolean[] movements, String color, boolean kingSide, int Row) {
        Piece king = board.getPiece(Row,4);
//        System.out.println(king +" " + King);
        if(king == null) return false;
        if(!king.getType().equals("King") && !king.getColor().equals(color)) return false;
        boolean isWhite = king.getColor().equals("white");
        if(kingSide){
            if(movements[1] || movements[2]) return false;
            Rook rook = (Rook) board.getPiece(Row,7);
            if(!rook.isKingSide()) return false;
            if(!rook.getType().equals("Rook") && !rook.getColor().equals(color)) return false;
            for(int i = 5; i < 7; i++){
                if(board.getPiece(Row,i) != null) return false;
                if(isChecked(board,Row,i,isWhite,true)) return false;
            }
        }else{
            if(movements[0] || movements[2]) return false;
            Rook rook = (Rook) board.getPiece(Row,1);
            if(rook.isKingSide()) return false;
            if(!rook.getType().equals("Rook") && !rook.getColor().equals(color)) return false;
            for(int i = 1; i < 4; i++){
                if(board.getPiece(Row,i) != null) return false;
                if(isChecked(board,Row,i,isWhite,true)) return false;
            }

        }
        return true;
    }
    public static String enPassant(Board board,int row, int col ,MoveInfo move, boolean isWhite){
        String dest = move.destination;
        int destCol = dest.charAt(0) - 'a';
        int destRow = dest.charAt(1) - '1';
        int dy = Math.abs(destRow - row);
        int dx = Math.abs(destCol - col);

        if(dy == 2) return dest;
        return null;
    }
}

//            if (i % 4 >= 2) {
//                // up diagonally
//                j = 1;
//            } else {
//                //  bellow diagonally
//                j = -1;
//            }
//            if (i % 2 == 0) {
//                //  left diagonally
//                k = 1;
//            } else {
//                //  right diagonally
//                k = -1;
//            }