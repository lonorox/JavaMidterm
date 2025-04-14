import java.util.HashMap;
import java.util.Map;

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
    private static final Map<Character, PieceMapper> SYMBOL_TO_TYPE = new HashMap<>();

    static {
        for (PieceMapper piece : values()) {
            SYMBOL_TO_TYPE.put(piece.symbol, piece);
        }
    }
    public static int getValueBySymbol(char symbol) {
        return SYMBOL_TO_TYPE.getOrDefault(symbol, null) != null
                ? SYMBOL_TO_TYPE.get(symbol).getValue()
                : 0;
    }
}