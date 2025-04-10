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
        String positionsRegex = "[a-h][1-8]";
        for (int i = 0; i < this.game.length-1; i++) {
            int isBlack = i % 2;

            String piece = "";
            String disambiguateFile = "";
            String disambiguateRank = "";
            String location = "";
            String capture = "";
            String destination = "";
            String promotion = "";
            String check = "";
            String checkmate = "";
            String castlingKing = "";
            String castlingQueen = "";
            String enPassant = "";

            String move = this.game[i];

            if (move.contains("o-o-o") || move.contains("O-O-O") || move.contains("0-0-0")){
                castlingQueen = "0-0-0";
            }
            if (move.contains("o-o") || move.contains("O-O") || move.contains("0-0")){
                castlingKing = "0-0";
            }
            if (castlingQueen.isEmpty() && castlingKing.isEmpty()){
                Pattern pattern = Pattern.compile(positionsRegex);
                Matcher matcher = pattern.matcher(move);

                List<String> matches = new ArrayList<>();

                while (matcher.find()) {
                    matches.add(matcher.group());
                }
                System.out.println(move);


                //check which piece is moving

                if (matches.size() > 1){
                    location = matches.get(0);
                    destination = matches.get(1);
                }else if (matches.size() == 1){
                    destination = matches.getFirst();
                    location = "";
                }else{
                    destination = "";
                    location = "";
                }

                if (Character.isUpperCase(move.charAt(0))){
                    piece = move.charAt(0)+"";
                }else{
                    piece = "P";
                }

                if (location.isEmpty()){
                    if (Character.isDigit(move.charAt(0))) {
                        disambiguateRank = move.charAt(0) + "";
                    }else if (Character.isDigit(move.charAt(1)) && piece != "P") {
                        disambiguateRank = move.charAt(1)+"";
                    }else{
                        disambiguateRank = "";
                    }

                    if (Character.isLowerCase(move.charAt(0)) && !Character.isDigit(move.charAt(1)) && piece == "P" && move.charAt(0) != 'x') {
                        disambiguateFile = move.charAt(0)+"";
                    }
                    else if(Character.isLowerCase(move.charAt(1)) && !Character.isDigit(move.charAt(2)) && move.charAt(1) != 'x') {
                        disambiguateFile = move.charAt(1)+"";
                    }else{
                        disambiguateFile = "";
                    }
                }
            }

            if(move.contains("x")){
                capture = "x";
            }

            if(move.contains("=")){
                int index = move.indexOf("=");
                promotion = move.substring(index,index+1);
            }

            if(move.contains("#")){
                checkmate = "#";
            }

            if(move.contains("+")){
                check = "+";
            }

            if(move.contains("e.p.") || move.contains("ep")){
                enPassant = "e.p.";
            }

            System.out.print(""+piece);
            System.out.print(disambiguateFile);
            System.out.print(disambiguateRank);
            System.out.print(location);
            System.out.print(capture);
            System.out.print(destination);
            System.out.print(check);
            System.out.print(checkmate);
            System.out.print(promotion);
            System.out.print(castlingKing);
            System.out.print(castlingQueen);
            System.out.print(enPassant);
            System.out.print("\n");

        }
    }

    public boolean analyzeMove(String move) {
         return true;
    }
}
