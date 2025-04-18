package Chess;

import Pieces.*;

public class Board {
//    int[][] board;
    private Piece[][] board = new Piece[8][8];
//    private int[][] alternativeBoard = new int[8][8];
    public Board() {
//        board = new Piece[8][8];
        initialize();
    }

    public Piece[][] getBoard() {
        return this.board;
    }
    public void setBoard(int row,int col,Piece val){
        this.board[row][col] = val;
    }
    public Piece getPiece(int row,int col){
        return this.board[row][col];
    }
//    public final void alternateInitialize() {
//        alternativeBoard[0][0] = -6;
//        alternativeBoard[0][1] = -5;
//        alternativeBoard[0][2] = -4;
//        alternativeBoard[0][3] = -3;
//        alternativeBoard[0][4] = -2;
//        alternativeBoard[0][5] = -4;
//        alternativeBoard[0][6] = -5;
//        alternativeBoard[0][7] = -6;
//        for (int i = 0; i < 8; i++) {
//            alternativeBoard[1][i] = -1;
//        }
//        alternativeBoard[7][0] = 6;
//        alternativeBoard[7][1] = 5;
//        alternativeBoard[7][2] = 4;
//        alternativeBoard[7][3] = 3;
//        alternativeBoard[7][4] = 2;
//        alternativeBoard[7][5] = 4;
//        alternativeBoard[7][6] = 5;
//        alternativeBoard[7][7] = 6;
//        for (int i = 0; i < 8; i++) {
//            alternativeBoard[6][i] = 1;
//        }
//    }
    public final void initialize() {
        // initialize white pieces


        board[0][0] = new Rook("white", false);
        board[0][1] = new Knight("white", false);
        board[0][2] = new Bishop("white", false);
        board[0][3] = new Queen("white");
        board[0][4] = new King("white");
        board[0][5] = new Bishop("white", true);
        board[0][6] = new Knight("white", true);
        board[0][7] = new Rook("white", true);
        //initialize white Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("white", i);
        }
        //initialize black Pieces
        board[7][0] = new Rook("black", false);
        board[7][1] = new Knight("black", false);
        board[7][2] = new Bishop("black", false);
        board[7][3] = new Queen("black");
        board[7][4] = new King("black");
        board[7][5] = new Bishop("black", true);
        board[7][6] = new Knight("black", true);
        board[7][7] = new Rook("black", true);

        //initialize black Pawns
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn("black", i);
        }
    }

    public final void illegalCaptureEmptySquare() {
        this.board = new Piece[8][8];
        board[0][0] = new Rook("white",true); // White rook on a1
        board[1][4] = new Pawn("white",1); // White pawn on e2
        // Rook tries to capture on e5, but e5 is empty
    }

    public final void illegalCaptureOwnPiece() {
        this.board = new Piece[8][8];
        board[0][1] = new Knight("white",false); // White knight on b1
        board[2][5] =  new Pawn("white",2); // White pawn on f3
        // Knight tries to capture on f3
    }

    public final void illegalRookThroughPieces() {
        this.board = new Piece[8][8];
        board[7][0] = new Rook("white",true); // White rook on a1
        board[6][0] = new Pawn("white",1); // White pawn blocking a2
        // Rook tries to move a1 to a8
    }

    public final void illegalCastleWhileInCheck() {
        this.board = new Piece[8][8];
        board[0][4] = new King("white"); // White king on e1
        board[0][7] = new Rook("white",true); // White rook on h1
        board[7][4] = new Queen("black");  // Black queen on e8 attacking e1
        // Attempt: White castles king-side
    }

    public final void illegalCastleThroughCheck() {
        this.board = new Piece[8][8];
        board[0][4] = new King("white"); // White king on e1
        board[0][7] = new Rook("white",true); // White rook on h1
        board[3][5] = new Queen("black");  // Black queen attacking f1
        // Attempt: White castles king-side
    }

    public final void illegalCastleAfterKingMoved() {
        this.board = new Piece[8][8];
        board[1][4] = new King("white"); // White king (assume has moved)
        board[0][0] = new Rook("white",false); // White rook on a1
        // You would mark internally that the king has moved
    }

    public final void illegalKnightMove() {
        this.board = new Piece[8][8];
        board[1][1] = new Knight("white",true); // White knight on b1
        board[4][4] = new Pawn("black",1);  // Black pawn on e5
        // Attempt: Knight tries to take e5 diagonally
    }

    public final void illegalMoveIntoCheck() {
        this.board = new Piece[8][8];
        board[0][5] = new King("white"); // White king on f1
        board[2][4] = new Queen("black");  // Black queen on e3, attacking e2
        // Attempt: Ke2
    }

    public final void illegalPawnCaptureEmpty() {
        this.board = new Piece[8][8];
        board[1][4] = new Pawn("white",1); // White pawn on e2
        board[2][3] = null;  // d5 is empty
        // Attempt: exd5
    }

    public final void illegalPromotionPiece() {
        this.board = new Piece[8][8];
        board[0][4] = new Pawn("white",1); // White pawn on e1
        // Attempt: e8=Z (invalid piece type)
    }
    public static String getSymbol(int value) {
        switch (Math.abs(value)) {
            case 1: return value > 0 ? "♙ " : "♟ "; // Pawn
            case 2: return value > 0 ? "♔ " : "♚ "; // King
            case 3: return value > 0 ? "♕ " : "♛ "; // Queen
            case 4: return value > 0 ? "♗ " : "♝ "; // Bishop
            case 5: return value > 0 ? "♘ " : "♞ "; // Knight
            case 6: return value > 0 ? "♖ " : "♜ "; // Rook
            default: return ". "; // Empty square
        }
    }

    public void drawBoard() {
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = this.board[row][col];
                if (piece == null) {
                    System.out.print(". " + " ");

                }else{
                    System.out.print(piece.draw()+ " ");
                }

            }
            System.out.println();
        }
        for (char c = 'a'; c <= 'h'; c++) {
            System.out.print("  " + c);
        }
    }
}