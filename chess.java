import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
class chess{
    // BufferedReader file;
    
    static public BufferedReader readPgn(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }
    
    public static void main(String[] args){
        System.out.println("Hello World");
        try {
            BufferedReader file = readPgn("pgns/Tbilisi2015.pgn");
            System.out.println(file.readLine());
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}