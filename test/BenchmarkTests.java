import static org.junit.Assert.assertTrue;

public class BenchmarkTests {
    public static void main(String[] args) {
        String startGame = "";
        String midGame   = "";
        String endGame   = "";

        benchmarkTests("Start Game", startGame);
        benchmarkTests("Mid Game", midGame);
        benchmarkTests("End Game", endGame);

    }

    private static void benchmarkTests(String gamePosition, String FEN) {
        Board board = new Board();
        board.fenToBoard(FEN);

        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            // adjust when zuggenerator is implemented
            // moves.movesGenerator(board);
        }
        long stopTime = System.nanoTime();

        long averageTime = (stopTime - startTime) / 1000;
        System.out.println("Average time for generating moves for " + gamePosition + ":" + averageTime);
    }
}
