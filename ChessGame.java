import java.util.Map;

public class ChessGame {
    final private Map<String, String> tags; // tag roasters section
    final private String moves;             // Movetext section

    // Constructor
    public ChessGame(Map<String, String> tags, String moves) {
        this.tags = tags;
        this.moves = moves;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public String getMoves() {
        return moves;
    }
}
