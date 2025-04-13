import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;

class chess{
    // BufferedReader file;
    
    static public BufferedReader readPgn(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }
    
    public static void main(String[] args){
        PGNReader reader = new PGNReader();
        try {
            BufferedReader file = readPgn("pgns/Tbilisi2015.pgn");
            reader.extractGames(file);
        } catch (IOException e) {
            System.out.println("File not found");
        }
        GameMaster master = new GameMaster(reader.getGames().getFirst().getMoves());
        master.analyzeGame();

    }
}