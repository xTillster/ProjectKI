public class BitBoardBenchmarkTests {
    public static void main(String[] args) {

/*
        evaluationTestsDepth1("Start Game", startGame);
        evaluationTestsDepth1("Mid Game", midGame);
        evaluationTestsDepth1("End Game", endGame);

        miniMaxTest("Start Game", startGame, 2);
        miniMaxTest("Start Game", startGame, 3);
        miniMaxTest("Start Game", startGame, 4);

        miniMaxTest("Mid Game", midGame, 2);
        miniMaxTest("Mid Game", midGame, 3);
        miniMaxTest("Mid Game", midGame, 4);

        miniMaxTest("End Game", endGame, 2);
        miniMaxTest("End Game", endGame, 3);
        miniMaxTest("End Game", endGame, 4);

 */
        BitBoard.importFEN("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 b");
        alphaBetaTest("Start Game",2);
        alphaBetaTest("Start Game",3);
        alphaBetaTest("Start Game",4);

        BitBoard.importFEN("2b01b01/2bb5/3b02b01/3r0brb0r01/1b06/8/2r0rr4/2r01r0r0 r");
        alphaBetaTest("Mid Game",2);
        alphaBetaTest("Mid Game",3);
        alphaBetaTest("Mid Game",4);

        BitBoard.importFEN("6/1b06/1r0b02bb2/2r02b02/8/5rr2/2r03r01/6 b");
        alphaBetaTest("End Game",2);
        alphaBetaTest("End Game",3);
        alphaBetaTest("End Game",4);

        /*
        benchmarkTests("Test", testGame);
        benchmarkTests("Start Game", startGame);
        benchmarkTests("Mid Game", midGame);
        benchmarkTests("End Game", endGame);
         */

    }

    /*
        private static void evaluationTestsDepth1(String gamePosition, Board board){
            boolean isMax = BitBoardFigures.blueToMove;


            long startTime = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                Board.miniMax(board, isMax, 1);
            }
            long stopTime = System.nanoTime();

            float averageTime = (float) (stopTime - startTime) / (10000*1000000);
            System.out.println("Average time for Evaluation " + gamePosition + ": " + averageTime + " ms \n"
                    + "Best move: " + Board.miniMax(board, true, 1).move + "\n");

        }



        private static void miniMaxTest(String gamePosition, Board board, int depth){
            Board.counter = 0;
            boolean isMax = board.blueToMove == 1;


            long startTime = System.nanoTime();
            Board.miniMax(board, isMax, depth);
            long stopTime = System.nanoTime();

            float totalTime = (float) (stopTime - startTime) / (1000000);
            System.out.println("Total time for Minimax for Depth " + depth + " in " + gamePosition + ": " + totalTime + " ms\n" +"Number of visited states in total: " + Board.counter + "\n" + "Number of visited states pro ms: " + Board.counter/(totalTime) + "\n" +
                    "Best move: " + Board.miniMax(board, isMax, depth).move + "\n");

        }
    */
    private static void alphaBetaTest(String gamePosition, int depth) {
        BitMoves.counter = 0;

        long startTime = System.nanoTime();

            boolean isMax = BitBoardFigures.blueToMove;

            BitValueMoves vm = BitBoard.alphaBeta(isMax, 4);

            long stopTime = System.nanoTime();

            float totalTime = (float) (stopTime - startTime) / (1000000);
            System.out.println("Total time for AlphaBeta for Depth " + depth + " in " + gamePosition + ": " + totalTime + " ms\n" + "Number of visited states in total: " + BitMoves.counter + "\n"
                    + "Number of visited states pro ms: " + BitMoves.counter / (totalTime) + "\n" + "Best move: " + vm.move + "\n");

        }
    }



    /*
    private static void benchmarkTests(String gamePosition, String FEN) {
        String fen = FEN.substring(0, FEN.length() - 2);
        Board.fenToBoard(fen);

        //Board.blueToMove = (FEN.charAt(FEN.length()-1) == 'b' ? 1 : 0);
        Board.boardToString();

        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {

            //Board.getPossibleMoves(Board.blueToMove);
        }


        long stopTime = System.nanoTime();

        long averageTime = (stopTime - startTime) / (1000);
        System.out.println("Average time for generating moves for " + gamePosition + ": " + averageTime + " nanoseconds");
    }

     */

