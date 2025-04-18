import Chess.Chess;
import PgnAnalyzers.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filePath;

        if (args.length >= 1) {
            filePath = args[0];
            System.out.println("Received file path from command-line argument.");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter file path: ");
            filePath = scanner.nextLine();
            scanner.close();
        }

        System.out.println("Using file path: " + filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Error: File does not exist.");
            return;
        }
        Chess chess = new Chess();
        chess.run(filePath);
    }
}