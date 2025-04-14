import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMaster {
    final private String[] game;
    final private Board board;

    public GameMaster(String game) {
        this.game = game.replaceAll("\\d+\\.", "").substring(1).split("\\s+");
        this.board = new Board();
    }

    public void analyzeGame() {
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
        for (int i = 0; i < this.game.length - 1; i++) {
//            this.board.drawBoard();
            boolean isWhite = i % 2 == 0;
            MoveInfo move = new MoveInfo();
            move.decipherMove(this.game[i]);
            System.out.println("\n "+this.game[i] + "\n");
            if (move.castlingQueen || move.castlingKing){
                continue;
            }
            int piece = PieceMapper.getValueBySymbol(move.piece.charAt(0));
            if (move.location.isEmpty()){
                int[][] tempBoard = this.board.getBoard();
                boolean foundValid = false;
                for(int row = 0; row < 8; row++){
                    for(int col = 0; col < 8; col++){
                        int destRow = Character.getNumericValue(move.destination.charAt(1)) - 1;
                        int destCol = move.destination.charAt(0) - 'a';
                        if(isWhite){
                            if(tempBoard[row][col] == piece * (-1)){
                                if (Pieces.isValidMove(board, piece ,row, col, move, true)) {
                                    this.board.setBoard(row, col, 0);
                                    this.board.setBoard(destRow, destCol, piece * (-1));
                                    foundValid = true;
                                }
                            }
                        }else{
                            if(tempBoard[row][col] == piece){
                                if (Pieces.isValidMove(board, piece ,row, col, move, false)) {
                                    this.board.setBoard(row, col, 0);
                                    this.board.setBoard(destRow, destCol, piece);
                                    foundValid = true;
                                }
                            }
                        }
                    }
                }
                if(!foundValid){
                    System.out.println("Invalid move");
                    break;
                }
            }else{
                if (move.location.length() == 2){
                    int col = move.location.charAt(0) - 'a';
                    int row = Character.getNumericValue(move.location.charAt(1)) - 1;
                    int destRow = Character.getNumericValue(move.destination.charAt(1)) - 1;
                    int destCol = move.destination.charAt(0) - 'a';
                    if (Pieces.isValidMove(board, piece ,row, col, move, isWhite)) {
                        this.board.setBoard(row, col, 0);
                        this.board.setBoard(destRow, destCol, piece * (isWhite ? 1 : -1));
                    }else{
                        System.out.println("Invalid move");
                        break;
                    }
                }
            }

            this.board.drawBoard();
        }
    }

    public boolean analyzeMove(String move) {
         return true;
    }
}
