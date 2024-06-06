import java.util.Arrays;

import static java.lang.Character.isDigit;

public class BitBoard {

    public static void main(String[] args) {
        importFEN("6/1b06/1r0b02bb2/2r02b02/8/5rr2/2r03r01/6 b");
    }

    public static void initiateBoard(){
        long SingleRed = 0L, SingleBlue = 0L, DoubleRed = 0L, DoubleBlue = 0L, MixedRed = 0L, MixedBlue = 0L;

        String[][] jumpBoard ={
                {"","r0","r0","r0","r0","r0","r0",""},
                {"","r0","r0","r0","r0","r0","r0",""},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {"","b0","b0","b0","b0","b0","b0",""},
                {"","b0","b0","b0","b0","b0","b0",""}};
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
                System.out.println("BoardIndex if 0 or 7: " + boardIndex);
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
                System.out.println("BoardIndex if 0 or 7: " + boardIndex);
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
}
