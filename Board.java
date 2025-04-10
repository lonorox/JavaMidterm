import Pieces.*;

public class Board {
    Piece[][] board;
    final private int[][] alternativeBoard = new int[8][8];
    public Board() {
        board = new Piece[8][8];
        initialize();
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
        alternativeBoard[7][0] = -6;
        alternativeBoard[7][1] = -5;
        alternativeBoard[7][2] = -4;
        alternativeBoard[7][3] = -3;
        alternativeBoard[7][4] = -2;
        alternativeBoard[7][5] = -4;
        alternativeBoard[7][6] = -5;
        alternativeBoard[7][7] = -6;
        for (int i = 0; i < 8; i++) {
            alternativeBoard[6][i] = -1;
        }
    }
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

    
}
