
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
//        MoveInfo move = new MoveInfo();
//        move.decipherMove("Nf6");
//        board.drawBoard();
//        int destRow = move.destination.charAt(1) - '1';
//        int destCol = move.destination.charAt(0) - 'a';
//        int piece = PieceMapper.getValueBySymbol('N');
//        if (Pieces.isValidMove(board, piece ,7, 6, move, false)) {
//            System.out.println("Move detected");
//            this.board.setBoard(7, 6, 0);
//            this.board.setBoard(destRow, destCol, 5);
//        }
//        System.out.println();
//        board.drawBoard();
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
            if (!analyzeMove(move,isWhite)) return false;
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
    public boolean analyzeMove(MoveInfo move,boolean isWhite) {
        if (move.castlingQueen || move.castlingKing) {
            int King = isWhite ? -2 : 2;
            int Rook = isWhite ? -6 : 6;
            int Row = isWhite ? 0 : 7;

            boolean[] checkCastlingMoves = (isWhite)? this.whiteCastling : this.blackCastling;
            boolean castle = Pieces.canCastle(this.board,checkCastlingMoves,isWhite, move.castlingKing, King, Rook, Row);
            if(!castle) return false;
            if(move.castlingQueen){
                board.setBoard(Row,0 ,0);
                board.setBoard(Row,4,0);
                board.setBoard(Row,3,Rook);
                board.setBoard(Row,2,King);
                if(isWhite){
                    this.whiteCastling[0] = true;
                    this.whiteCastling[2] = true;
                }else{
                    this.blackCastling[0] = true;
                    this.blackCastling[2] = true;
                }
                return !Pieces.isChecked(board, Row, 2, isWhite, true);
            }else{
                board.setBoard(Row,4 ,0);
                board.setBoard(Row,7,0);
                board.setBoard(Row,5,Rook);
                board.setBoard(Row,6,King);
                if(isWhite){
                    this.whiteCastling[1] = true;
                    this.whiteCastling[2] = true;
                }else{
                    this.blackCastling[1] = true;
                    this.blackCastling[2] = true;
                }
                return !Pieces.isChecked(board, Row, 6, isWhite, true);
            }
//              can not make move since it gets checked
        }
        if(this.enPassant_able != null && !move.enPassant){
            this.enPassant_able = null;
        }

        int pieceFromChar = PieceMapper.getValueBySymbol(move.piece.charAt(0));
        int destRow = move.destination.charAt(1) - '1';
        int destCol = move.destination.charAt(0) - 'a';
        int piece = isWhite ? -1 * pieceFromChar : pieceFromChar;
        if (move.location.isEmpty()) {
            int[][] tempBoard = this.board.getBoard();
            boolean foundValid = false;
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (tempBoard[row][col] == piece) {
                        if (Pieces.isValidMove(board, Math.abs(piece), row, col, move, isWhite,this.enPassant_able)) {
                            this.board.setBoard(row, col, 0);
                            this.board.setBoard(destRow, destCol, piece);
                            foundValid = true;
                            if(move.piece.charAt(0) == 'R' || move.piece.charAt(0) == 'K'){
                                if(row == 0 || row == 7){
                                    updateCastlings(row,col,isWhite,move.piece.charAt(0));
                                }
                            }
                            if(move.piece.charAt(0) == 'P'){
                                if(move.enPassant && this.enPassant_able != null){
                                    int passantCol = enPassant_able.charAt(0) - 'a';
                                    int passantRow = enPassant_able.charAt(1) - '1';
                                    this.board.setBoard(passantRow, passantCol, 0);
                                }
                                this.enPassant_able = Pieces.enPassant(board,row,col,move,isWhite);


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
            if (Pieces.isValidMove(board, Math.abs(piece), row, col, move, isWhite,this.enPassant_able)) {
                this.board.setBoard(row, col, 0);
                this.board.setBoard(destRow, destCol, piece);
                if(move.piece.charAt(0) == 'R' || move.piece.charAt(0) == 'K'){
                    if(row == 0 || row == 7){
                        updateCastlings(row,col,isWhite,move.piece.charAt(0));
                    }
                }
                if(move.piece.charAt(0) == 'P'){
                    if(move.enPassant && this.enPassant_able != null){
                        int passantCol = enPassant_able.charAt(0) - 'a';
                        int passantRow = enPassant_able.charAt(1) - '1';
                        this.board.setBoard(passantRow, passantCol, 0);
                    }
                    this.enPassant_able = Pieces.enPassant(board,row,col,move,isWhite);
                }
            } else {
                System.out.println("Invalid move");
                return false;
            }
        }else {
            int[][] tempBoard = this.board.getBoard();
            char a = move.location.charAt(0);
            if (Character.isDigit(a)) {
                boolean foundValid = false;
                int row = a - '1';
                for (int col = 0; col < 8; col++) {
                    if (tempBoard[row][col] == piece) {
                        if (Pieces.isValidMove(board, Math.abs(piece), row, col, move, isWhite,this.enPassant_able)) {

                            this.board.setBoard(row, col, 0);
                            this.board.setBoard(destRow, destCol, piece);
                            if(move.piece.charAt(0) == 'R' || move.piece.charAt(0) == 'K'){
                                if(row == 0 || row == 7){
                                    updateCastlings(row,col,isWhite,move.piece.charAt(0));
                                }
                            }
                            if(move.piece.charAt(0) == 'P'){
                                if(move.enPassant && this.enPassant_able != null){
                                    int passantCol = enPassant_able.charAt(0) - 'a';
                                    int passantRow = enPassant_able.charAt(1) - '1';
                                    this.board.setBoard(passantRow, passantCol, 0);
                                }
                                this.enPassant_able = Pieces.enPassant(board,row,col,move,isWhite);
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
                    if (tempBoard[row][col] == piece) {
                        if (Pieces.isValidMove(board, Math.abs(piece), row, col, move, isWhite,this.enPassant_able)) {
                            System.out.println(this.enPassant_able + " " + move.location);

                            this.board.setBoard(row, col, 0);
                            this.board.setBoard(destRow, destCol, piece);
                            if(move.piece.charAt(0) == 'R' || move.piece.charAt(0) == 'K'){
                                if(row == 0 || row == 7){
                                    updateCastlings(row,col,isWhite,move.piece.charAt(0));
                                }
                            }
                            if(move.piece.charAt(0) == 'P'){
                                if(move.enPassant && this.enPassant_able != null){
                                    int passantCol = enPassant_able.charAt(0) - 'a';
                                    int passantRow = enPassant_able.charAt(1) - '1';
                                    this.board.setBoard(passantRow, passantCol, 0);
                                }
                                this.enPassant_able = Pieces.enPassant(board,row,col,move,isWhite);
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
//        if(Pieces.isChecked(board,0,0 ,true,false)) return false;
        return true;
    }
}
