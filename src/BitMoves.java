import java.util.Arrays;

import static java.lang.Long.bitCount;

public class BitMoves {
    static long RED_CAPTURE;
    static long RED_ROOK;
    static long RED_NON_CAPTURE;
    static long BLUE_CAPTURE;
    static long BLUE_NON_CAPTURE;
    static long BLUE_ROOK;
    static long EMPTY_BLUE;
    static long EMPTY_RED;
    static long EMPTY_KNIGHT_RED;
    static long EMPTY_KNIGHT_BLUE;
    static long FILE_A = 36170086419038336L;
    static long FILE_AB = 217020518514230019L;
    static long FILE_GH = -4557430888798830400L;
    static long FILE_H = 72340172838076672L;
    static int i = 0;
    static boolean colorRed = true;
    static boolean capture = false;

    public static void main(String[] args) {
//      BitBoard.initiateBoard();
        BitBoard.importFEN("5r0/6bb1/8/8/5b02/8/rr7/6 r");
//      String a = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        String b = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
//      System.out.println(BitBoardFigures.SingleRed);
        System.out.println(b);
//      makeMove(b, true);
        isGameFinished();
        Float c = evaluatePosition(1, BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);

    }
    //TODO: capture kontrollieren; Bits zusammenfügen

    public static float evaluatePosition(int depth, long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue){
        float value= 0;

        value += bitCount(SingleBlue) * 10;
        for (int i = Long.numberOfTrailingZeros(SingleBlue); i < 64 - Long.numberOfLeadingZeros(SingleBlue); i++) {
            if (((SingleBlue >> i) & 1) == 1) {
                value += (float) ((i/8+1)*2.5);
            }
        }
        value += bitCount(DoubleBlue) * 15;
        for (int i = Long.numberOfTrailingZeros(DoubleBlue); i < 64 - Long.numberOfLeadingZeros(DoubleBlue); i++) {
            if (((SingleBlue >> i) & 1) == 1) {
                value += (float) ((i/8+1)*2.5);
            }
        }
        value += bitCount(MixedBlue) * 20;
        for (int i = Long.numberOfTrailingZeros(MixedBlue); i < 64 - Long.numberOfLeadingZeros(MixedBlue); i++) {
            if (((SingleBlue >> i) & 1) == 1) {
                value += (float) ((i/8+1)*2.5);
            }
        }
        value -= bitCount(SingleRed) * 10;
        for (int i = Long.numberOfTrailingZeros(SingleRed); i < 64 - Long.numberOfLeadingZeros(SingleRed); i++) {
            if (((SingleBlue >> i) & 1) == 1) {
                value += (float) ((8-i/8)*2.5);
            }
        }
        value -= bitCount(DoubleRed) * 15;
        for (int i = Long.numberOfTrailingZeros(DoubleRed); i < 64 - Long.numberOfLeadingZeros(DoubleRed); i++) {
            if (((SingleBlue >> i) & 1) == 1) {
                value += (float) ((8-i/8)*2.5);
            }
        }
        value -= bitCount(MixedRed) * 20;
        for (int i = Long.numberOfTrailingZeros(MixedRed); i < 64 - Long.numberOfLeadingZeros(MixedRed); i++) {
            if (((SingleBlue >> i) & 1) == 1) {
                value += (float) ((8-i/8)*2.5);
            }
        }
        return value;
    }



    public static String possibleMovesBlue(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        BLUE_CAPTURE = SingleRed | DoubleRed | MixedRed;
        BLUE_NON_CAPTURE = ~(DoubleBlue | MixedBlue);
        BLUE_ROOK = ~(SingleBlue);
        EMPTY_BLUE = ~(SingleRed | DoubleRed | MixedRed | DoubleBlue | MixedBlue);
        EMPTY_KNIGHT_BLUE = ~(DoubleBlue | MixedBlue);
        return possibleMovesSB(SingleBlue)+possibleMovesNB(MixedBlue)+possibleMovesNB(DoubleBlue);
    }

    public static String possibleMovesRed(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        RED_CAPTURE = SingleBlue | DoubleBlue | MixedBlue;
        RED_NON_CAPTURE = ~(DoubleRed | MixedRed);
        RED_ROOK = ~(SingleRed);
        EMPTY_RED = ~(SingleBlue | DoubleRed | MixedRed | DoubleBlue | MixedBlue);
        EMPTY_KNIGHT_RED = ~(DoubleRed | MixedRed);
        return possibleMovesSR(SingleRed)+possibleMovesNR(MixedRed)+possibleMovesNR(DoubleRed);
    }

    // Erklärung: i ist die Position, auf die die Figur kann z.B. 51, dann kann die Figur auf das Feld 51
    // Felder werden von oben gezählt = erste Reihe beginnt bei 0
    // es werden immer 4 Zahlen zu dem String hinzugefügt für jeden möglichen Move, die das Feld beschreiben
    // erste Zahl: Reihe des Startzustandes (bei 0 angefangen, von oben nach unten gezählt)
    // zweite Zahl: Feld von links Startzustand
    // dritte Zahl: Abstand nach oben Zielfeldes
    // vierte Zahl: Abstand links Zielfeld
    private static String possibleMovesSR(long singleRed) {
        String singleRedMoves = "";
        //Capture right
        long SINGLERED_MOVES = (singleRed >> 7) & RED_CAPTURE & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Capture right " +i);
                singleRedMoves += "" + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);
                //               System.out.println(singleRedMoves);
            }
        }
        //Capture left
        SINGLERED_MOVES = (singleRed >> 9) & RED_CAPTURE & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Capture left " + i);
                singleRedMoves += "" + (i / 8 + 1) + (i % 8 + 1) + (i / 8) + (i % 8);
//                System.out.println(singleRedMoves);
            }
        }
        //Move 1 forward
        //128L für Feld 7(nicht stehen auf Feld 8)
        //TODO: Problem für Feld 0, da 0L nicht geht
        SINGLERED_MOVES =(singleRed >> 8) & EMPTY_RED & ~128L;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Move forward " +i);
                singleRedMoves += "" + (i / 8 + 1) + (i % 8) + (i / 8) + (i % 8);
//                System.out.println(singleRedMoves);
            }
        }
        //Move 1 left
        //~-9187201950435737472L nicht File A
        //~72057594037927936L für Ecke 56 (nicht stehen auf Feld 57)
        SINGLERED_MOVES = (singleRed >>1) & EMPTY_RED & ~-9187201950435737472L & ~72057594037927936L;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 left " +i);
                singleRedMoves += "" + (i / 8) + (i % 8 + 1) + (i / 8) + (i % 8);
//                System.out.println(singleRedMoves);
            }
        }
        //Move 1 right
        // ~72340172838076672L nicht File H
        //~-9223372036854775808L für Ecke 63 (nicht stehen auf Feld 62)
        SINGLERED_MOVES = (singleRed << 1) & EMPTY_RED & ~72340172838076672L & ~-9223372036854775808L;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 right " +i);
                singleRedMoves += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
//                System.out.println(singleRedMoves);
            }
        }
        return singleRedMoves;
    }

    private static String possibleMovesNR(long knightRed) {
        String knightRedMoves = "";
        //2 left 1 up
        //1L für Ecke 0, Feld 8
        long KNIGHTRED_MOVES = (knightRed >> 10)& EMPTY_KNIGHT_RED & ~FILE_GH & ~1L;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("2 left 1 up " +i);
                knightRedMoves += "" + (i / 8 + 1) + (i % 8 + 2) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }
        //1L right 2 up
        //~128L für Ecke 7 (Feld 22)
        //~1L für Reihe 1 -> kann da nicht stehen
        KNIGHTRED_MOVES = (knightRed >> 15) & EMPTY_KNIGHT_RED & ~FILE_H & ~128L &~1L;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("1 right 2 up " + i);
                knightRedMoves += "" + (i / 8 + 2) + (i % 8 - 1) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }
        //1 left 2 up
        //~1L für Ecke 0, Feld 17
        KNIGHTRED_MOVES = (knightRed >> 17) & EMPTY_KNIGHT_RED & ~FILE_A & ~1L;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("1 left 2 up " +i);
                knightRedMoves += "" + (i / 8 + 2) + (i % 8 + 1) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }
        //2 right 1 up
        //~128L für Ecke 7, Feld 22
        KNIGHTRED_MOVES = (knightRed >> 6) & EMPTY_RED & ~FILE_AB &~128L;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("Move 2 right 1 up " +i);
                knightRedMoves += "" + (i / 8 + 1) + (i % 8 - 2) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }

        return knightRedMoves;
    }

    private static String possibleMovesSB(long singleRed) {
        String singleBlueMoves = "";

        //Capture right
        long SINGLEBLUE_MOVES = (singleRed << 7) & BLUE_CAPTURE & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Capture right: " + i);
                singleBlueMoves += "" + (i / 8 - 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        //Capture left
        SINGLEBLUE_MOVES = (singleRed << 9) & BLUE_CAPTURE & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Capture right: " + i);
                singleBlueMoves += "" + (i / 8 - 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 forward
        //~72057594037927936L für Ecke 56
        //-9223372036854775808 für Ecke 63
        SINGLEBLUE_MOVES = (singleRed << 8) & EMPTY_BLUE & ~-9223372036854775808L&~72057594037927936L;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Move forward: " + i);
                singleBlueMoves += "" + (i / 8 - 1) + (i % 8) + (i / 8) + (i % 8);
            }
        }
        //Move 1 right
        //~128L für ist die Ecke auf Feld 7
        SINGLEBLUE_MOVES = (singleRed << 1) & EMPTY_BLUE & ~FILE_H &~128L;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 right: " + i);
                singleBlueMoves += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 left
        //~1L für ist die Ecke auf Feld 0
        SINGLEBLUE_MOVES = (singleRed >> 1) & EMPTY_BLUE & ~FILE_A & ~1L;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 left: " + i);
                singleBlueMoves += "" + (i / 8) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        return singleBlueMoves;
    }

    private static String possibleMovesNB(long knightBlue) {
        String knightBlueMoves = "";
        //2 left 1 down
        //~72057594037927936L für Ecke 56, Feld 50
        long KNIGHTRED_MOVES = (knightBlue << 6)& EMPTY_KNIGHT_BLUE & ~FILE_GH& ~72057594037927936L ;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("2 left 1 down " +i);
                knightBlueMoves += "" + (i / 8 - 1) + (i % 8+2) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }
        //1 left 2 down
        //~36170086419038336 nicht in file a und b
        // 72057594037927936 für Ecke 56, Feld 41
        KNIGHTRED_MOVES = (knightBlue << 15) & EMPTY_KNIGHT_BLUE & ~36170086419038336L & ~72057594037927936L;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("1 left 2 down " + i);
                knightBlueMoves += "" + (i / 8-2) + (i % 8 + 1) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }
        //1 right 2 down
        //~-9223372036854775808L für Ecke 63, Feld 46
        KNIGHTRED_MOVES = (knightBlue << 17) & EMPTY_KNIGHT_BLUE & ~FILE_A & ~-9223372036854775808L;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("1 right 2 down " +i);
                knightBlueMoves += "" + (i / 8 - 2) + (i % 8 - 1) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }
        //2 right 1 down
        KNIGHTRED_MOVES = (knightBlue << 10) & EMPTY_BLUE & ~FILE_AB & ~-9223372036854775808L;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("2 right 1 down " +i);
                knightBlueMoves += "" + (i / 8 - 1) + (i % 8 -2 ) + (i / 8) + (i % 8);
//                System.out.println(knightRedMoves);
            }
        }

        return knightBlueMoves;
    }

    public static void makeMove(String moves, boolean start){
        //startfeld
        if (start) {
            BitMoves.makeStartMove(BitBoardFigures.SingleRed, moves.substring(i, i + 4), 'S');
            BitMoves.makeStartMove(BitBoardFigures.SingleBlue, moves.substring(i, i + 4), 's');
            BitMoves.makeStartMove(BitBoardFigures.DoubleRed, moves.substring(i, i + 4), 'D');
            BitMoves.makeStartMove(BitBoardFigures.DoubleBlue, moves.substring(i, i + 4), 'd');
            BitMoves.makeStartMove(BitBoardFigures.MixedRed, moves.substring(i, i + 4), 'M');
            BitMoves.makeStartMove(BitBoardFigures.MixedBlue, moves.substring(i, i + 4), 'm');

            //Zielfeld für capture Moves
        }else{
            BitMoves.makeEndCaptureMove(BitBoardFigures.SingleRed, moves, 'S');
            BitMoves.makeEndCaptureMove(BitBoardFigures.SingleBlue, moves, 's');
            BitMoves.makeEndCaptureMove(BitBoardFigures.DoubleRed, moves, 'D');
            BitMoves.makeEndCaptureMove(BitBoardFigures.DoubleBlue, moves, 'd');
            BitMoves.makeEndCaptureMove(BitBoardFigures.MixedRed, moves, 'M');
            BitMoves.makeEndCaptureMove(BitBoardFigures.MixedBlue, moves, 'm');
        }
        //wenn auf dem Zielfeld kein capture stattgefunden hat
        if (!capture){
            makeEndMove(moves);
        }
    }

    public static void makeStartMove(long board, String move, char type) {
        int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
        //if statement is only true, when the current bitboard is occupied at the start position
        if (((board>>>start)&1)==1) {
            switch(type){
                case 'S':
                    BitBoardFigures.SingleRed&=~(1L<<start);
                    makeMove(move, false);
                    break;
                case 'D':
                    BitBoardFigures.DoubleRed&=~(1L<<start);
                    BitBoardFigures.SingleRed|=(1L<<start);
                    makeMove(move, false);
                    break;
                case 'M': BitBoardFigures.MixedRed&=~(1L<<start);
                    BitBoardFigures.SingleBlue|=(1L<<start);
                    makeMove(move, false);
                    break;
                case 's': BitBoardFigures.SingleBlue&=~(1L<<start);
                    colorRed= false;
                    makeMove(move, false);
                    break;
                case 'd': BitBoardFigures.DoubleBlue&=~(1L<<start);
                    BitBoardFigures.SingleBlue|=(1L<<start);
                    colorRed= false;
                    makeMove(move, false);
                    break;
                case 'm': BitBoardFigures.MixedBlue&=~(1L<<start);
                    BitBoardFigures.SingleRed|=(1L<<start);
                    colorRed= false;
                    makeMove(move, false);
                    break;
            }
        }
    }

    public static void makeEndCaptureMove(long board, String move, char type) {
        int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
        //if statement is only true, when the current bitboard is occupied at the end position
        if ((((board>>>end)&1)==1)&&colorRed) {
            switch(type){
                case 'S': BitBoardFigures.SingleRed&=~(1L<<end);
                    BitBoardFigures.DoubleRed|=(1L<<end);
                    capture = true;
                    break;
                case 's': BitBoardFigures.SingleBlue&=~(1L<<end);
                    capture = true;
                    break;
                case 'd': BitBoardFigures.DoubleBlue&=~(1L<<end);
                    BitBoardFigures.MixedRed|=(1L<<end);
                    capture = true;
                    break;
                case 'm': BitBoardFigures.MixedBlue&=~(1L<<end);
                    BitBoardFigures.DoubleRed|=(1L<<end);
                    capture = true;
                    break;
            }
        }else if((((board>>>end)&1)==1)&&!colorRed) {
            System.out.println("capture blue");
            switch(type){
                case 'S': BitBoardFigures.SingleRed&=~(1L<<end);
                    capture = true;
                    break;
                case 'D': BitBoardFigures.DoubleRed&=~(1L<<end);
                    BitBoardFigures.MixedBlue|=(1L<<end);
                    capture = true;
                    break;
                case 'M': BitBoardFigures.MixedRed&=~(1L<<end);
                    BitBoardFigures.DoubleBlue|=(1L<<end);
                    capture = true;
                    break;
                case 's': BitBoardFigures.SingleBlue&=~(1L<<end);
                    BitBoardFigures.DoubleBlue|=(1L<<end);
                    capture = true;
                    break;
            }
        }
    }

    public static void makeEndMove(String move) {
        int end = (Character.getNumericValue(move.charAt(2)) * 8) + (Character.getNumericValue(move.charAt(3)));
        if (colorRed){
            BitBoardFigures.SingleRed|=(1L<<end);
        }else{
            BitBoardFigures.SingleBlue|=(1L<<end);
        }
    }

    public static void drawBitboard(long bitBoard) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]="";
        }
        for (int i=0;i<64;i++) {
            if (((bitBoard>>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
            if ("".equals(chessBoard[i/8][i%8])) {chessBoard[i/8][i%8]=" ";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

    public static void isGameFinished(){
        //No more red pieces on board
        if(BitBoardFigures.SingleRed == 0 && BitBoardFigures.MixedRed == 0 && BitBoardFigures.DoubleRed == 0){
            System.out.println("Game won by Blue.");
            return;
        }
        //No more blue pieces on board
        else if(BitBoardFigures.SingleBlue == 0 && BitBoardFigures.MixedBlue == 0 && BitBoardFigures.DoubleBlue == 0){
            System.out.println("Game won by Red.");
            return;
        }
        //No more moves for player who is am zug
        if (BitBoardFigures.blueToMove) {
            String moves = possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
            if(moves.isEmpty()){
                System.out.println("Game won by Red.");
                return;
            }
        }
        else{
            String moves = possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
            if(moves.isEmpty()){
                System.out.println("Game won by Blue.");
                return;
            }
        }

        // Constants for specific bitboard configurations
        final long BLUE_WIN_CONDITION = 9079256848778919936L; // Binary number with the first 6 bits as 1
        final long RED_WIN_CONDITION = 126L; // Specific condition for red


        // Check if blue on red home row
        if ((BitBoardFigures.SingleBlue & BLUE_WIN_CONDITION) != 0 || (BitBoardFigures.MixedBlue & BLUE_WIN_CONDITION) != 0 || (BitBoardFigures.DoubleBlue & BLUE_WIN_CONDITION) != 0){
            System.out.println("Game won by Blue.");
            return;
        }
        // Check if blue on red home row
        if ((BitBoardFigures.SingleRed & RED_WIN_CONDITION) != 0 || (BitBoardFigures.MixedRed & RED_WIN_CONDITION) != 0 || (BitBoardFigures.DoubleRed & RED_WIN_CONDITION) != 0){
            System.out.println("Game won by Red.");
        }

    }

    /*long  SingleRedt  = BitMoves.makeMove(BitBoardFigures.SingleRed, moves.substring(i, i + 4), 'S'), SingleBluet = BitMoves.makeMove(BitBoardFigures.SingleBlue, moves.substring(i, i + 4), 'S'),
            DoubleRedt = BitMoves.makeMove(BitBoardFigures.DoubleRed, moves.substring(i, i + 4), 'B'), DoubleBluet = BitMoves.makeMove(BitBoardFigures.DoubleBlue, moves.substring(i, i + 4), 'R'),
            MixedRedt  = BitMoves.makeMove(BitBoardFigures.MixedRed, moves.substring(i, i + 4), 'Q'), MixedBluet  = BitMoves.makeMove(BitBoardFigures.MixedBlue, moves.substring(i, i + 4), 'K');



    public static void makeMoveFEN(String fen, String moves, String type) {
        BitBoard.importFEN(fen);
        long SingleRedt = 0;
        long SingleBluet = 0;
        long DoubleRedt = 0;
        long DoubleBluet = 0;
        long MixedRedt = 0;
        long MixedBluet = 0;
        for (int i = 0; i <= (moves.length() - 4); i++) {
            SingleRedt = BitMoves.makeMove(BitBoardFigures.SingleRed, moves.substring(i, i + 4), type);
            SingleBluet = BitMoves.makeMove(BitBoardFigures.SingleBlue, moves.substring(i, i + 4), type);
            DoubleRedt = BitMoves.makeMove(BitBoardFigures.DoubleRed, moves.substring(i, i + 4), type);
            DoubleBluet = BitMoves.makeMove(BitBoardFigures.DoubleBlue, moves.substring(i, i + 4), type);
            MixedRedt = BitMoves.makeMove(BitBoardFigures.MixedRed, moves.substring(i, i + 4), type);
            MixedBluet = BitMoves.makeMove(BitBoardFigures.MixedBlue, moves.substring(i, i + 4), type);
        }
        BitBoard.drawArray(SingleRedt, SingleBluet, DoubleRedt, DoubleBluet, MixedRedt, MixedBluet);
    }

    public static long makeMove(long board, String move, String type){
        //Single move
        //if(type == 'S')

        int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
        int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
        //if statement is only true, when the current bitboard is occupied at the start position
        if (((board>>>start)&1)==1) {
            //Remove piece from start position
            board&=~(1L<<start);
            if (Objects.equals(type, "r0")){
                //end position has a SingleRed
                if(((board>>>end)&1)==1){
                    //Remove r0 at end position, as it has been stacked on
                    board &=~(1L<<end);
                    BitBoardFigures.DoubleRed |=(1L<<end);
                }
                //if end position has a SingleBlue
                else if (((BitBoardFigures.SingleBlue>>>end)&1) == 1) {
                    //Remove b0 from end position, as it has been captured
                    BitBoardFigures.SingleBlue &=~(1L<<end);
                    //r0 is moved to the end position, board must be SingleRed if this point is reached
                    board|=(1L<<end);
                }
                //if end position has a DoubleBlue
                else if (((BitBoardFigures.DoubleBlue>>>end)&1) == 1) {
                    //Remove bb from end position, as it has been captured
                    BitBoardFigures.DoubleBlue &=~(1L<<end);
                    //br is formed at the end position
                    BitBoardFigures.MixedRed |=(1L<<end);
                    //if end position has a MixedBlue
                } else if (((BitBoardFigures.MixedBlue>>>end)&1) == 1) {
                    //Remove rb from end position, as it has been captured
                    BitBoardFigures.MixedBlue &=~(1L<<end);
                    //rr is formed at the end position
                    BitBoardFigures.DoubleRed |=(1L<<end);
                }
                //if end position is empty
                else{
                    board|=(1L<<end);
                }
            }
            else if (Objects.equals(type, "b0")){
                //end position has a SingleBlue
                if(((board>>>end)&1)==1){
                    //Remove b0 at end position, as it has been stacked on
                    board &=~(1L<<end);
                    //bb is formed at end position
                    BitBoardFigures.DoubleBlue |=(1L<<end);
                }
                //if end position has a SingleRed
                else if (((BitBoardFigures.SingleRed>>>end)&1) == 1) {
                    //Remove r0 from end position, as it has been captured
                    BitBoardFigures.SingleRed &=~(1L<<end);
                    //b0 is moved to the end position, board must be SingleBlue if this point is reached
                    board|=(1L<<end);
                }
                //if end position has a DoubleRed
                else if (((BitBoardFigures.DoubleRed>>>end)&1) == 1) {
                    //Remove rr from end position, as it has been captured
                    BitBoardFigures.DoubleRed &=~(1L<<end);
                    //rb is formed at the end position
                    BitBoardFigures.MixedBlue |=(1L<<end);

                }
                //if end position has a MixedRed
                else if (((BitBoardFigures.MixedRed>>>end)&1) == 1) {
                    //Remove br from end position, as it has been captured
                    BitBoardFigures.MixedRed &=~(1L<<end);
                    //bb is formed at the end position
                    BitBoardFigures.DoubleBlue |=(1L<<end);
                }
                //if end position is empty
                else{
                    board|=(1L<<end);
                }

            }
            else if(Objects.equals(type, "rr") || Objects.equals(type, "br")){
                switch (type) {
                    case "rr":
                        //r0 is left at start position
                        BitBoardFigures.SingleRed |=(1L<<start);
                    case "br":
                        //b0 remains at start position
                        BitBoardFigures.SingleBlue |=(1L<<start);
                }
                //if end position has a r0
                if(((BitBoardFigures.SingleRed>>>end)&1) == 1){
                    //Remove r0 from end position, as it has been stacked on
                    BitBoardFigures.SingleRed &=~(1L<<end);
                    //Add rr at end position
                    BitBoardFigures.DoubleRed |=(1L<<end);
                }
                //if end position has a b0
                if(((BitBoardFigures.SingleBlue>>>end)&1) == 1){
                    //Remove b0 from end position, as it has been captured
                    BitBoardFigures.SingleBlue &=~(1L<<end);
                    //Add br at end position
                    BitBoardFigures.MixedRed |=(1L<<end);
                }
                //if end position has a bb
                if(((BitBoardFigures.DoubleBlue>>>end)&1) == 1){
                    //Remove bb from end position, as it has been captured
                    BitBoardFigures.DoubleBlue &=~(1L<<end);
                    //Add br at end position
                    BitBoardFigures.MixedRed |=(1L<<end);

                }
                //if end position has a rb
                if(((BitBoardFigures.MixedBlue>>>end)&1) == 1){
                    //Remove rb from end position, as it has been captured
                    BitBoardFigures.MixedBlue &=~(1L<<end);
                    //Add rr at end position
                    BitBoardFigures.DoubleRed |=(1L<<end);
                }
                //if end position is empty
                else{
                    //r0 is moved to end position
                    BitBoardFigures.SingleRed |=(1L<<end);
                }
            }
            else if(Objects.equals(type, "bb") || Objects.equals(type, "rb")){
                switch (type) {
                    case "rb":
                        //r0 is left at start position
                        BitBoardFigures.SingleRed |=(1L<<start);
                    case "bb":
                        //b0 remains at start position
                        BitBoardFigures.SingleBlue |=(1L<<start);
                }
                //if end position has a r0
                if(((BitBoardFigures.SingleRed>>>end)&1) == 1){
                    //Remove r0 from end position, as it has been captured
                    BitBoardFigures.SingleRed &=~(1L<<end);
                    //Add rb at end position
                    BitBoardFigures.MixedBlue |=(1L<<end);
                }
                //if end position has a b0
                if(((BitBoardFigures.SingleBlue>>>end)&1) == 1){
                    //Remove b0 from end position, as it has been stacked on
                    BitBoardFigures.SingleBlue &=~(1L<<end);
                    //Add bb at end position
                    BitBoardFigures.DoubleBlue |=(1L<<end);
                }
                //if end position has a rr
                if(((BitBoardFigures.DoubleRed>>>end)&1) == 1){
                    //Remove rr from end position, as it has been captured
                    BitBoardFigures.DoubleRed &=~(1L<<end);
                    //Add rb at end position
                    BitBoardFigures.MixedBlue |=(1L<<end);

                }
                //if end position has a br
                if(((BitBoardFigures.MixedRed>>>end)&1) == 1){
                    //Remove br from end position, as it has been captured
                    BitBoardFigures.MixedRed &=~(1L<<end);
                    //Add bb at end position
                    BitBoardFigures.DoubleBlue |=(1L<<end);
                }
                //if end position is empty
                else{
                    //b0 is moved to end position
                    BitBoardFigures.SingleBlue |=(1L<<start);
                }

            }
        }
        return board;
    }

     */
}
