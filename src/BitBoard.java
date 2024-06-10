import java.awt.geom.QuadCurve2D;
import java.util.Arrays;

import static java.lang.Character.isDigit;

public class BitBoard {
    static long SingleRedCopy = 0;
    static long SingleBlueCopy = 0;
    static long DoubleRedCopy = 0;
    static long DoubleBlueCopy = 0;
    static long MixedRedCopy = 0;
    static long MixedBlueCopy = 0;

    public static void main(String[] args) {
        importFEN("2b01b01/2bb5/3b02b01/3r0brb0r01/1b06/8/2r0rr4/2r01r0r0 b");
        deepCopyBoard(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        System.out.println("initiate evaluation: " + BitMoves.evaluatePosition(0, BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue));
        boolean isMax = true;
        while (!BitMoves.isGameFinished()) {

            if (BitBoardFigures.blueToMove){
                isMax = true;
            } else {
                isMax = false;
            }

            BitValueMoves vm = alphaBeta(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue, isMax, 4);

            String move = vm.move;
            System.out.println("Move made: " + vm.move + " on expected eval " + vm.v);
            BitMoves.makeMove(move, true);
            deepCopyFigures(SingleRedCopy, SingleBlueCopy, DoubleRedCopy, DoubleBlueCopy, MixedRedCopy, MixedBlueCopy);
            BitBoardFigures.blueToMove = !BitBoardFigures.blueToMove;

        }


    }

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
    */

    static public BitValueMoves alphaBeta(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue, boolean isMax, int depth){
        return alphaBetaRecursion(SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue, depth, -100000.0f, +100000.0f, isMax);
    }

    static public BitValueMoves alphaBetaRecursion(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue, int depth, float alpha, float beta, boolean isMax){
        if(depth == 0 || BitMoves.isGameFinished()){
//            System.out.println("bitboard: " + BitMoves.evaluatePosition(depth, BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue));
//            System.out.println("copy: " + BitMoves.evaluatePosition(depth, SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue));
            return new BitValueMoves(BitMoves.evaluatePosition(depth, SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue ), null, depth);
        }

        if (isMax){
            //float value = alpha;
            float value = -100000.0f;
            String bestMove = null;
            int bestDepth = depth;
            String moves;

            if (BitBoardFigures.blueToMove){
                moves = BitMoves.possibleMovesBlue(SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue);
                System.out.println(moves);
            }else{
                moves = BitMoves.possibleMovesRed(SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue);
                System.out.println(moves);
            }

            for (int i=0; i<moves.length()-4; i+=4) {
                deepCopyBoard(SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue);
                BitMoves.makeMove(moves.substring(i, i + 4), true);

                BitValueMoves evaluation = alphaBetaRecursion(SingleRedCopy, SingleBlueCopy, DoubleRedCopy, DoubleBlueCopy, MixedRedCopy, MixedBlueCopy,depth - 1, alpha, beta, false);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v > value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = moves.substring(i, i + 4);
                    bestDepth = evaluation.depth;
                }

                alpha = Math.max(alpha, value);

                if (value >= beta) {
                    //System.out.println("break");
                    break;
                }

                /*if (alpha >= beta) {
                    //System.out.println("break");
                    break;
                }*/
            }
            return new BitValueMoves(value, bestMove, bestDepth);
        } else {
            //float value = beta;
            float value = 100000.0f;
            String bestMove = null;
            int bestDepth = depth;
            String moves;

            if (BitBoardFigures.blueToMove){
                moves = BitMoves.possibleMovesBlue(SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue);
            }else{
                moves = BitMoves.possibleMovesBlue(SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue);
            }

            for (int i=0; i<moves.length()-4; i+=4) {
                deepCopyBoard(SingleRed, SingleBlue, DoubleRed, DoubleBlue, MixedRed, MixedBlue);
                BitMoves.makeMove(moves.substring(i, i + 4), true);

                BitValueMoves evaluation = alphaBetaRecursion(SingleRedCopy, SingleBlueCopy, DoubleRedCopy, DoubleBlueCopy, MixedRedCopy, MixedBlueCopy,depth - 1, alpha, beta, false);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v < value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = moves.substring(i, i + 4);
                    bestDepth = evaluation.depth;
                }

                beta = Math.min(beta, value);

                if (value <= alpha) {
                    //System.out.println("break");
                    break;
                }

                /* if (alpha >= beta) {
                    //System.out.println("break");
                    break;
                }*/

            }
            return new BitValueMoves(value, bestMove, bestDepth);
        }
    }

    public static void deepCopyBoard(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        SingleRedCopy = SingleRed;
        SingleBlueCopy = SingleBlue;
        DoubleRedCopy = DoubleRed;
        DoubleBlueCopy = DoubleBlue;
        MixedRedCopy = MixedRed;
        MixedBlueCopy= MixedBlue;
    }

    public static void deepCopyFigures(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        System.out.println(BitBoardFigures.SingleRed);
        BitBoardFigures.SingleRed = SingleRed;
        System.out.println(SingleRed);
        System.out.println(BitBoardFigures.SingleRed);
        BitBoardFigures.SingleBlue = SingleBlue;
        BitBoardFigures.DoubleRed = DoubleRed;
        BitBoardFigures.DoubleBlue = DoubleBlue;
        BitBoardFigures.MixedRed = MixedRed;
        BitBoardFigures.MixedBlue= MixedBlue;
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

    public static void drawArray(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        String[][] jumpBoard = new String[8][8];
        for (int i=0;i<64;i++) {
            jumpBoard[i/8][i%8]=" ";
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
            System.out.println(Arrays.toString(jumpBoard[i]));
        }
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


}
