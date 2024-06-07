import java.util.Objects;

public class BitMoves {
    static long RED_CAPTURE;
    static long RED_ROOK;
    static long RED_NON_CAPTURE;
    static long BLUE_CAPTURE;
    static long BLUE_NON_CAPTURE;
    static long BLUE_ROOK;
    static long EMPTY;
    static long EMPTY_KNIGHT;
    static long FILE_A = 72340172838076673L;
    static long FILE_AB = 217020518514230019L;
    static long FILE_GH = -4557430888798830400L;
    static long FILE_H = -9187201950435737472L;
    static long RANK_7= 65280L;
    static long RANK_2= 71776119061217280L;

    public static void main(String[] args) {

        BitBoard.initiateBoard();
        //String a = BitMoves.possibleMovesBlue(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        String b = BitMoves.possibleMovesRed(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
        makeMoveFEN("6/6bb1/8/8/5b02/8/rr7/6 r", b, "dr");
        BitBoard.drawArray(BitBoardFigures.SingleRed, BitBoardFigures.SingleBlue, BitBoardFigures.DoubleRed, BitBoardFigures.DoubleBlue, BitBoardFigures.MixedRed, BitBoardFigures.MixedBlue);
    }

    public static String possibleMovesBlue(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        BLUE_CAPTURE = SingleRed | DoubleRed | MixedRed;
        BLUE_NON_CAPTURE = ~(DoubleBlue | MixedBlue);
        BLUE_ROOK = ~(SingleBlue);
        EMPTY = ~(SingleRed | DoubleRed | MixedRed | DoubleBlue | MixedBlue);
        EMPTY_KNIGHT = ~(DoubleBlue | MixedBlue);
        return possibleMovesSB(SingleBlue)+possibleMovesNB(MixedBlue)+possibleMovesNB(DoubleBlue);
    }

    public static String possibleMovesRed(long SingleRed, long SingleBlue, long DoubleRed, long DoubleBlue, long MixedRed, long MixedBlue) {
        RED_CAPTURE = SingleBlue | DoubleBlue | MixedBlue;
        RED_NON_CAPTURE = ~(DoubleRed | MixedRed);
        RED_ROOK = ~(SingleRed);
        EMPTY = ~(SingleBlue | DoubleRed | MixedRed | DoubleBlue | MixedBlue);
        EMPTY_KNIGHT = ~(DoubleRed | MixedRed);
        return possibleMovesSR(SingleRed)+possibleMovesNR(MixedRed)+possibleMovesNR(DoubleRed);
    }

    // Erklärung: i ist die Position, auf die die Figur kann z.B. 51, dann kann die Figur auf das Feld 51
    // Felder werden von oben gezählt = erste reihe beginnt bei 0
    // es werden immer 4 Zahlen zu dem String hinzugefügt für jeden möglichen Move, die das Feld beschreiben
    // erste Zahl: Reihe des Startzustandes (bei 0 angefangen, von oben nach unten gezählt)
    // zweite Zahl:
    // dritte Zahl: Abstand nach oben des neuen Feldes
    // vierte Zahl: Abstand links Zielfeld (Beispiel Feld 4 von links -> Abstand = 3)
    private static String possibleMovesSB(long singleBlue) {
        String singleBlueMoves = "";
        //Capture right
        long SINGLEBLUE_MOVES = (singleBlue >> 7) & BLUE_CAPTURE & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Capture right " +i);
                singleBlueMoves += "" + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);
                System.out.println(singleBlueMoves);

            }
        }
        //Capture left
        SINGLEBLUE_MOVES = (singleBlue >> 9) & BLUE_CAPTURE & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Capture left " + i);
                singleBlueMoves += "" + (i / 8 + 1) + (i % 8 + 1) + (i / 8) + (i % 8);
                System.out.println(singleBlueMoves);
            }
        }
        //Move 1 forward -> Can form stacks
        SINGLEBLUE_MOVES = (singleBlue >> 8) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Move forward " +i);
                singleBlueMoves += "" + (i / 8 + 1) + (i % 8) + (i / 8) + (i % 8);
                System.out.println(singleBlueMoves);
            }
        }
        //Move 1 left -> Can form stacks
        SINGLEBLUE_MOVES = (singleBlue >> 1) & EMPTY & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 left " +i);
                singleBlueMoves += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
                System.out.println(singleBlueMoves);
            }
        }
        //Move 1 right -> Can form stacks
        SINGLEBLUE_MOVES = (singleBlue << 1) & EMPTY & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLEBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLEBLUE_MOVES); i++) {
            if (((SINGLEBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 right " +i);
                singleBlueMoves += "" + (i / 8) + (i % 8 + 1) + (i / 8) + (i % 8);
                System.out.println(singleBlueMoves);
            }
        }
        return singleBlueMoves;
    }

    private static String possibleMovesNB(long knightBlue) {
        String knightBlueMoves = "";
        //2 left 1 up
        long KNIGHTBLUE_MOVES = (knightBlue >> 10)& EMPTY_KNIGHT & ~FILE_GH ;
        for (int i = Long.numberOfTrailingZeros(KNIGHTBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTBLUE_MOVES); i++) {
            if (((KNIGHTBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("2 left 1 up " +i);
                knightBlueMoves += "" + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);
                System.out.println(knightBlueMoves);
            }
        }
        //1 right 2 up
        KNIGHTBLUE_MOVES = (knightBlue >> 15) & EMPTY_KNIGHT & ~FILE_H & ~RANK_7;
        for (int i = Long.numberOfTrailingZeros(KNIGHTBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTBLUE_MOVES); i++) {
            if (((KNIGHTBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("1 right 2 up " + i);
                knightBlueMoves += "" + (i / 8 + 1) + (i % 8 + 1) + (i / 8) + (i % 8);
                System.out.println(knightBlueMoves);
            }
        }
        //1 left 2 up
        KNIGHTBLUE_MOVES = (knightBlue >> 17) & EMPTY_KNIGHT& ~FILE_A & ~RANK_7;
        for (int i = Long.numberOfTrailingZeros(KNIGHTBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTBLUE_MOVES); i++) {
            if (((KNIGHTBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("1 left 2 up " +i);
                knightBlueMoves += "" + (i / 8 + 1) + (i % 8) + (i / 8) + (i % 8);
                System.out.println(knightBlueMoves);
            }
        }
        //2 right 1 up
        KNIGHTBLUE_MOVES = (knightBlue >> 6) & EMPTY& ~FILE_AB;
        for (int i = Long.numberOfTrailingZeros(KNIGHTBLUE_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTBLUE_MOVES); i++) {
            if (((KNIGHTBLUE_MOVES >> i) & 1) == 1) {
                System.out.println("Move 2 right 1 up " +i);
                knightBlueMoves += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
                System.out.println(knightBlueMoves);
            }
        }

        return knightBlueMoves;
    }

    private static String possibleMovesSR(long singleRed) {
        String singleRedMoves = "";

        //Capture right
        long SINGLERED_MOVES = (singleRed << 7) & RED_CAPTURE & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Capture right: " + i);
                singleRedMoves += "" + (i / 8 - 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        //Capture left
        SINGLERED_MOVES = (singleRed << 9) & RED_CAPTURE & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Capture right: " + i);
                singleRedMoves += "" + (i / 8 - 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 forward
        SINGLERED_MOVES = (singleRed << 8) & EMPTY;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Move forward: " + i);
                singleRedMoves += "" + (i / 8 - 1) + (i % 8) + (i / 8) + (i % 8);
            }
        }
        //Move 1 right
        SINGLERED_MOVES = (singleRed << 1) & EMPTY & ~FILE_H;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 right: " + i);
                singleRedMoves += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
            }
        }
        //Move 1 left
        SINGLERED_MOVES = (singleRed >> 1) & EMPTY & ~FILE_A;
        for (int i = Long.numberOfTrailingZeros(SINGLERED_MOVES); i < 64 - Long.numberOfLeadingZeros(SINGLERED_MOVES); i++) {
            if (((SINGLERED_MOVES >> i) & 1) == 1) {
                System.out.println("Move 1 left: " + i);
                singleRedMoves += "" + (i / 8) + (i % 8 + 1) + (i / 8) + (i % 8);
            }
        }
        return singleRedMoves;
    }

    private static String possibleMovesNR(long knightRed) {
        String knightRedMoves = "";
        //2 left 1 down
        long KNIGHTRED_MOVES = (knightRed << 6)& EMPTY_KNIGHT & ~FILE_GH;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("2 left 1 up " +i);
                knightRedMoves += "" + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);
                System.out.println(knightRedMoves);
            }
        }
        //1 right 2 down
        KNIGHTRED_MOVES = (knightRed << 15) & EMPTY_KNIGHT & ~FILE_H & ~RANK_2;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("1 right 2 up " + i);
                knightRedMoves += "" + (i / 8 + 1) + (i % 8 + 1) + (i / 8) + (i % 8);
                System.out.println(knightRedMoves);
            }
        }
        //1 left 2 down
        KNIGHTRED_MOVES = (knightRed << 17) & EMPTY_KNIGHT & ~FILE_A & ~RANK_2;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("1 left 2 up " +i);
                knightRedMoves += "" + (i / 8 + 1) + (i % 8) + (i / 8) + (i % 8);
                System.out.println(knightRedMoves);
            }
        }
        //2 right 1 down
        KNIGHTRED_MOVES = (knightRed << 10) & EMPTY& ~FILE_AB;
        for (int i = Long.numberOfTrailingZeros(KNIGHTRED_MOVES); i < 64 - Long.numberOfLeadingZeros(KNIGHTRED_MOVES); i++) {
            if (((KNIGHTRED_MOVES >> i) & 1) == 1) {
                System.out.println("2 right 1 up " +i);
                knightRedMoves += "" + (i / 8) + (i % 8 - 1) + (i / 8) + (i % 8);
                System.out.println(knightRedMoves);
            }
        }

        return knightRedMoves;
    }

    /*long  SingleRedt  = BitMoves.makeMove(BitBoardFigures.SingleRed, moves.substring(i, i + 4), 'S'), SingleBluet = BitMoves.makeMove(BitBoardFigures.SingleBlue, moves.substring(i, i + 4), 'S'),
            DoubleRedt = BitMoves.makeMove(BitBoardFigures.DoubleRed, moves.substring(i, i + 4), 'B'), DoubleBluet = BitMoves.makeMove(BitBoardFigures.DoubleBlue, moves.substring(i, i + 4), 'R'),
            MixedRedt  = BitMoves.makeMove(BitBoardFigures.MixedRed, moves.substring(i, i + 4), 'Q'), MixedBluet  = BitMoves.makeMove(BitBoardFigures.MixedBlue, moves.substring(i, i + 4), 'K');

    */

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
}
