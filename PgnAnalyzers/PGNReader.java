package PgnAnalyzers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Chess.*;
public class PGNReader {
     List<ChessGame> games;

    public PGNReader() {
        games = new ArrayList<>();
    }   

    public void addGame(ChessGame game) {
        games.add(game);
    }

    public List<ChessGame> getGames() {
        return games;
    }

    private boolean isValidMovetext(String movetext) {
        Pattern movePattern = Pattern.compile("\\s*(\\d{1,3})\\.?\\s*((?:(?:O-O(?:-O)?)|(?:[KQNBR][1-8a-h]?x?[a-h]x?[1-8])|(?:[a-h]x?[a-h]?[1-8]\\=?[QRNB]?))\\+?)(?:\\s*\\d+\\.?\\d+?m?s)?\\.?\\s*((?:(?:O-O(?:-O)?)|(?:[KQNBR][1-8a-h]?x?[a-h]x?[1-8])|(?:[a-h]x?[a-h]?[1-8]\\=?[QRNB]?))\\+?)?(?:\\s*\\d+\\.?\\d+?m?s)?\n");

        Matcher matcher = movePattern.matcher(movetext);

        // We’ll count how many full turns (number + two moves) we find
        return matcher.find();// At least some valid turns found
    }

    public void extractGames(BufferedReader file) throws IOException {
        StringBuilder movetextBuilder = new StringBuilder();
        Map<String, String> tags = new HashMap<>();
        String line = file.readLine();
        while (line != null) {
            //check if line is a tag by searching for "[" and "]"
            if (line.contains("[") && line.contains("]")) {
                // if there is a movetext, add the game to the list, and reset the movetext and tags
                if (!movetextBuilder.isEmpty()) {

//                    if(!isValidMovetext(movetextBuilder.toString())) {
                    String movetext = movetextBuilder.toString().trim();
                    addGame(new ChessGame(tags, movetext));

                    movetextBuilder = new StringBuilder();
                    tags = new HashMap<>();
                }
                //split tags into tag value and tag name
                String[] tag = line.split("\"");
                //store tags in a map
                tags.put(tag[0].substring(1), " \"" + tag[1] + "\"");
            }
            //if line is not a tag, it is part of the movetext
            if (!line.startsWith("[") && !line.trim().isEmpty()) {
                movetextBuilder.append(line).append(" ");
            }

            line = file.readLine();
        }

    }
    
}
