import Chess.Board;
import Exceptions.InvalidMoveException;
import PgnAnalyzers.MoveInfo;
import org.junit.jupiter.api.Test;
import GM.GameMaster;
import static org.junit.jupiter.api.Assertions.*;


public class UnitTests {

    private Board board;

    @Test
    public void testIllegalCaptureOwnPiece() {
        Board board = new Board();
        board.illegalCaptureOwnPiece();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("Nxf3");
        boolean result = false;
        try {
            result = gm.analyzeMove(move,true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result); // Should be illegal
    }

    @Test
    public void testIllegalCaptureEmptySquare() {
        Board board = new Board();
        board.illegalCaptureEmptySquare();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("Rxe5"); // Rook tries to capture an empty square
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }
    @Test
    public void testIllegalCastleWhileInCheck() {
        Board board = new Board();
        board.illegalCastleWhileInCheck();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("O-O"); // Castling while in check
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

    @Test
    public void testIllegalCastleThroughCheck() {
        Board board = new Board();
        board.illegalCastleThroughCheck();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("O-O"); // Castling through check (f1 attacked)
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

    @Test
    public void testIllegalCastleAfterKingMoved() {
        Board board = new Board();
        board.illegalCastleAfterKingMoved();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("O-O-O"); // Castling after king moved
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

    @Test
    public void testIllegalKnightMove() {
        Board board = new Board();
        board.illegalKnightMove();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("Nxe5"); // Knight tries to move diagonally
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

    @Test
    public void testIllegalMoveIntoCheck() {
        Board board = new Board();
        board.illegalMoveIntoCheck();
        board.drawBoard();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        MoveInfo move = new MoveInfo();
        move.decipherMove("Kf2"); // King moves into check
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);

    }

    @Test
    public void testIllegalPawnCaptureEmpty() {
        Board board = new Board();
        board.illegalPawnCaptureEmpty();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("exd5"); // Pawn tries to capture empty square
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

    @Test
    public void testIllegalPromotionPiece() {
        Board board = new Board();
        board.illegalPromotionPiece();
        GameMaster gm = new GameMaster("");
        gm.setBoard(board);
        board.drawBoard();
        MoveInfo move = new MoveInfo();
        move.decipherMove("e8=Z"); // Invalid promotion piece
        boolean result = false;
        try {
            result = gm.analyzeMove(move, true);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }
    @Test
    public void testIllegalEnPassant() {
        Board board = new Board();
        GameMaster gm = new GameMaster("1. e4 d5 2. e5 f5 3. d4 g5 4. h4 g4 5. f4 h6 6. c4 gxf3e.p. 1-0");
        boolean result = gm.analyzeGame();
        assertFalse(result);
    }
    @Test
    public void testLegalEnPassant() {
        Board board = new Board();
        GameMaster gm = new GameMaster("1. e4 d5 2. e5 f5 3. exf6ep g5 4. f4 gxf4 1-0");
        boolean result = gm.analyzeGame();
        assertTrue(result);

    }
    @Test
    public void testKnightMove(){
        Board board = new Board();
        GameMaster gm = new GameMaster("1. Nf3 Nf6 2. Nc3 Nc6 3. e4 e5 4. Nxe5 Nxe4 1-0");
        boolean result = gm.analyzeGame();
        assertTrue(result);
    }
    @Test
    public void testBishopMove(){
        Board board = new Board();
        GameMaster gm = new GameMaster("1. e4 e5 2. Bc4 Bc5 3. Qh5 Nc6 4. Qxf7# 1-0");
        boolean result = gm.analyzeGame();
        assertTrue(result);
    }
    @Test
    public void testRookMove(){
        Board board = new Board();
        GameMaster gm = new GameMaster("1. d4 d5 2. Nf3 Nf6 3. Bf4 e6 4. e3 Bd6 5. Bxd6 Qxd6 6. c4 c6 7. Nc3 O-O 8. Rc1 Nbd7 9. c5 Qe7 10. Bd3 e5 11. dxe5 Nxe5 12. Nxe5 Qxe5 13. O-O Re8 14. Re1 Ng4 15. g3 Qh5 1-0");
        boolean result = gm.analyzeGame();
        assertTrue(result);
    }
    @Test
    public void testQueenMove(){
        Board board = new Board();
        GameMaster gm = new GameMaster("1. d4 d5 2. c4 c6 3. Nc3 Nf6 4. Qb3 dxc4 5. Qxc4 Bf5 6. Qb3 Qb6 7. Qxb6 axb6 1-0");
        boolean result = gm.analyzeGame();
        assertTrue(result);
    }
//    public void testIllegalCastleWhileInCheck() {
//        Chess.ChessGame game = new Chess.ChessGame();
//        game.illegalCastleWhileInCheck();
//        boolean result = game.isLegalMove("O-O");
//        assertFalse(result); // Castling while in check is illegal
//    board.drawBoard();
//    }
//
//    @Test
//    public void testMoveThroughPiece() {
//        Chess.ChessGame game = new Chess.ChessGame();
//        game.illegalRookThroughPieces();
//        boolean result = game.isLegalMove("Ra1-a8");
//        assertFalse(result); // Rook can't jump over pawns
//    }
//
//    @Test
//    public void testIllegalKnightMove() {
//        Chess.ChessGame game = new Chess.ChessGame();
//        game.illegalKnightMove();
//        boolean result = game.isLegalMove("Nxe5");
//        assertFalse(result); // Knight can't move like bishop
//    }
//
//    @Test
//    public void testMoveIntoCheck() {
//        Chess.ChessGame game = new Chess.ChessGame();
//        game.illegalMoveIntoCheck();
//        boolean result = game.isLegalMove("Ke2");
//        assertFalse(result); // King can't move into check
//    }

}
