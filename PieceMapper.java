public enum PieceMapper {
    PAWN('P', 1),
    KING('K', 2),
    QUEEN('Q', 3),
    BISHOP('B', 4),
    KNIGHT('N', 5),
    ROOK('R', 6);

    PieceMapper(char symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }
    private final char symbol;
    private final int value;

    public int getValue() {
        return value;
    }

    public char getSymbol() {
        return symbol;
    }
}