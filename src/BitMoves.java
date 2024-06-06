
public class BitMoves {
    static long RED_CAPTURE;
    static long RED_ROOK;
    static long RED_NON_CAPTURE;
    static long BLUE_CAPTURE;
    static long BLUE_NON_CAPTURE;
    static long BLUE_ROOK;
    static long EMPTY;
    static long FILE_A = 72340172838076673L;
    static long FILE_H = -9187201950435737472L;

    public static String possibleMovesBlue(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        BLUE_CAPTURE = SingleRed | DoubleRed | MixedRed;
        BLUE_NON_CAPTURE = ~(DoubleBlue | MixedBlue);
        BLUE_ROOK = ~(SingleBlue);
        EMPTY = ~(SingleRed | DoubleRed | MixedRed | DoubleBlue | MixedBlue);

        String list = possibleMovesSB(SingleBlue);
        return list;
    }

    public static void main(String[] args) {
        BitBoard.importFEN("6/1b06/1r0b02bb2/2r02b02/8/5rr2/2r03r01/6 b");
        long SingleRed = 0L, SingleBlue = 0L, DoubleRed = 0L, DoubleBlue = 0L, MixedRed = 0L, MixedBlue = 0L;
        String[][] jumpBoard ={
                {"","r0","r0","r0","r0","r0","r0",""},
                {"","r0","r0","r0","r0","r0","r0",""},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," ", " ", " ", " "," "," "},
                {"","b0","b0","b0","b0","b0","b0",""},
                {"","b0","b0","b0","b0","b0","b0",""}};
        BitBoard.arrayToBitboards(jumpBoard,SingleRed,SingleBlue,DoubleRed,DoubleBlue,MixedRed,MixedBlue);
        String list = possibleMovesBlue(BitBoardFigures.SingleRed,BitBoardFigures.SingleBlue,BitBoardFigures.DoubleRed,BitBoardFigures.DoubleBlue,BitBoardFigures.MixedRed,BitBoardFigures.MixedBlue);
        System.out.println("List:" + list);


    }


    public static String possibleMovesRed(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        RED_CAPTURE = SingleBlue | DoubleBlue | MixedBlue;
        RED_NON_CAPTURE = ~(DoubleRed | MixedRed);
        RED_ROOK = ~(SingleRed);
        EMPTY = ~(SingleBlue | DoubleRed | MixedRed | DoubleBlue | MixedBlue);

        String list = possibleMovesSR(SingleRed);
        return list;
    }

    private static String possibleMovesSB(long singleBlue) {
        String list = "";
        //Capture right
        long SINGLEBLUE_MOVES = (singleBlue >> 7) & BLUE_CAPTURE & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Capture left
        SINGLEBLUE_MOVES = (singleBlue >> 9) & BLUE_CAPTURE & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8 + 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 forward
        SINGLEBLUE_MOVES = (singleBlue >> 8) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8 + 1) + (i % 8) + (i / 8) + (i % 8);
            }
        }
        //Move 1 right
        SINGLEBLUE_MOVES = (singleBlue >> 1) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 left
        SINGLEBLUE_MOVES = (singleBlue << 1) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        return list;
    }

    private static String possibleMovesSR(long singleRed) {
        String list = "";

        //Capture right
        long SINGLERED_MOVES = (singleRed << 7) & RED_CAPTURE & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8 - 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        //Capture left
        SINGLERED_MOVES = (singleRed << 9) & RED_CAPTURE & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8 - 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 forward
        SINGLERED_MOVES = (singleRed << 8) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8 - 1) + (i % 8) + (i / 8) + (i % 8);
            }
        }
        //Move 1 right
        SINGLERED_MOVES = (singleRed << 1) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 left
        SINGLERED_MOVES = (singleRed >> 1) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                list += "" + (i / 8) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        return list;
    }
}
