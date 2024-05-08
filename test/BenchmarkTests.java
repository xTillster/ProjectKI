import java.util.Set;


public class BenchmarkTests {
    public static void main(String[] args) {
        String testGame  = "b0b04/b02bb2b01/2b05/4rb3/6b01/2r04r0/1r01r0r01r01/r0r04 r";
        String startGame = "0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 b";
        String midGame   = "2b01b01/2bb5/3b02b01/3r0brb0r01/1b06/8/2r0rr4/2r01r0r0 r";
        String endGame   = "6/1b06/1r0b02bb2/2r02b02/8/5rr2/2r03r01/6 b";

        benchmarkTests("Test", testGame);
        //benchmarkTests("Start Game", startGame);
        //benchmarkTests("Mid Game", midGame);
        //benchmarkTests("End Game", endGame);

    }
    private static void benchmarkTests(String gamePosition, String FEN) {
        String fen = FEN.substring(0, FEN.length() - 2);
        Board.fenToBoard(fen);

        Board.blueToMove = (FEN.charAt(FEN.length()-1) == 'b' ? 1 : 0);
        Board.boardToString();

        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            Set<Move> moves = Board.getPossibleMoves();
            moves = Board.getLegalMoves(moves);
        }


        long stopTime = System.nanoTime();

        long averageTime = (stopTime - startTime) / (1000);
        System.out.println("Average time for generating moves for " + gamePosition + ": " + averageTime + " nanoseconds");
    }
}
