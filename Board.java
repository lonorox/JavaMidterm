import Pieces.*;

public class Board {
    int[][] board;
    private int[][] alternativeBoard = new int[8][8];
    public Board() {
//        board = new Piece[8][8];
        alternateInitialize();
//  initialize();
    }

    public int[][] getBoard() {
        return this.alternativeBoard;
    }
    public void setBoard(int row,int col,int val){
        this.alternativeBoard[row][col] = val;
    }
    public int getPiece(int row,int col){
        return this.alternativeBoard[row][col];
    }
    public final void alternateInitialize() {
        alternativeBoard[0][0] = -6;
        alternativeBoard[0][1] = -5;
        alternativeBoard[0][2] = -4;
        alternativeBoard[0][3] = -3;
        alternativeBoard[0][4] = -2;
        alternativeBoard[0][5] = -4;
        alternativeBoard[0][6] = -5;
        alternativeBoard[0][7] = -6;
        for (int i = 0; i < 8; i++) {
            alternativeBoard[1][i] = -1;
        }
        alternativeBoard[7][0] = 6;
        alternativeBoard[7][1] = 5;
        alternativeBoard[7][2] = 4;
        alternativeBoard[7][3] = 3;
        alternativeBoard[7][4] = 2;
        alternativeBoard[7][5] = 4;
        alternativeBoard[7][6] = 5;
        alternativeBoard[7][7] = 6;
        for (int i = 0; i < 8; i++) {
            alternativeBoard[6][i] = 1;
        }
    }

    public final void illegalCaptureEmptySquare() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[0][0] = -6; // White rook on a1
        alternativeBoard[1][4] = -1; // White pawn on e2
        // Rook tries to capture on e5, but e5 is empty
    }

    public final void illegalCaptureOwnPiece() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[0][1] = -5; // White knight on b1
        alternativeBoard[2][5] = -1; // White pawn on f3
        // Knight tries to capture on f3
    }

    public final void illegalRookThroughPieces() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[7][0] = -6; // White rook on a1
        alternativeBoard[6][0] = -1; // White pawn blocking a2
        // Rook tries to move a1 to a8
    }

    public final void illegalCastleWhileInCheck() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[0][4] = -2; // White king on e1
        alternativeBoard[0][7] = -6; // White rook on h1
        alternativeBoard[7][4] = 3;  // Black queen on e8 attacking e1
        // Attempt: White castles king-side
    }

    public final void illegalCastleThroughCheck() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[0][4] = -2; // White king on e1
        alternativeBoard[0][7] = -6; // White rook on h1
        alternativeBoard[3][5] = 3;  // Black queen attacking f1
        // Attempt: White castles king-side
    }

    public final void illegalCastleAfterKingMoved() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[1][4] = -2; // White king (assume has moved)
        alternativeBoard[0][0] = -6; // White rook on a1
        // You would mark internally that the king has moved
    }

    public final void illegalKnightMove() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[1][1] = -5; // White knight on b1
        alternativeBoard[4][4] = 1;  // Black pawn on e5
        // Attempt: Knight tries to take e5 diagonally
    }

    public final void illegalMoveIntoCheck() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[0][5] = -2; // White king on e1
        alternativeBoard[2][4] = 3;  // Black queen on e3, attacking e2
        // Attempt: Ke2
    }

    public final void illegalPawnCaptureEmpty() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[1][4] = -1; // White pawn on e2
        alternativeBoard[2][3] = 0;  // d5 is empty
        // Attempt: exd5
    }

    public final void illegalPromotionPiece() {
        this.alternativeBoard = new int[8][8];
        alternativeBoard[0][4] = -1; // White pawn on e1
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
                int piece = this.alternativeBoard[row][col];
                System.out.print(getSymbol(piece) + " ");
//                System.out.print(piece + " " + col + " ");
            }
            System.out.println();
        }
        for (char c = 'a'; c <= 'h'; c++) {
            System.out.print("  " + c);
        }
    }
}
