public class Pieces {
    public static boolean isValidMove(Board board, int piece,int row, int col, MoveInfo move, boolean isWhite) {
        return switch (piece) {
            case 1 -> validPawn(board, row, col, move, isWhite);
            case 2 -> validKing(board, row, col, move, isWhite);
            case 3 -> validQueen(board, row, col, move, isWhite);
            case 4 -> validBishop(board, row, col, move, isWhite);
            case 5 -> validKnight(board, row, col, move, isWhite);
            case 6 -> validRook(board, row, col, move, isWhite);
            default -> false;
        };
    }

    public static boolean validPawn(Board board,int row, int col ,MoveInfo move, boolean isWhite) {
        String dest = move.destination;
        int destCol = dest.charAt(0) - 'a';
        int destRow = dest.charAt(1) - '1';
        int dy = Math.abs(destRow - row);
        if(!move.capture && col != destCol) {
            return false;
        }
        if(isWhite) {
            if (move.capture){
                return false;
            }else{
                return (dy <= 2 && row == 1) || (dy == 1 && row > 2) && destRow <= 7;
            }
        }else{
            if (move.capture){
                return false;
            }else{
                return (dy <= 2);
            }

        }
    }
    public static boolean validBishop(Board board, int row, int col, MoveInfo move, boolean isWhite) {
        System.out.println(row + " " + col + "\n");
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        // check how many squares moved horizontally and vertically
        int dx = Math.abs(destCol - col);
        int dy = Math.abs(destRow - row);
        return dx == dy;

    }
    public static boolean validKnight(Board board,int row, int col, MoveInfo move,boolean isWhite) {
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        // check how many squares moved horizontally and vertically
        int dy = Math.abs(destRow - row);
        int dx = Math.abs(destCol - col);

        return (dy == 2 && dx == 1) || (dy == 1 && dx == 2);
    }
    public static boolean validRook(Board board, int row, int col, MoveInfo move,boolean isWhite) {
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';

        if (row != destRow && col != destCol) return false;
        // check how many squares moved horizontally and vertically
        int dy = Integer.signum(destRow - row);
        int dx = Integer.signum(destCol - col);

        int r = row + dy, c = col + dx;
        while (r != destRow || c != destCol) {
            if (board.getPiece(r,c)!= 0) return false;
            r += dy;
            c += dx;
        }

        return true;
    }
    public static boolean validQueen(Board board, int row, int col, MoveInfo move,boolean isWhite) {
        return validRook(board, row, col, move, isWhite) || validBishop(board, row, col, move, isWhite);
    }
    public static boolean validKing(Board board,int row, int col, MoveInfo move,boolean isWhite) {
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        // check how many squares moved horizontally and vertically
        int dy = Math.abs(destRow - row);
        int dx = Math.abs(destCol - col);

        return dy <= 1 && dx <= 1;
    }
}
