import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

public class Board {
    //board = new Figures[row][column]
    //board = new Figures[2][a]
    //column uses ascii char values
    public Figures[][] board = new Figures[8][8];

    //index 0 are red figures, index 1 are blue figures
    public HashMap<Field, Figures>[] figureMap = new HashMap[2];
    public int blueToMove;

    //v2.0 enhanced
    boolean isEmptyMoveSet = false;

    //v2.0 enhanced
    boolean isFigureOnLastRow = false;

    public static void main(String[] args) {

        Board board = new Board();

        //assumes given fen is correct
        String init = "b0b0b0b0b0b0/1b0b0b0b0b0b01/8/8/8/8/1r0r0r0r0r0r01/r0r0r0r0r0r0 r";
        String fen = init.substring(0, init.length() - 2);

        board.fenToBoard(fen);
        board.blueToMove = (init.charAt(init.length()-1) == 'b' ? 1 : 0);
        System.out.println("init eval: " + board.evaluatePosition(0));
        board.boardToString();

        boolean isMax = true;
        while (!board.isGameFinishedEnhanced(board.blueToMove)) {

            if (board.blueToMove == 1){
                isMax = true;
            } else {
                isMax = false;
            }

            long start = System.currentTimeMillis();
            ValueMove vm = alphaBeta(board, isMax);
            long end = System.currentTimeMillis();
            Move move = vm.move;
            System.out.println();
            System.out.println("took: " + (end - start) + " ms");
            System.out.println("Move made: " + move + " on expected eval " + vm.v);
            board.makeMove(move);
            board.boardToString();
        }

    }

    public void setBoardContent(Figures[][] board, HashMap<Field, Figures>[] maps, int blueToMove) {
        this.board = board;
        this.figureMap = maps;
        this.blueToMove = blueToMove;
    }

    static public ValueMove alphaBeta(Board board, boolean isMax){
        //TODO: here would the time cutoff and iterative depth search be implemented for a game contest, however there are no boundaries yet
        return alphaBetaRecursion(board, 7, -100000.0f, +100000.0f, isMax);
    }

    static public ValueMove alphaBetaRecursion(Board board, int depth, float alpha, float beta, boolean isMax){
        if(depth == 0 || board.isGameFinishedEnhanced(board.blueToMove)){
            return new ValueMove(board.evaluatePosition(depth), null, depth);
        }

        if (isMax){
            //float value = alpha;
            float value = -100000.0f;
            Move bestMove = null;
            int bestDepth = depth;

            for (Move move : board.getPossibleAndLegalMoves(board.blueToMove)) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                ValueMove evaluation = alphaBetaRecursion(newBoard, depth - 1, alpha, beta, false);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v > value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = move;
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
            return new ValueMove(value, bestMove, bestDepth);
        } else {
            //float value = beta;
            float value = 100000.0f;
            Move bestMove = null;
            int bestDepth = depth;

            for (Move move : board.getPossibleAndLegalMoves(board.blueToMove)) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                ValueMove evaluation = alphaBetaRecursion(newBoard, depth - 1, alpha, beta, true);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v < value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = move;
                    bestDepth = evaluation.depth;
                }

                beta = Math.min(beta, value);

                if (value <= alpha) {
                    //System.out.println("break");
                    break;
                }

                /*if (alpha >= beta) {
                    //System.out.println("break");
                    break;
                }*/

            }
            return new ValueMove(value, bestMove, bestDepth);
        }
    }

    static public ValueMove alphaBetaRecursionNew(Board board, int depth, float alpha, float beta, boolean isMax){
        if(depth == 0 || board.isGameFinishedEnhanced(board.blueToMove)){
            return new ValueMove(board.evaluatePosition(depth), null, depth);
        }

        if (isMax){
            //float value = alpha;
            float value = -100000.0f;
            Move bestMove = null;
            int bestDepth = depth;

            for (Move move : board.getPossibleAndLegalMoves(board.blueToMove)) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                ValueMove evaluation = alphaBetaRecursionNew(newBoard, depth - 1, alpha, beta, false);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v > value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = move;
                    bestDepth = evaluation.depth;
                }

                alpha = Math.max(alpha, value);

                /*if (value > beta) {
                    //System.out.println("break");
                    break;
                }*/

                /*if (beta <= alpha) {
                    //System.out.println("break");
                    break;
                }*/
            }
            return new ValueMove(value, bestMove, bestDepth);
        } else {
            //float value = beta;
            float value = 100000.0f;
            Move bestMove = null;
            int bestDepth = depth;

            for (Move move : board.getPossibleAndLegalMoves(board.blueToMove)) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                ValueMove evaluation = alphaBetaRecursionNew(newBoard, depth - 1, alpha, beta, true);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v < value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = move;
                    bestDepth = evaluation.depth;
                }

                beta = Math.min(beta, value);

                /*if (value < alpha) {
                    //System.out.println("break");
                    break;
                }*/

                /*if (beta <= alpha) {
                    //System.out.println("break");
                    break;
                }*/

            }
            return new ValueMove(value, bestMove, bestDepth);
        }
    }

    static public ValueMove miniMax(Board board, boolean isMax) {
        //TODO: here would the time cutoff and iterative depth search be implemented for a game contest, however there are no boundaries yet
        return miniMaxRecursion(board, 5, isMax);
    }

    static public ValueMove miniMaxRecursion(Board board, int depth, boolean isMax){
        if(depth == 0 || board.isGameFinishedEnhanced(board.blueToMove)){
            return new ValueMove(board.evaluatePosition(depth), null, depth);
        }

        if (isMax){
            float value = -100000.0f;
            Move bestMove = null;
            int bestDepth = depth;

            for (Move move : board.getPossibleAndLegalMoves(board.blueToMove)) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                ValueMove evaluation = miniMaxRecursion(newBoard, depth - 1, false);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v > value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = move;
                    bestDepth = evaluation.depth;
                }
            }
            return new ValueMove(value, bestMove, bestDepth);
        } else {
            float value = 100000.0f;
            Move bestMove = null;
            int bestDepth = depth;

            for (Move move : board.getPossibleAndLegalMoves(board.blueToMove)) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                ValueMove evaluation = miniMaxRecursion(newBoard, depth - 1, true);

                //value = Math.max(value, evaluation.v);
                if (evaluation.v < value ||
                        (evaluation.v == value && evaluation.depth > bestDepth)){
                    value = evaluation.v;
                    bestMove = move;
                    bestDepth = evaluation.depth;
                }
            }
            return new ValueMove(value, bestMove, bestDepth);
        }
    }

    //TODO: replace with unmake move
    public Figures[][] deepCopyBoard() {
        Figures[][] newBoard = new Figures[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    //TODO: replace with unmake move
    public HashMap<Field, Figures>[] deepCopyBoardMap() {
        HashMap<Field, Figures>[] newBoardMap = new HashMap[2];
        for (int i = 0; i < 2; i++) {
            newBoardMap[i] = new HashMap();
            for (Map.Entry<Field, Figures> entry : figureMap[i].entrySet()) {
                newBoardMap[i].put(entry.getKey(), entry.getValue());
            }
        }
        return newBoardMap;
    }


    //TODO: implement evaluation function, group T FEN 6/4bb3/8/8/4b0r0b01/8/8/6 b; Stellungsbeschreibung: Blau gewinnt in einem Zug durch Blocken not implemented
    public float evaluatePosition(int depth){
        if(figureMap[0].isEmpty()) return +1000.0f + depth;
        if(figureMap[1].isEmpty()) return -1000.0f - depth;

        float value = 0;

        for (Map.Entry<Field, Figures> entry : figureMap[1].entrySet()) {
            if (entry.getKey().row == 7) return +1000.0f + depth;

            if (entry.getValue() == Figures.DOUBLE_BLUE){
                value += 20;
                value += (float) ((entry.getKey().row + 1) * 2.5);
            } else {
                value += 10;
            }

            value += (float) ((entry.getKey().row + 1) * 2.5);
        }

        for (Map.Entry<Field, Figures> entry : figureMap[0].entrySet()) {
            if (entry.getKey().row == 0) return -1000.0f - depth;

            if (entry.getValue() == Figures.DOUBLE_RED){
                value -= 20;
                value -= (float) ((8 - entry.getKey().row) * 2.5);
            } else {
                value -= 10;
            }

            value -= (float) ((8 - entry.getKey().row) * 2.5);
        }

        return value;
    }


    /*
    //überprüft, ob die korrekten Zeichenkombinationen verwendet worden sind
    static boolean correctFen(String fen){
        String regex = "^(?=(?:b0|bb|rb|br|rr|r0|[1-8]|\\/)+\\s+[br]+$)";
        Matcher matcher = Pattern.compile(regex).matcher(fen);
        boolean matchFound = matcher.find();
        if(matchFound&&correctCharCount(fen)&&checkFenLogic(fen)) {
            System.out.println("Match found");
            return true;
        } else {
            System.out.println("Match not found");
            return false;
        }
    }

    //überprüft, ob die richtige Anzahl von r, b und / vorkommt
    static boolean correctCharCount(String fen){
        String regex= "^(?=(?:[^r]*r){0,13}[^r]*$)(?=(?:[^b]*b){0,13}[^b]*$)(?=(?:[^\\/]*\\/){7}[^\\/]*$).*$";
        Matcher matcher = Pattern.compile(regex).matcher(fen);
        boolean matchFound = matcher.find();
        if(matchFound) {
            return true;
        } else {
            System.out.println("Wrong count of / or r or b");
            return false;
        }
    }

    //überprüft die Länge der jeweiligen Zeilen
    static boolean checkFenLogic(String fen){
        String[] fen_split = fen.split("/");
        for(int i = 0; i < fen_split.length; i++) {
            int count_length = 0;
            for (int j = 0; j < fen_split[i].length(); j++) {
                if (isDigit(fen_split[i].charAt(j))) {
                    count_length += Character.getNumericValue(fen_split[i].charAt(j));
                }else if(fen_split[i].charAt(j)==' '){
                    j+=1;
                }else {
                    count_length += 1;
                    j +=1;
                }
            }
            if((i==0 || i==7)){
                if(count_length !=6){
                    System.out.println("Wrong length " + i + ". current length" + count_length);
                    return false;
                }else if(!correct_lastLine(fen_split[i], i)){
                    return false;
                }
            }else if(i < 7 && count_length != 8) {
                System.out.println("Wrong length " + i + ". current length" + count_length);
                return false;
            }
        }
        return true;
    }

    //überprüft, ob in der ersten/letzten Zeile nur einmal r/b und kein rb/br vorkommt
    static boolean correct_lastLine(String fen, int line) {
        switch (line){
            case 0 ->{
                String regex= "^(?=[^r]*r?[^r]*$)(?!.*rb).*$";
                Matcher matcher = Pattern.compile(regex).matcher(fen);
                boolean matchFound = matcher.find();
                if(matchFound) {
                    return true;
                } else {
                    System.out.println("non reachable position (First Line)");
                    return false;
                }
            }
            case 7 ->{
                String fenLastLine = fen.substring(0,fen.length()-2);
                String regex= "^(?=[^b]*b?[^b]*$)(?!.*br).*$";
                Matcher matcher = Pattern.compile(regex).matcher(fenLastLine);
                boolean matchFound = matcher.find();
                if(matchFound) {
                    return true;
                } else {
                    System.out.println("non reachable position (Last Line)");
                    return false;
                }
            }
            default -> {
                System.out.println("Wrong");
                return false;
            }
        }
    }

     */



    //assumes the fen has correct syntax; old and slower
    public void fenToBoard(String fen){
        board = new Figures[8][8];
        String tmpFEN = fen;
        char value;

        figureMap[0] = new HashMap<>();
        figureMap[1] = new HashMap<>();

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){

                //handle blank edges
                if ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)){
                    continue;
                }

                value = tmpFEN.charAt(0);

                if(value == 'r'){
                    value = tmpFEN.charAt(1);

                    switch (value){
                        case '0' -> {
                            board[i][j] = Figures.SINGLE_RED;
                            figureMap[0].put(new Field(i, (char) ('a' + j)), Figures.SINGLE_RED);
                        }
                        case 'r' -> {
                            board[i][j] = Figures.DOUBLE_RED;
                            figureMap[0].put(new Field(i, (char) ('a' + j)), Figures.DOUBLE_RED);
                        }
                        case 'b' -> {
                            board[i][j] = Figures.MIXED_BLUE;
                            figureMap[0].put(new Field(i, (char) ('a' + j)), Figures.MIXED_BLUE);
                            figureMap[1].put(new Field(i, (char) ('a' + j)), Figures.MIXED_BLUE);
                        }
                    }

                    tmpFEN = tmpFEN.substring(2);
                    continue;
                }

                if(value == 'b'){
                    value = tmpFEN.charAt(1);

                    //i here from 0 to 7 for board index, has to be accounted for
                    switch (value){
                        case '0' -> {
                            board[i][j] = Figures.SINGLE_BLUE;
                            figureMap[1].put(new Field(i, (char) ('a' + j)), Figures.SINGLE_BLUE);
                        }
                        case 'b' -> {
                            board[i][j] = Figures.DOUBLE_BLUE;
                            figureMap[1].put(new Field(i, (char) ('a' + j)), Figures.DOUBLE_BLUE);
                        }
                        case 'r' -> {
                            board[i][j] = Figures.MIXED_RED;
                            figureMap[0].put(new Field(i, (char) ('a' + j)), Figures.MIXED_RED);
                            figureMap[1].put(new Field(i, (char) ('a' + j)), Figures.MIXED_RED);
                        }
                    }

                    tmpFEN = tmpFEN.substring(2);
                    continue;
                }

                //a character is just an ASCII value in java
                int skip = value - '0';
                //before using Profiler:
                //int skip = Integer.parseInt(String.valueOf(value))

                j += skip - 1;
                //cut the empty spaces number
                tmpFEN = tmpFEN.substring(1);
            }

            //cut the slash from the string
            //don't cut at the end
            if(i != 7) tmpFEN = tmpFEN.substring(1);
        }
    }

    //assumes the fen has correct syntax; TODO
    /*static void fenToBoardNew(String fen){
        char[] tmpFEN = fen.toCharArray();
        char value;
        int index = 0;

        figureMap[0] = new HashMap<>();
        figureMap[1] = new HashMap<>();

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){

                //handle blank edges
                if ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)){
                    ++index;
                    continue;
                }

                try{
                    value = tmpFEN[index];
                    System.out.println(value);
                } catch (Exception e){
                    boardToString();
                    return;
                }


                if(value == 'r'){
                    ++index;
                    value = tmpFEN[index];

                    switch (value){
                        case '0' -> {
                            board[i][j] = Figures.SINGLE_RED;
                            figureMap[0].put(new Field(i, j), Figures.SINGLE_RED);
                        }
                        case 'r' -> {
                            board[i][j] = Figures.DOUBLE_RED;
                            figureMap[0].put(new Field(i, j), Figures.DOUBLE_RED);
                        }
                        case 'b' -> {
                            board[i][j] = Figures.MIXED_BLUE;
                            figureMap[0].put(new Field(i, j), Figures.MIXED_BLUE);
                            figureMap[1].put(new Field(i, j), Figures.MIXED_BLUE);
                        }
                    }
                    ++index;
                    continue;
                }

                if(value == 'b'){
                    ++index;
                    value = tmpFEN[index];

                    switch (value){
                        case '0' -> {
                            board[i][j] = Figures.SINGLE_BLUE;
                            figureMap[1].put(new Field(i, j), Figures.SINGLE_BLUE);
                        }
                        case 'b' -> {
                            board[i][j] = Figures.DOUBLE_BLUE;
                            figureMap[1].put(new Field(i, j), Figures.DOUBLE_BLUE);
                        }
                        case 'r' -> {
                            board[i][j] = Figures.MIXED_RED;
                            figureMap[0].put(new Field(i, j), Figures.MIXED_RED);
                            figureMap[1].put(new Field(i, j), Figures.MIXED_RED);
                        }
                    }

                    ++index;
                    continue;
                }

                //a character is just an ASCII value in java
                int skip = value - '0';
                //before using Profiler:
                //int skip = Integer.parseInt(String.valueOf(value))

                j += skip - 1;
                //cut the empty spaces number
                ++index;
            }

            //cut the slash from the string
            //don't cut at the end
            if(i != 7) ++index;
        }
    }*/

    public void boardToString(){
        for(int i=7; i>-1; i--){
            System.out.print((i+1) + "|");
            for(int j=0; j<8; j++){
                Figures tmp = board[i][j];

                switch (tmp){
                    case SINGLE_RED -> {
                        System.out.print(" r0 " + "|");
                    }
                    case SINGLE_BLUE -> {
                        System.out.print(" b0 " + "|");
                    }
                    case DOUBLE_RED -> {
                        System.out.print(" rr " + "|");
                    }
                    case DOUBLE_BLUE -> {
                        System.out.print(" bb "+ "|");
                    }
                    case MIXED_RED -> {
                        System.out.print(" br " + "|");
                    }
                    case MIXED_BLUE -> {
                        System.out.print(" rb " + "|");
                    }
                    case null -> {
                        System.out.print("    " + "|");
                    }
                }
            }
            System.out.println();
            if(i == 0){
                System.out.println("    a    b    c    d    e    f    g    h");
            }
        }
    }

    //v2.0 enhanced, doesn't work if fen init position is already finished
    boolean isGameFinishedEnhanced(int blueToMove){
        //index 0 are red figures, index 1 are blue figures
        if(figureMap[0].isEmpty() || figureMap[1].isEmpty()) return true;

        //game is over if current player has no moves left, improved
        //if (getPossibleAndLegalMoves(blueToMove).isEmpty()) return true;
        if (isEmptyMoveSet) return true;

        //maybe replace with flag in makeMove?
        /*for(int i = 1; i<7; i++){
            //check if red has figure on blue home row
            switch (board[0][i]){
                case SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                    return true;
                }
                case SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> { }
                case null -> { }
            }

            //check if blue has figure on red home row
            switch (board[7][i]){
                case SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                    return true;
                }
                case SINGLE_RED, DOUBLE_RED, MIXED_RED -> { }
                case null -> { }
            }

        }*/

        if (isFigureOnLastRow) return true;

        return false;
    }

    //v2.0 enhanced
    Set<Move> getPossibleAndLegalMoves(int blueToMove){
        HashSet<Move> moves = new HashSet<>();

        for(Map.Entry<Field, Figures> entry: figureMap[blueToMove].entrySet()){

            //if blue is to move: a MIXED_RED has no possible moves (the blue single is blocked by the red on top)
            if (blueToMove == 1) {
                if (entry.getValue() == Figures.MIXED_RED) {
                    continue;
                }
            }

            //if red is to move: a MIXED_BLUE has no possible moves (the red single is blocked by the blue on top)
            if (blueToMove == 0) {
                if (entry.getValue() == Figures.MIXED_BLUE) {
                    continue;
                }
            }

            switch (entry.getValue()){
                //find all theoretically possible moves and subtract not allowed moves
                //time complexity not great
                case SINGLE_RED -> {
                    Move move1 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col+1));
                    Move move2 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-1, entry.getKey().col);
                    Move move3 = new Move(entry.getKey().row, entry.getKey().col, (entry.getKey().row-1), (char) (entry.getKey().col+1));
                    Move move4 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col-1));
                    Move move5 = new Move(entry.getKey().row, entry.getKey().col, (entry.getKey().row-1), (char) (entry.getKey().col-1));

                    if (isLegalMove(move1)){
                        moves.add(move1);
                    }
                    if (isLegalMove(move2)){
                        moves.add(move2);
                    }
                    if (isLegalMove(move3)){
                        moves.add(move3);
                    }
                    if (isLegalMove(move4)){
                        moves.add(move4);
                    }
                    if (isLegalMove(move5)){
                        moves.add(move5);
                    }
                }
                case SINGLE_BLUE -> {
                    Move move1 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col+1));
                    Move move2 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, entry.getKey().col);
                    Move move3 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col+1));
                    Move move4 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col-1));
                    Move move5 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col-1));

                    if (isLegalMove(move1)){
                        moves.add(move1);
                    }
                    if (isLegalMove(move2)){
                        moves.add(move2);
                    }
                    if (isLegalMove(move3)){
                        moves.add(move3);
                    }
                    if (isLegalMove(move4)){
                        moves.add(move4);
                    }
                    if (isLegalMove(move5)){
                        moves.add(move5);
                    }
                }
                case DOUBLE_RED, MIXED_RED -> {
                    Move move1 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-2, (char) (entry.getKey().col+1));
                    Move move2 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-2, (char) (entry.getKey().col-1));
                    Move move3 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-1, (char) (entry.getKey().col+2));
                    Move move4 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-1, (char) (entry.getKey().col-2));

                    if (isLegalMove(move1)){
                        moves.add(move1);
                    }
                    if (isLegalMove(move2)){
                        moves.add(move2);
                    }
                    if (isLegalMove(move3)){
                        moves.add(move3);
                    }
                    if (isLegalMove(move4)){
                        moves.add(move4);
                    }
                }
                case DOUBLE_BLUE, MIXED_BLUE -> {
                    Move move1 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+2, (char) (entry.getKey().col+1));
                    Move move2 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+2, (char) (entry.getKey().col-1));
                    Move move3 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col+2));
                    Move move4 = new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col-2));

                    if (isLegalMove(move1)){
                        moves.add(move1);
                    }
                    if (isLegalMove(move2)){
                        moves.add(move2);
                    }
                    if (isLegalMove(move3)){
                        moves.add(move3);
                    }
                    if (isLegalMove(move4)){
                        moves.add(move4);
                    }
                }
            }
        }

        isEmptyMoveSet = moves.isEmpty();
        return moves;
    }

    //v2.0 enhanced
    boolean isLegalMove(Move move){
        boolean notLegal = false;

        Figures start = board[move.start_row][move.start_col - 'a'];
        Figures target;

        //TODO adjust for ascii
        int i = move.end_row;
        int j = move.end_col - 'a';

        //removes all moves to corners
        if ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)){
            return notLegal;
        }

        //remove all moves out of board
        try {
            target = board[move.end_row][move.end_col - 'a'];
        } catch (ArrayIndexOutOfBoundsException e){
            return notLegal;
        }

        //add all RED figure capture or tower building moves
        /*if (start == Figures.SINGLE_RED){
            switch(target){
                case SINGLE_RED-> {
                    //all okay but capture
                    if(isNormalMove(move)){
                        return true;
                    }
                }
                case SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                    //all okay but normal move
                    if(isNormalCapture(move)){
                        return true;
                    }
                }
                case DOUBLE_RED, MIXED_RED -> { return notLegal; }
                case null -> {
                    //add all moves to empty field
                    if(isNormalMove(move)){
                        return true;
                    }
                }
            }
        }

        //add all RED figure capture or tower building moves
        if (start == Figures.SINGLE_BLUE){
            switch(target){
                case SINGLE_BLUE -> {
                    //all okay but capture
                    if(isNormalMove(move)){
                        return true;
                    }
                }
                case SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                    //all okay but normal move
                    if(isNormalCapture(move)){
                        return true;
                    }
                }
                case DOUBLE_BLUE, MIXED_BLUE -> { return notLegal; }
                case null -> {
                    //add all moves to empty field
                    if(isNormalMove(move)){
                        return true;
                    }
                }
            }
        }

        if (start == Figures.MIXED_RED || start == Figures.DOUBLE_RED){
            switch(target){
                case SINGLE_RED, SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                    //capture and tower
                    return true;
                }
                case DOUBLE_RED, MIXED_RED -> { return notLegal; }
                case null -> {
                    //add all moves to empty field
                    return true;
                }
            }
        }

        //add all RED figure capture or tower building moves
        if (start == Figures.MIXED_BLUE || start == Figures.DOUBLE_BLUE){
            switch(target){
                case SINGLE_BLUE, SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                    //capture and tower
                    return true;
                }
                case DOUBLE_BLUE, MIXED_BLUE -> { return notLegal; }
                case null -> {
                    //add all moves to empty field
                    return true;
                }
            }
        }*/

        switch(start){
            case SINGLE_RED -> {
                switch (target) {
                    case SINGLE_RED -> {
                        //all okay but capture
                        if (isNormalMove(move)) {
                            return true;
                        }
                    }
                    case SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                        //all okay but normal move
                        if (isNormalCapture(move)) {
                            return true;
                        }
                    }
                    case DOUBLE_RED, MIXED_RED -> {
                        return notLegal;
                    }
                    case null -> {
                        //add all moves to empty field
                        if (isNormalMove(move)) {
                            return true;
                        }
                    }
                }
            }
            case SINGLE_BLUE -> {
                switch (target) {
                    case SINGLE_BLUE -> {
                        //all okay but capture
                        if (isNormalMove(move)) {
                            return true;
                        }
                    }
                    case SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                        //all okay but normal move
                        if (isNormalCapture(move)) {
                            return true;
                        }
                    }
                    case DOUBLE_BLUE, MIXED_BLUE -> {
                        return notLegal;
                    }
                    case null -> {
                        //add all moves to empty field
                        if (isNormalMove(move)) {
                            return true;
                        }
                    }
                }
            }
            case MIXED_RED, DOUBLE_RED -> {
                switch(target){
                    case SINGLE_RED, SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                        //capture and tower
                        return true;
                    }
                    case DOUBLE_RED, MIXED_RED -> { return notLegal; }
                    case null -> {
                        //add all moves to empty field
                        return true;
                    }
                }
            }
            case MIXED_BLUE, DOUBLE_BLUE -> {
                switch(target){
                    case SINGLE_BLUE, SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                        //capture and tower
                        return true;
                    }
                    case DOUBLE_BLUE, MIXED_BLUE -> { return notLegal; }
                    case null -> {
                        //add all moves to empty field
                        return true;
                    }
                }
            }
        }

        return notLegal;
    }

    /* v1.0
    public boolean isGameFinished(int blueToMove){
            //index 0 are red figures, index 1 are blue figures
        if(figureMap[0].isEmpty() || figureMap[1].isEmpty()) return true;

        //TODO clean this up
        Set<Move> moves = getPossibleMoves(blueToMove);
        if (getLegalMoves(moves).isEmpty()) return true;

        for(int i = 1; i<7; i++){

            //check if red has figure on blue home row
            switch (board[0][i]){
                case SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                    return true;
                }
                case SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> { }
                case null -> { }
            }

            //check if blue has figure on red home row
            switch (board[7][i]){
                case SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                    return true;
                }
                case SINGLE_RED, DOUBLE_RED, MIXED_RED -> { }
                case null -> { }
            }
        }

        return false;
    }*/

    /* v1.0
    public Set<Move> getPossibleMoves(int blueToMove){
        HashSet<Move> moves = new HashSet<>();

        for(Map.Entry<Field, Figures> entry: figureMap[blueToMove].entrySet()){

            //if blue is to move: a MIXED_RED has no possible moves (the blue single is blocked by the red on top)
            if (blueToMove == 1) {
                if (entry.getValue() == Figures.MIXED_RED) {
                    continue;
                }
            }

            //if red is to move: a MIXED_BLUE has no possible moves (the red single is blocked by the blue on top)
            if (blueToMove == 0) {
                if (entry.getValue() == Figures.MIXED_BLUE) {
                    continue;
                }
            }

            switch (entry.getValue()){
                //find all theoretically possible moves and subtract not allowed moves
                //time complexity not great
                case SINGLE_RED -> {
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-1, entry.getKey().col));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, (entry.getKey().row-1), (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col-1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, (entry.getKey().row-1), (char) (entry.getKey().col-1)));
                }
                case SINGLE_BLUE -> {
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, entry.getKey().col));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col-1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col-1)));
                }
                case DOUBLE_RED, MIXED_RED -> {
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-2, (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-2, (char) (entry.getKey().col-1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-1, (char) (entry.getKey().col+2)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-1, (char) (entry.getKey().col-2)));
                }
                case DOUBLE_BLUE, MIXED_BLUE -> {
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+2, (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+2, (char) (entry.getKey().col-1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col+2)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row+1, (char) (entry.getKey().col-2)));
                }
            }
        }

        return moves;
    }*/

    /* v1.0
    public Set<Move> getLegalMoves(Set<Move> moves){
        HashSet<Move> legalMoves = new HashSet<>();

        for(Move move: moves){
            Figures start = board[move.start_row][move.start_col - 'a'];
            Figures target;


            //TODO adjust for ascii
            int i = move.end_row;
            int j = move.end_col - 'a';

            //removes all moves to corners
            if ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)){
                continue;
            }


            //remove all moves out of board
            try {
                target = board[move.end_row][move.end_col - 'a'];
            } catch (ArrayIndexOutOfBoundsException e){
                continue;
            }

            //add all RED figure capture or tower building moves
            if (start == Figures.SINGLE_RED){
                switch(target){
                    case SINGLE_RED-> {
                        //all okay but capture
                        if(isNormalMove(move)){
                            legalMoves.add(move);
                        }
                        continue;
                    }
                    case SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                        //all okay but normal move
                        if(isNormalCapture(move)){
                            legalMoves.add(move);
                        }
                        continue;
                    }
                    case DOUBLE_RED, MIXED_RED -> { continue; }
                    case null -> {
                        //add all moves to empty field
                        if(isNormalMove(move)){
                            legalMoves.add(move);
                        }
                        continue;
                    }
                }
            }

            //add all RED figure capture or tower building moves
            if (start == Figures.SINGLE_BLUE){
                switch(target){
                    case SINGLE_BLUE -> {
                        //all okay but capture
                        if(isNormalMove(move)){
                            legalMoves.add(move);
                        }
                        continue;
                    }
                    case SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                        //all okay but normal move
                        if(isNormalCapture(move)){
                            legalMoves.add(move);
                        }
                        continue;
                    }
                    case DOUBLE_BLUE, MIXED_BLUE -> { continue;  }
                    case null -> {
                        //add all moves to empty field
                        if(isNormalMove(move)){
                            legalMoves.add(move);
                        }
                        continue;
                    }
                }
            }

            if (start == Figures.MIXED_RED || start == Figures.DOUBLE_RED){
                switch(target){
                    case SINGLE_RED, SINGLE_BLUE, DOUBLE_BLUE, MIXED_BLUE -> {
                        //capture and tower
                        legalMoves.add(move);
                        continue;
                    }
                    case DOUBLE_RED, MIXED_RED -> { continue; }
                    case null -> {
                        //add all moves to empty field
                        legalMoves.add(move);
                        continue;
                    }
                }
            }

            //add all RED figure capture or tower building moves
            if (start == Figures.MIXED_BLUE || start == Figures.DOUBLE_BLUE){
                switch(target){
                    case SINGLE_BLUE, SINGLE_RED, DOUBLE_RED, MIXED_RED -> {
                        //capture and tower
                        legalMoves.add(move);
                        continue;
                    }
                    case DOUBLE_BLUE, MIXED_BLUE -> { continue; }
                    case null -> {
                        //add all moves to empty field
                        legalMoves.add(move);
                        continue;
                    }
                }
            }

            System.out.println("Shouldn't be here: " + move);
            //boardToString();
        }

        return legalMoves;
    }*/

    public boolean isNormalMove(Move move){
        if(move.start_col - move.end_col == 0 && move.start_row - move.end_row != 0){
            return true;
        }

        if(move.start_col - move.end_col != 0 && move.start_row - move.end_row == 0){
            return true;
        }

        return false;
    }

    public boolean isNormalCapture(Move move){
        if(move.start_col - move.end_col != 0 && move.start_row - move.end_row != 0){
            return true;
        }

        return false;
    }

    public void makeMove(Move move){
        Figures start = board[move.start_row][move.start_col - 'a'];
        Figures target = board[move.end_row][move.end_col - 'a'];

        if (start == Figures.SINGLE_RED){
            switch (target){
                case SINGLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_RED);
                }
                case SINGLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.SINGLE_RED);

                    figureMap[1].remove(new Field(move.end_row, move.end_col));
                }
                case DOUBLE_RED, MIXED_RED -> { }
                case DOUBLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.MIXED_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.MIXED_RED);

                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.MIXED_RED);
                }
                case MIXED_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_RED);

                    figureMap[1].remove(new Field(move.end_row, move.end_col));
                }
                case null -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.SINGLE_RED);
                }
            }
        } else if (start == Figures.SINGLE_BLUE){
            switch (target){
                case SINGLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.SINGLE_BLUE);

                    figureMap[0].remove(new Field(move.end_row, move.end_col));
                }
                case SINGLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_BLUE);
                }
                case DOUBLE_BLUE, MIXED_BLUE -> { }
                case DOUBLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.MIXED_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.MIXED_BLUE);

                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.MIXED_BLUE);
                }
                case MIXED_RED -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_BLUE);

                    figureMap[0].remove(new Field(move.end_row, move.end_col));
                }
                case null -> {
                    board[move.start_row][move.start_col - 'a'] = null;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.SINGLE_BLUE);
                }
            }
        } else if (start == Figures.DOUBLE_RED){
            switch (target){
                case SINGLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_RED);
                }
                case SINGLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.SINGLE_RED);

                    figureMap[1].remove(new Field(move.end_row, move.end_col));
                }
                case DOUBLE_RED, MIXED_RED -> { }
                case DOUBLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.MIXED_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.MIXED_RED);

                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.MIXED_RED);
                }
                case MIXED_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_RED);

                    figureMap[1].remove(new Field(move.end_row, move.end_col));
                }
                case null -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.SINGLE_RED);
                }
            }
        } else if (start == Figures.DOUBLE_BLUE){
            switch (target){
                case SINGLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.SINGLE_BLUE);

                    figureMap[0].remove(new Field(move.end_row, move.end_col));
                }
                case SINGLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_BLUE);
                }
                case DOUBLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.MIXED_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.MIXED_BLUE);

                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.MIXED_BLUE);
                }
                case DOUBLE_BLUE, MIXED_BLUE -> { }
                case MIXED_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_BLUE);

                    figureMap[0].remove(new Field(move.end_row, move.end_col));
                }
                case null -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.SINGLE_BLUE);
                }
            }
        } else if (start == Figures.MIXED_RED){
            switch (target){
                case SINGLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_RED);
                }
                case SINGLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.SINGLE_RED);

                    figureMap[1].remove(new Field(move.end_row, move.end_col));
                }
                case DOUBLE_RED, MIXED_RED -> { }
                case DOUBLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.MIXED_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.MIXED_RED);

                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.MIXED_RED);
                }
                case MIXED_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_RED);

                    figureMap[1].remove(new Field(move.end_row, move.end_col));
                }
                case null -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[0].remove(new Field(move.start_row, move.start_col));
                    figureMap[1].put(new Field(move.start_row, move.start_col), Figures.SINGLE_BLUE);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.SINGLE_RED);
                }
            }
        } else if (start == Figures.MIXED_BLUE){
            switch (target){
                case SINGLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.SINGLE_BLUE);

                    figureMap[0].remove(new Field(move.end_row, move.end_col));
                }
                case SINGLE_BLUE -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_BLUE);
                }
                case DOUBLE_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.MIXED_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.MIXED_BLUE);

                    figureMap[0].put(new Field(move.end_row, move.end_col), Figures.MIXED_BLUE);
                }
                case DOUBLE_BLUE, MIXED_BLUE -> { }
                case MIXED_RED -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.DOUBLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.DOUBLE_BLUE);

                    figureMap[0].remove(new Field(move.end_row, move.end_col));
                }
                case null -> {
                    board[move.start_row][move.start_col - 'a'] = Figures.SINGLE_RED;
                    figureMap[1].remove(new Field(move.start_row, move.start_col));
                    figureMap[0].put(new Field(move.start_row, move.start_col), Figures.SINGLE_RED);

                    board[move.end_row][move.end_col - 'a'] = Figures.SINGLE_BLUE;
                    figureMap[1].put(new Field(move.end_row, move.end_col), Figures.SINGLE_BLUE);
                }
            }
        }

        if (blueToMove == 1) {
            blueToMove = 0;
        } else {
            blueToMove = 1;
        }

    }
}
