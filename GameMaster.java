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



    public void analyzeGame(){

        for (int i = 0; i < this.game.length-1; i++) {
            int isBlack = i % 2;
            MoveInfo move = new MoveInfo();
            move.decipherMove(this.game[i]);

        }
        this.board.drawBoard();
    }

    public boolean analyzeMove(String move) {
         return true;
    }
}
