import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Character.isDigit;

public class BitBoard {
    static long counter = 0;
    public static void main(String[] args) {
        //importFEN("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 r");
        /*System.out.println("Initial evaluation: " + BitMoves.evaluatePosition(0, BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue));
        boolean isMax;
        while (!BitMoves.isGameFinished()) {
            if (BitBoardFigures.blueToMove){
                isMax = true;
            } else {
                isMax = false;
            }
            BitValueMoves vm = alphaBeta( isMax, 4);
            String move = vm.move;
            System.out.println("Move " + vm.move);
            System.out.println("Move made: " + moveToString(vm.move) + " on expected eval " + vm.v);
            BitMoves.makeMove(move, true);
            BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
            BitBoardFigures.blueToMove = !BitBoardFigures.blueToMove;
            System.out.println(BitMoves.isGameFinished());

        }*/

        for (int i = 0; i < 10; i++) {
            importFEN("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 r");
            boolean isMax;
            BitMoves.initZobristTable();

            while (!BitMoves.isGameFinished()) {
                isMax = BitBoardFigures.blueToMove;

                String move;
                if (isMax) {
                    //alpha beta plays blue
                    move = alphaBetaWithTransposition(isMax, 4).move;
                    System.out.println("alphaBeta is making the move: " + move);
                } else {
                    //mcts plays red
                    //timelimit in ms
                    move = mctsUCT(500);
                    System.out.println("mcts is making the move: " + move);
                }
                BitMoves.makeMove(move, true);
                //BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
                BitBoardFigures.blueToMove = !BitBoardFigures.blueToMove;
            }
            if(!BitBoardFigures.blueToMove) {
                System.out.println("Alpha Beta AI has won!");
            } else {
                System.out.println("MCTS AI has won!");
            }
            BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        }

    }


    static public BitValueMoves alphaBeta(boolean isMax, int depth){
        BitMoves.moveCounter += 1;
        int timeDepth = 4;
        long startTime = System.currentTimeMillis();
        if (BitMoves.aiRunningTime > 117000){
            BitValueMoves move = alphaBetaRecursion(2, -100000.0f, +100000.0f, isMax); //panic mode
            long endTime = System.currentTimeMillis();
            BitMoves.aiRunningTime += (endTime - startTime);
            return move;
        } else if (BitMoves.aiRunningTime > 110000){
            BitValueMoves move = alphaBetaRecursion(4, -100000.0f, +100000.0f, isMax); //slight panic mode
            long endTime = System.currentTimeMillis();
            BitMoves.aiRunningTime += (endTime - startTime);
            return move;
        } else if ((BitBoardFigures.blueToMove && BitMoves.blueFigureCount < 8) ||
                (!BitBoardFigures.blueToMove && BitMoves.redFigureCount < 8)){
            if (BitMoves.aiRunningTime > 100000){
                timeDepth = 9; //~3.5s or better in endgame
            } else {
                timeDepth = 10; //~13s or better in endgame
            }
        } else {
            timeDepth = 8; //~3.3s on full board
            if(BitMoves.moveCounter < 5){
                timeDepth = 6; //fast opening
                //timeDepth = 6; //TODO: DEBUG HERE AND COMMENT OUT
            }
        }

        BitValueMoves move = alphaBetaRecursion(timeDepth, -100000.0f, +100000.0f, isMax);
        long endTime = System.currentTimeMillis();
        BitMoves.aiRunningTime += (endTime - startTime);
        return move;
    }

    static public BitValueMoves alphaBetaWithTransposition(boolean isMax, int depth){
        BitMoves.transpositionTable = new HashMap<>();
        BitMoves.moveCounter += 1;
        int timeDepth = 4;
        long startTime = System.currentTimeMillis();
        if (BitMoves.aiRunningTime > 117000){
            BitValueMoves move = alphaBetaRecursion(2, -100000.0f, +100000.0f, isMax); //panic mode
            long endTime = System.currentTimeMillis();
            BitMoves.aiRunningTime += (endTime - startTime);
            return move;
        }
        if (BitMoves.aiRunningTime > 110000){
            BitValueMoves move = alphaBetaRecursion(4, -100000.0f, +100000.0f, isMax); //slight panic mode
            long endTime = System.currentTimeMillis();
            BitMoves.aiRunningTime += (endTime - startTime);
            return move;
        }
        if ((BitBoardFigures.blueToMove && BitMoves.blueFigureCount < 8) ||
                (!BitBoardFigures.blueToMove && BitMoves.redFigureCount < 8)){
            if (BitMoves.aiRunningTime > 100000){
                timeDepth = 9; //~3.5s or better in endgame
            } else {
                timeDepth = 12; //~13s or better in endgame
            }
        } else {
            timeDepth = 8; //~3.3s on full board
            if(BitMoves.moveCounter < 5){
                timeDepth = 6; //fast opening
                //timeDepth = 6; //TODO: DEBUG HERE AND COMMENT OUT
            }
        }

        BitValueMoves move = alphaBetaWithTranspositionTableRecursion(timeDepth, -100000.0f, +100000.0f, isMax);
        long endTime = System.currentTimeMillis();
        BitMoves.aiRunningTime += (endTime - startTime);
        return move;
    }

    static public BitValueMoves alphaBetaRecursion(int depth, float alpha, float beta, boolean isMax){
        if(depth == 0 || BitMoves.isGameFinished()){
//            System.out.println("copy: " + BitMoves.evaluatePosition(depth, SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue));
            return new BitValueMoves(BitMoves.evaluatePosition(depth, BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue), null, depth);
        }

        if (isMax){
            //float value = alpha;
            float value = -100000.0f;
            String bestMove = null;
            int bestDepth = depth;
            String moves;


            moves = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);


            for (int i=0; i<moves.length(); i+=4) {
                String makeMove = BitMoves.makeMove(moves.substring(i, i + 4), true);
//                System.out.println("Move make: " +makeMove);
//                BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
                BitValueMoves evaluation = alphaBetaRecursion(depth - 1, alpha, beta, false);
//                System.out.println("After move is done:" + moveToString(makeMove));
//                BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
                BitMoves.unmakeStack.push(makeMove);
                BitMoves.undoMove();
//                System.out.println("After undoing " + makeMove);
//                BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);

                if (evaluation.v > value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = moves.substring(i, i + 4);
                    bestDepth = evaluation.depth;
                }


                alpha = Math.max(alpha, value);

  /*              if (value >= beta) {
                    //System.out.println("break");
                    break;
                }*/

                if (alpha >= beta) {
                    //System.out.println("break");
                    break;
                }
            }

            return new BitValueMoves(value, bestMove, bestDepth);
        } else {
            //float value = beta;
            float value = 100000.0f;
            String bestMove = null;
            int bestDepth = depth;
            String moves;


            moves = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);


            for (int i=0; i<moves.length(); i+=4) {
                String makeMove = BitMoves.makeMove(moves.substring(i, i + 4), true);
//                System.out.println("Move make: " +makeMove);
//                BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);

                BitValueMoves evaluation = alphaBetaRecursion(depth - 1, alpha, beta, true);
//                System.out.println("After move is done: " + moveToString(makeMove));
//                BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
                BitMoves.unmakeStack.push(makeMove);
                BitMoves.undoMove();
//                System.out.println("After undoing " + makeMove);
//               BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);


                //value = Math.max(value, evaluation.v);
                if (evaluation.v < value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = moves.substring(i, i + 4);
                    bestDepth = evaluation.depth;
                }

                beta = Math.min(beta, value);

               /* if (value <= alpha) {
                    //System.out.println("break");
                    break;
                }

                */if (alpha >= beta) {
                    //System.out.println("break");
                    break;
                }

            }

            return new BitValueMoves(value, bestMove, bestDepth);
        }
    }

    static public BitValueMoves alphaBetaWithTranspositionTableRecursion(int depth, float alpha, float beta, boolean isMax){
        if(depth == 0 || BitMoves.isGameFinished()){
            return new BitValueMoves(BitMoves.evaluatePosition(depth, BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue), null, depth);
        }

        long hashedBoard = BitMoves.hashBoard();
        //if(BitMoves.elementOfTranspositionTable(hashedBoard) != null){
        //    return BitMoves.elementOfTranspositionTable(hashedBoard).bitValueMove;
        //}
        if(BitMoves.transpositionTable.containsKey(hashedBoard)){
            return BitMoves.transpositionTable.get(hashedBoard).bitValueMove;
        }

        if (isMax){
            float value = -100000.0f;
            String bestMove = null;
            int bestDepth = depth;
            String moves;


            moves = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);


            for (int i=0; i<moves.length(); i+=4) {
                String makeMove = BitMoves.makeMove(moves.substring(i, i + 4), true);
                //BitValueMoves evaluation = alphaBetaRecursion(depth - 1, alpha, beta, false);

                BitValueMoves evaluation = alphaBetaWithTranspositionTableRecursion(depth - 1, alpha, beta, false);

                BitMoves.unmakeStack.push(makeMove);
                BitMoves.undoMove();

                if (evaluation.v > value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = moves.substring(i, i + 4);
                    bestDepth = evaluation.depth;
                }

                alpha = Math.max(alpha, value);

                if (alpha >= beta) {
                    break;
                }
            }

            BitValueMoves bitValueMoves = new BitValueMoves(value, bestMove, bestDepth);
            hashedBoard = BitMoves.hashBoard(); //TODO: maybe remove
            BitMoves.addToTranspositionTable(hashedBoard, new TranspositionValues(bitValueMoves, alpha, beta));
            return bitValueMoves;

        } else {
            float value = 100000.0f;
            String bestMove = null;
            int bestDepth = depth;
            String moves;


            moves = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);


            for (int i=0; i<moves.length(); i+=4) {
                String makeMove = BitMoves.makeMove(moves.substring(i, i + 4), true);

                BitValueMoves evaluation = alphaBetaWithTranspositionTableRecursion(depth - 1, alpha, beta, true);

                BitMoves.unmakeStack.push(makeMove);
                BitMoves.undoMove();

                if (evaluation.v < value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = moves.substring(i, i + 4);
                    bestDepth = evaluation.depth;
                }

                beta = Math.min(beta, value);

                if (alpha >= beta) {
                    break;
                }

            }

            BitValueMoves bitValueMoves = new BitValueMoves(value, bestMove, bestDepth);
            hashedBoard = BitMoves.hashBoard(); //TODO: maybe remove
            BitMoves.addToTranspositionTable(hashedBoard, new TranspositionValues(bitValueMoves, alpha, beta));
            return bitValueMoves;

        }
    }

    //computationalBudget in ms
    static public String mctsUCT(long computationalBudget){
        String possibleMoves;
        BitMoves.mctsBlueToMove = BitBoardFigures.blueToMove;

        BitBoardFigures.mctsSingleRed = BitBoardFigures.SingleRed;
        BitBoardFigures.mctsDoubleRed = BitBoardFigures.DoubleRed;
        BitBoardFigures.mctsMixedRed = BitBoardFigures.MixedRed;
        BitBoardFigures.mctsSingleBlue = BitBoardFigures.SingleBlue;
        BitBoardFigures.mctsDoubleBlue = BitBoardFigures.DoubleBlue;
        BitBoardFigures.mctsMixedBlue = BitBoardFigures.MixedBlue;

        if(BitMoves.mctsBlueToMove){
            possibleMoves = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        } else {
            possibleMoves = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        }

        MCTSNode root = new MCTSNode("", possibleMoves, null);

        long endtime = System.currentTimeMillis() + computationalBudget;

        MCTSNode v_i;
        int playoutReward;

        while(System.currentTimeMillis() < endtime){
            BitMoves.mctsBlueToMove = BitBoardFigures.blueToMove;

            v_i = mctsTreePolicy(root);
            playoutReward = mctsDefaultPolicy();
            mctsBackup(v_i, playoutReward);
        }

        //get best move
        /*String bestMove;
        try {
            bestMove = root.children.entrySet().stream().reduce((entry1, entry2) -> entry1.getValue().getWinRate() > entry2.getValue().getWinRate() ? entry1 : entry2).get().getKey();
        } catch (NoSuchElementException e){
            return "";
        }

        return bestMove;*/

        return mctsBestChild(root, 0).sourceMove;
    }

    private static MCTSNode mctsTreePolicy(MCTSNode root){
        MCTSNode nodePointer = root;

        //while(!nodePointer.isTerminal){
        while(!BitMoves.isGameFinished()){
            if(!nodePointer.isExpanded){
                return mctsExpand(nodePointer);
            } else {
                nodePointer = mctsBestChild(nodePointer, Math.sqrt(2));
                String move = BitMoves.makeMove(nodePointer.sourceMove, true);
                //BitMoves.unmakeStack.push(move);
                BitMoves.mctsBlueToMove = !BitMoves.mctsBlueToMove;
            }
        }

        return nodePointer;
    }

    private static MCTSNode mctsExpand(MCTSNode root){
        String randomUntriedMove = root.getRandomPlayoutMove();

        randomUntriedMove = BitMoves.makeMove(randomUntriedMove, true);
        //BitMoves.unmakeStack.push(randomUntriedMove);
        BitMoves.mctsBlueToMove = !BitMoves.mctsBlueToMove;

        String possibleMoves;
        if(BitMoves.mctsBlueToMove){
            possibleMoves = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        } else {
            possibleMoves = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        }

        MCTSNode newChild = new MCTSNode(randomUntriedMove, possibleMoves, root);

        root.addChild(randomUntriedMove, newChild);
        return newChild;
    }

    private static MCTSNode mctsBestChild(MCTSNode node, double uct_c){
        try {
            return node.children.entrySet().stream().reduce((entry1, entry2) -> uctValue(entry1.getValue(), uct_c) > uctValue(entry2.getValue(), uct_c) ? entry1 : entry2).get().getValue();
        } catch (NoSuchElementException e){
            return null;
        }
    }

    private static double uctValue(MCTSNode node, double uct_c){
        return node.playoutsWon / node.playoutsSum + uct_c * Math.sqrt((Math.log(node.parent.playoutsSum / node.playoutsSum)));
    }

    private static int mctsDefaultPolicy(){
        String moves;
        while(!BitMoves.isGameFinished()){
            if (BitMoves.mctsBlueToMove){
                moves = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
            } else {
                moves = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
            }

            //select move uniformly at random
            //sometimes BitMoves.mctsBlueToMove is wrong, the try-catch fixes this bug
            int offset;
            try {
                offset = ThreadLocalRandom.current().nextInt(0, moves.length()/4);
            } catch (IllegalArgumentException ignored){
                BitMoves.mctsBlueToMove = !BitMoves.mctsBlueToMove;
                if (BitMoves.mctsBlueToMove){
                    moves = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
                } else {
                    moves = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
                }
                offset = ThreadLocalRandom.current().nextInt(0, moves.length()/4);
            }

            String randomMove = moves.substring(offset*4, offset*4+4);

            randomMove = BitMoves.makeMove(randomMove, true);
            //BitMoves.unmakeStack.push(randomMove);
            BitMoves.mctsBlueToMove = !BitMoves.mctsBlueToMove;
        }

        counter += 1;
        if(counter == Long.MAX_VALUE - 1) System.out.println("overflowing");

        if(BitMoves.mctsBlueStarted){
            if(BitMoves.blueWon){
                return 1;
            } else {
                //return -1;
                return 0;
            }
        } else {
            if(BitMoves.blueWon){
                //return -1;
                return 0;
            } else {
                return 1;
            }
        }
    }

    private static void mctsBackup(MCTSNode leaf, int reward){
        leaf.playoutsSum = 1;
        leaf.playoutsWon = reward;
        MCTSNode parent = leaf.parent;
        while(parent != null){
            parent.playoutsSum++;
            parent.playoutsWon += reward;
            parent = parent.parent;
        }

        BitBoardFigures.SingleRed = BitBoardFigures.mctsSingleRed;
        BitBoardFigures.SingleBlue = BitBoardFigures.mctsSingleBlue;
        BitBoardFigures.MixedRed = BitBoardFigures.mctsMixedRed;
        BitBoardFigures.MixedBlue = BitBoardFigures.mctsMixedBlue;
        BitBoardFigures.DoubleRed = BitBoardFigures.mctsDoubleRed;
        BitBoardFigures.DoubleBlue = BitBoardFigures.mctsDoubleBlue;

        /*while(!BitMoves.unmakeStack.isEmpty()){
            try {
                BitMoves.undoMove();
            } catch (StringIndexOutOfBoundsException e){
                //System.out.println(BitMoves.unmakeStack);
                break;
            }

            //drawArray(BitBoardFigures.SingleRed,BitBoardFigures.SingleBlue,BitBoardFigures.DoubleRed,BitBoardFigures.DoubleBlue,BitBoardFigures.MixedRed,BitBoardFigures.MixedBlue);

        }*/
        //importFEN("b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 b");



        /*try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
    }

    public static void drawArray(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        String[][] jumpBoard = new String[8][8];
        /*for (int i=0;i<64;i++) {
            jumpBoard[i/8][i%8]=" ";
        }*/

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                jumpBoard[i][j] = "  ";
            }
        }


        for (int i=0;i<64;i++) {
            int row = 7 - (i / 8); // Flip rows here
            int col = i % 8;

            if (((SingleRed>>i)&1)==1) {jumpBoard[row][col]="r0";}
            if (((SingleBlue>>i)&1)==1) {jumpBoard[row][col]="b0";}
            if (((DoubleRed>>i)&1)==1) {jumpBoard[row][col]="rr";}
            if (((DoubleBlue>>i)&1)==1) {jumpBoard[row][col]="bb";}
            if (((MixedRed>>i)&1)==1) {jumpBoard[row][col]="br";}
            if (((MixedBlue>>i)&1)==1) {jumpBoard[row][col]="rb";}
        }
        for (int i=0;i<8;i++) {
            String tmp = Arrays.toString(jumpBoard[i]);
            tmp = tmp.replace(",", " |");
            tmp = tmp.replace("[", "| ");
            tmp = tmp.replace("]", " |");
            System.out.print(8-i + " ");
            System.out.println(tmp);
        }
        System.out.println("    a    b    c    d    e    f    g    h");
    }

    public static void importFEN(String fenString) {
        //BitBoardFigures.SingleRed=0; BitBoardFigures.SingleBlue=0; BitBoardFigures.DoubleRed=0;
        //BitBoardFigures.DoubleBlue=0; BitBoardFigures.MixedRed=0; BitBoardFigures.MixedBlue=0;
        BitBoardFigures.SingleRed = 0;
        BitBoardFigures.SingleBlue = 0;
        BitBoardFigures.DoubleRed = 0;
        BitBoardFigures.DoubleBlue = 0;
        BitBoardFigures.MixedRed = 0;
        BitBoardFigures.MixedBlue = 0;

        BitMoves.redFigureCount = 0;
        BitMoves.blueFigureCount = 0;

        int charIndex = 0;
        int boardIndex = 0;
        while (fenString.charAt(charIndex) != ' ')
        {

            //System.out.println("BoardIndex: " + boardIndex);
            //System.out.println("Char " +  fenString.charAt(charIndex));
            if(boardIndex == 0 || boardIndex == 7 || boardIndex == 56 || boardIndex == 63){
//                System.out.println("BoardIndex if 0 or 7: " + boardIndex);
                boardIndex++;
                continue;
            }


            //System.out.println("Character at: " + fenString.charAt(charIndex) +  " " + charIndex);
            if (fenString.charAt(charIndex) == 'r') {
                BitMoves.redFigureCount += 1;
                charIndex++;
                switch (fenString.charAt(charIndex)){
                    case '0': BitBoardFigures.SingleRed |= (1L << boardIndex++);
                        charIndex++;
                        break;
                    case 'b': BitBoardFigures.MixedBlue |= (1L << boardIndex++);
                        charIndex++;
                        break;
                    case 'r': BitBoardFigures.DoubleRed |= (1L << boardIndex++);
                        charIndex++;
                        break;
                }
            }
            if (fenString.charAt(charIndex) == 'b') {
                BitMoves.blueFigureCount += 1;
                charIndex++;
                switch (fenString.charAt(charIndex)){
                    case '0': BitBoardFigures.SingleBlue |= (1L << boardIndex++);
                        charIndex++;
                        break;
                    case 'b': BitBoardFigures.DoubleBlue |= (1L << boardIndex++);
                        charIndex++;
                        break;
                    case 'r': BitBoardFigures.MixedRed |= (1L << boardIndex++);
                        charIndex++;
                        break;
                }
            }

            if(boardIndex == 0 || boardIndex == 7 || boardIndex == 56 || boardIndex == 63){
//                System.out.println("BoardIndex if 0 or 7: " + boardIndex);
                boardIndex++;
                continue;
            }

            if(Character.isDigit(fenString.charAt(charIndex))) {
                int skip = Character.getNumericValue(fenString.charAt(charIndex));
                boardIndex += skip;
                charIndex++;
            }

            if (fenString.charAt(charIndex) == '/'){
                charIndex++;
            }

        }
        BitBoardFigures.blueToMove = (fenString.charAt(++charIndex) == 'b');
        drawArray(BitBoardFigures.SingleRed,BitBoardFigures.SingleBlue,BitBoardFigures.DoubleRed,BitBoardFigures.DoubleBlue,BitBoardFigures.MixedRed,BitBoardFigures.MixedBlue);
    }

    static String moveToString(String move){
        if(move!= null) {
            int coltp = Character.getNumericValue(move.charAt(1));
            char start_col = (char) (coltp + 97);
            coltp = Character.getNumericValue(move.charAt(3));
            char end_col = (char) (coltp + 97);
            int start_row = Character.getNumericValue(move.charAt(0)) + 1;
            int end_row = Character.getNumericValue(move.charAt(2)) + 1;

            return "" + start_col + start_row + "-" + end_col + end_row;
        }else return move;
    }

    /*public static void deepCopyBoard(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        SingleRedCopy = SingleRed;
        SingleBlueCopy = SingleBlue;
        DoubleRedCopy = DoubleRed;
        DoubleBlueCopy = DoubleBlue;
        MixedRedCopy = MixedRed;
        MixedBlueCopy= MixedBlue;
    }

    public static void deepCopyFigures(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        BitBoardFigures.SingleRed = SingleRed;
        BitBoardFigures.SingleBlue = SingleBlue;
        BitBoardFigures.DoubleRed = DoubleRed;
        BitBoardFigures.DoubleBlue = DoubleBlue;
        BitBoardFigures.MixedRed = MixedRed;
        BitBoardFigures.MixedBlue= MixedBlue;
    }
     */
    /*public static void initiateBoard(){
        long SingleRed = 0L, SingleBlue = 0L, DoubleRed = 0L, DoubleBlue = 0L, MixedRed = 0L, MixedBlue = 0L;

        String[][] jumpBoard ={

                {"00","01","02","03","04","05","06","07"},
                {"08","09","10","11","12","13","14","15"},
                {"16","17","18","19","20","21","22","23"},
                {"24","25","26","27","28","29","30","31"},
                {"32","33","34","35","36","37","38","39"},
                {"40","41","42","43","44","45","46","47"},
                {"48","49","50","51","52","53","54","55"},
                {"56","57","58","59","60","61","62","63"}};



                {"0L","1L","2L","03","04","05","06","07"},
                {"08","09","10","11","12","13","14","15"},
                {"16","17","18","19","20","21","22","23"},
                {"24","25","26","27","28","29","30","31"},
                {"32","33","34","35","36","37","38","39"},
                {"40","41","42","43","44","45","46","47"},
                {"48","49","50","51","52","53","54","55"},
                {"56","57","58","59","60","61","62","63"}};


                {"","","","","","","",""},
                {"","","","","","","",""},
                {"","","","","","","",""},
                {"","","",""," "," ",""," "},
                {"","",""," ","","","",""},
                {"rr","rr","rr","rr","rr","rr","rr","rr"},
                {"rr","rr","rr","rr","rr","rr","rr","rr"},
                {"","","","","","","",""}};
        arrayToBitboards(jumpBoard,SingleRed,SingleBlue,DoubleRed,DoubleBlue,MixedRed,MixedBlue);
    }

        public static void arrayToBitboards(String [][] jumpBoard, long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue){
        String Binary;
        for (int i=0;i<64;i++) {
            Binary="0000000000000000000000000000000000000000000000000000000000000000";
            Binary=Binary.substring(i+1)+"1"+Binary.substring(0, i);
            switch (jumpBoard[i/8][i%8]) {
                case "r0": SingleRed+=convertStringToBitboard(Binary);
                    break;
                case "b0": SingleBlue+=convertStringToBitboard(Binary);
                    break;
                case "rr": DoubleRed+=convertStringToBitboard(Binary);
                    break;
                case "bb": DoubleBlue+=convertStringToBitboard(Binary);
                    break;
                case "rb": MixedBlue+=convertStringToBitboard(Binary);
                    break;
                case "br": MixedRed+=convertStringToBitboard(Binary);
                    break;
            }
        }
        drawArray(SingleRed,SingleBlue,DoubleRed,DoubleBlue,MixedRed,MixedBlue);
        BitBoardFigures.SingleRed=SingleRed; BitBoardFigures.SingleBlue=SingleBlue; BitBoardFigures.DoubleRed=DoubleRed;
        BitBoardFigures.DoubleBlue=DoubleBlue; BitBoardFigures.MixedRed=MixedRed; BitBoardFigures.MixedBlue=MixedBlue;
    }

        private static long convertStringToBitboard(String Binary) {
        if (Binary.charAt(0)=='0') {//not going to be a negative number
            return Long.parseLong(Binary, 2);
        } else {
            return Long.parseLong("1"+Binary.substring(2), 2)*2;
        }
    }

    */
}
