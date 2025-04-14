public class Pieces {
    public static boolean isValidMove(Board board, int piece,int row, int col, MoveInfo move, boolean isWhite, String enPassant) {
        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        if(board.getPiece(row,col) == 0) return false;
        if(move.capture){
            if(board.getPiece(destRow,destCol) == 0 && !move.enPassant){
                return false;
                //capture unavailable since square is empty
            }
            if(board.getPiece(destRow,destCol) > 0 && !isWhite) return false;
            if(board.getPiece(destRow,destCol) < 0 && isWhite) return false;
        }else{
            if(board.getPiece(destRow,destCol) != 0){
                return false;
                //there should not be anything on square, since nothing was captured by pgn
            }
        }
        return switch (piece) {
            case 1 -> validPawn(board, row, col, move, isWhite,enPassant);
            case 2 -> validKing(board, row, col, move, isWhite);
            case 3 -> validQueen(board, row, col, move, isWhite);
            case 4 -> validBishop(board, row, col, move, isWhite);
            case 5 -> validKnight(board, row, col, move, isWhite);
            case 6 -> validRook(board, row, col, move, isWhite);
            default -> false;
        };
    }

    public static String enPassant(Board board,int row, int col ,MoveInfo move, boolean isWhite){
        String dest = move.destination;
        int destCol = dest.charAt(0) - 'a';
        int destRow = dest.charAt(1) - '1';
        int dy = Math.abs(destRow - row);
        int dx = Math.abs(destCol - col);
        System.out.println(dy);
        if(dy == 2) return dest;
        return null;
    }
    public static boolean validPawn(Board board,int row, int col ,MoveInfo move, boolean isWhite, String enPassant_able) {
        String dest = move.destination;
        int destCol = dest.charAt(0) - 'a';
        int destRow = dest.charAt(1) - '1';
        int dy = Math.abs(destRow - row);
        int dx = Math.abs(destCol - col);
        if(!move.capture && col != destCol) {
            return false;
        }
        System.out.println(enPassant_able);
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
    public static boolean validBishop(Board board, int row, int col, MoveInfo move, boolean isWhite) {
        System.out.println(row + " " + col + "\n");

        int destCol = move.destination.charAt(0) - 'a';
        int destRow = move.destination.charAt(1) - '1';
        // check how many squares moved horizontally and vertically
        int dx = Math.abs(destCol - col);
        int dy = Math.abs(destRow - row);
        if(dx == 0 || dy == 0) return false; //did no move

        int stepRow = (destRow - row) / dx;
        int stepCol = (destCol - col) / dx;
        for (int i = 1; i < dx; i++) {
            if (board.getPiece(row + i * stepRow, col + i * stepCol) != 0) return false;
        }

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

        // check move direction
        int dy = Integer.signum(destRow - row);
        int dx = Integer.signum(destCol - col);

        int r = row + dy, c = col + dx;
        while (r != destRow || c != destCol) {
            if (board.getPiece(r,c)!= 0) return false;
            r += dy;
            c += dx;
        }
        if(move.capture){
            // nothing to capture
            return board.getPiece(destRow, destCol) != 0;
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
        if(Pieces.isChecked(board,destRow,destCol ,true,true)) return false;

        return dy <= 1 && dx <= 1;
    }
    public static boolean isChecked(Board board, int row, int col, boolean isWhite,boolean kingLoc) {
        int kingPiece = isWhite ? -2 : 2;
        int kingRow = 0;
        int kingCol = 0;
        // find king
        if(!kingLoc){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(board.getPiece(i,j) == kingPiece){
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


        int knight = (isWhite) ? 5 : -5;
        int rook = (isWhite) ? 6 : -6;
        int queen = (isWhite) ? 3 : -3;
        int bishop = (isWhite) ? 4 : -4;
        /* check if king is threatened by either rook or queen vertically or horizontally */
        // vertical
        for (int i = row-1; i >= 0; i--) {
            int pieceRow = board.getPiece(row,i);
            if (pieceRow != queen && pieceRow != rook && pieceRow != 0) break;
            if(pieceRow == 0 ) continue;
            return true;
        }
        for (int i = row+1; i < 8; i++) {
            int pieceRow = board.getPiece(row,i);
            if (pieceRow != queen && pieceRow != rook && pieceRow != 0) break;
            if(pieceRow == 0 ) continue;
            return true;
        }
        // horizontal
        for (int i = col-1; i >= 0; i--) {
            int pieceCol = board.getPiece(i,col);
            if (pieceCol != queen && pieceCol != rook && pieceCol != 0) break;
            if(pieceCol == 0) continue;
            return true;
        }
        for (int i = col+1; i < 8; i++) {
            int pieceCol = board.getPiece(i,col);
            if (pieceCol != queen && pieceCol != rook && pieceCol != 0) break;
            if(pieceCol == 0) continue;
            return true;
        }

        /*check if king is threatened by either bishop or queen diagonally */
        for (int i = 0; i < 4; i++) {
            int k;
            int j;
            if (i % 4 >= 2) {
                // up diagonally
                j = 1;
            } else {
                //  bellow diagonally
                j = -1;
            }
            if (i % 2 == 0) {
                //  left diagonally
                k = 1;
            } else {
                //  right diagonally
                k = -1;
            }
            int rows = row;
            int cols = col;

            while (rows >= 0 && rows < 8 && cols >= 0 && cols < 8) {
                int piece = board.getPiece(rows, cols);
                if(piece == 0) {
                    rows += k;
                    cols += j;
                    continue;
                }
                if((isWhite && piece < 0) || (!isWhite && piece > 0)) break;
                if (piece != queen && piece != bishop) break;

                return true;

            }
        }

        /* check if king is threatened by a pawn */
        int pawnDirection = (isWhite) ? 1 : -1;
        for (int i = 0; i < 2; i++){
            int moveRow = row + pawnDirection;
            int moveCol = col  + (i % 2 == 0 ? -1 : 1);
            if(moveRow >=0 && moveRow < 8 && moveCol >= 0 && moveCol < 8){
                int piece = board.getPiece(moveRow,moveCol);
                if (piece != 0) {
                    if(isWhite){
                        if(piece == 1) return true;
                    }else{
                        if(piece == -1) return true;
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
                int piece = board.getPiece(newRow,newCol);
                if(piece == knight) return true;
            }
        }
        return false;
    }

    public static boolean canCastle(Board board, boolean[] movements, boolean isWhite, boolean kingSide, int King, int Rook, int Row) {
        int king = board.getPiece(Row,4);
        System.out.println(king +" " + King);
        if(king != King) return false;
        if(kingSide){
            if(movements[1] || movements[2]) return false;
            int rook = board.getPiece(Row,7);
            if(rook !=Rook) return false;
            if (isChecked(board,Row,4,isWhite,true)) return false;
            for(int i = 5; i < 7; i++){
                if(board.getPiece(Row,i) != 0) return false;
                if(isChecked(board,Row,i,isWhite,true)) return false;
            }
        }else{
            if(movements[0] || movements[2]) return false;
            int rook = board.getPiece(Row,1);
            if(rook !=Rook) return false;
            if (isChecked(board,Row,4,isWhite,true)) return false;
            for(int i = 1; i < 4; i++){
                if(board.getPiece(Row,i) != 0) return false;
                if(isChecked(board,Row,i,isWhite,true)) return false;
            }

        }
        return true;
    }
}
