package Chess;
import GM.GameMaster;
import PgnAnalyzers.PGNReader;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class chess{
    // BufferedReader file;

    static public BufferedReader readPgn(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }

    public static void main(String[] args){

        PGNReader reader = new PGNReader();
        List<Future<Boolean>> results = new ArrayList<>();
        try {
            BufferedReader file = readPgn("pgns/Tbilisi2015.pgn");
            reader.extractGames(file);
        } catch (IOException e) {
            System.out.println("File not found");
        }


        try (ExecutorService executor = Executors.newFixedThreadPool(
                4, r -> {
                    Thread t = new Thread(r);
                    t.setName("Game-Thread-" + t.getId());
                    return t;
                })) {

            for (ChessGame game : reader.getGames()) {
//                if (!game.getMoves().matches(regex)) continue;

                Callable<Boolean> task = () -> {
                    GameMaster gm = new GameMaster(game.getMoves());
                    boolean result = gm.analyzeGame();
                    synchronized (System.out) {
                        System.out.println("== Game Output from Thread: " + Thread.currentThread().getName() + " ==");
                    }

                    return result;
                };

                results.add(executor.submit(task));
            }
        }

        System.out.println("Total games submitted: " + results.size());
        for (Future<Boolean> future : results) {
            try {
                Boolean valid = future.get(); // blocks until ready
                System.out.println("Game validity: " + valid);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error during game validation", e);
            }
        }

    }
}