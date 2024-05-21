import java.util.*;

public class Board {
    //board = new Figures[row][column]
    //board = new Figures[2][a]
    //column uses ascii char values
    public Figures[][] board = new Figures[8][8];

    //index 0 are red figures, index 1 are blue figures
    public HashMap<Field, Figures>[] figureMap = new HashMap[2];
    public int blueToMove;

    static public HashMap<Float, Set<Move>>[] minimaxMoves = new HashMap[10];

    public static void main(String[] args) {

        Board board = new Board();

        //assumes given fen is correct
        String init = "6/8/5b0b01/8/8/5r0r01/8/6 b";
        String fen = init.substring(0, init.length() - 2);

        board.fenToBoard(fen);
        board.blueToMove = (init.charAt(init.length()-1) == 'b' ? 1 : 0);
        System.out.println("init eval: " + board.evaluatePosition());
        board.boardToString();

        boolean isMax = true;
        for (int i = 0; i < 25; i++) {

            if (board.blueToMove == 1){
                isMax = true;
            } else {
                isMax = false;
            }

            float v = alphaBeta(board, isMax);

            Move move = getMove(v);
            board.makeMove(move);
            System.out.println("Current eval: " + board.evaluatePosition());
            //System.out.println(board.minimaxMoves.get(v));
            System.out.println("alpha beta eval: " + v);
            System.out.println(Arrays.toString(minimaxMoves));
            board.boardToString();
            System.out.println();

                /*if (newBoard.blueToMove == 1) {
                    newBoard.blueToMove = 0;
                } else {
                    newBoard.blueToMove = 1;
                }*/

        }


        /*while(!board.isGameFinished(board.blueToMove)){
            Set<Move> moves = board.getPossibleMoves(board.blueToMove);
            moves = board.getLegalMoves(moves);
            Move move = moves.iterator().next();

            Random rand = new Random();
            //Move move;
            int i = rand.nextInt(moves.size());
            do {
                move = moves.iterator().next();
                --i;
            } while (i != 0);

            board.makeMove(move);
            String color = "";
            if(board.blueToMove == 1) {color = "Blue";} else {color = "Red";}

            System.out.println(color + " makes move: " + move);
            board.boardToString();

            if (board.blueToMove == 1) {
                board.blueToMove = 0;
            } else {
                board.blueToMove = 1;
            }

        }*/

    }

    public void setBoardContent(Figures[][] board, HashMap<Field, Figures>[] maps, int blueToMove) {
        this.board = board;
        this.figureMap = maps;
        this.blueToMove = blueToMove;
    }

    static public float alphaBeta(Board board, boolean isMax){
        board.minimaxMoves = new HashMap[10];
        return alphaBetaRecursion(board, 5, -10000.0f, +10000.0f, isMax, null);
    }

    static private float alphaBetaRecursion(Board board, int depth, float alpha, float beta, boolean isMax, Move firstMove) {
        if(depth == 0 || board.isGameFinished(board.blueToMove)) {
            float eval =  board.evaluatePosition();
            addMove(eval, firstMove, depth);
            return eval;
        }

        if (isMax){
            //float v = alpha;
            float v = -10000.0f;


            Set<Move> moves = board.getLegalMoves(board.getPossibleMoves(board.blueToMove));
            for (Move move : moves) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                /*if (newBoard.blueToMove == 1) {
                    newBoard.blueToMove = 0;
                } else {
                    newBoard.blueToMove = 1;
                }*/

                if (firstMove == null){
                    v = Math.max(v, alphaBetaRecursion(newBoard, depth - 1, v, beta, false, new Move(move.start_row, move.start_col, move.end_row, move.end_col)));
                } else {
                    v = Math.max(v, alphaBetaRecursion(newBoard, depth - 1, v, beta, false, firstMove));
                }

                alpha = Math.max(alpha, v);

                if(v >= beta) break;

                //board.minimaxMoves.put(v, move);

            }
            return v;
        } else {
            //float v = beta;
            float v = 10000.0f;

            Set<Move> moves = board.getLegalMoves(board.getPossibleMoves(board.blueToMove));
            for (Move move : moves) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                /*if (newBoard.blueToMove == 1) {
                    newBoard.blueToMove = 0;
                } else {
                    newBoard.blueToMove = 1;
                }*/

                if (firstMove == null){
                    v = Math.min(v, alphaBetaRecursion(newBoard, depth - 1, alpha, v, true, new Move(move.start_row, move.start_col, move.end_row, move.end_col)));
                } else {
                    v = Math.min(v, alphaBetaRecursion(newBoard, depth - 1, alpha, v, true, firstMove));
                }

                beta = Math.min(beta, v);

                if(v <= alpha) break;

                //board.minimaxMoves.put(v, move);
            }
            return v;
        }
    }

    static public Move getMove(float v) {
        //TODO modify: if chosen move (at the end) is present somewhere with a lower depth and the eval is better or worse it has to be dropped and a new move has to be selected

        for (int i = minimaxMoves.length - 1; -1 < i; i--) {
            HashMap<Float, Set<Move>> map = minimaxMoves[i];
            if (map == null) continue;
            if(!map.containsKey(v)) continue;
            return map.get(v).iterator().next();
        }
        return null;
    }

    static public void addMove(float v, Move move, int depth) {
        if (minimaxMoves[depth] == null) minimaxMoves[depth] = new HashMap();
        if (!minimaxMoves[depth].containsKey(v)) {
            minimaxMoves[depth].put(v, new HashSet<>());
        }

        minimaxMoves[depth].get(v).add(move);
    }

    static public float miniMax(Board board, boolean isMax) {
        minimaxMoves = new HashMap[10];
        return miniMaxRecursion(board, 5, isMax, null);
    }

    static private float miniMaxRecursion(Board board, int depth, boolean isMax, Move firstMove) {
        if(depth == 0 || board.isGameFinished(board.blueToMove)) {
            float eval =  board.evaluatePosition();
            //System.out.println();
            //System.out.println("end pos eval: " + eval);
            //board.boardToString();
            //System.out.println();
            //minimaxMoves.put(eval, firstMove);
            //if(eval == 5.0f)System.out.println(depth + " " + firstMove);
            addMove(eval, firstMove, depth);
            return eval;
        }

        if (isMax){
            float v = -10000.0f;

            Set<Move> moves = board.getLegalMoves(board.getPossibleMoves(board.blueToMove));
            for (Move move : moves) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                /*if (newBoard.blueToMove == 1) {
                    newBoard.blueToMove = 0;
                } else {
                    newBoard.blueToMove = 1;
                }*/

                //System.out.println();
                //System.out.println("make move: " + move + ", evals to: " + newBoard.evaluatePosition());
                //newBoard.boardToString();
                //System.out.println();

                if (firstMove == null){
                    v = Math.max(v, miniMaxRecursion(newBoard, depth - 1, false, new Move(move.start_row, move.start_col, move.end_row, move.end_col)));
                } else {
                    v = Math.max(v, miniMaxRecursion(newBoard, depth - 1, false, firstMove));
                }

                //board.minimaxMoves.put(v, move);
            }
            return v;
        } else {
            float v = +10000.0f;

            Set<Move> moves = board.getLegalMoves(board.getPossibleMoves(board.blueToMove));
            for (Move move : moves) {
                Board newBoard = new Board();
                newBoard.setBoardContent(board.deepCopyBoard(), board.deepCopyBoardMap(), board.blueToMove);
                newBoard.makeMove(move);

                /*if (newBoard.blueToMove == 1) {
                    newBoard.blueToMove = 0;
                } else {
                    newBoard.blueToMove = 1;
                }*/

                //System.out.println();
                //System.out.println("make move: " + move + ", evals to: " + newBoard.evaluatePosition());
                //newBoard.boardToString();
                //System.out.println();

                if (firstMove == null){
                    v = Math.min(v, miniMaxRecursion(newBoard, depth - 1, true, new Move(move.start_row, move.start_col, move.end_row, move.end_col)));
                } else {
                    v = Math.min(v, miniMaxRecursion(newBoard, depth - 1, true, firstMove));
                }

                //board.minimaxMoves.put(v, move);
            }
            return v;
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

    //TODO: implement evaluation function
    public float evaluatePosition(){
        if(figureMap[0].isEmpty()) return +10000.0f;
        if(figureMap[1].isEmpty()) return -10000.0f;

        float value = 0;

        for (Map.Entry<Field, Figures> entry : figureMap[1].entrySet()) {
            if (entry.getKey().row == 7) return +10000.0f;

            if (entry.getValue() == Figures.DOUBLE_BLUE){
                value += 20;
                value += (float) ((entry.getKey().row + 1) * 2.5);
            } else {
                value += 10;
            }

            value += (float) ((entry.getKey().row + 1) * 2.5);
        }

        for (Map.Entry<Field, Figures> entry : figureMap[0].entrySet()) {
            if (entry.getKey().row == 0) return -10000.0f;

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
    }

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
                    /*if (entry.getKey().col == 0){
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col+1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row-1), entry.getKey().col));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row-1), entry.getKey().col+1));
                    } else if (entry.getKey().col == 7) {
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col-1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row-1), entry.getKey().col));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row-1), entry.getKey().col-1));
                    }  else {
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col+1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row-1), entry.getKey().col));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row-1), entry.getKey().col+1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col-1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row-1), entry.getKey().col-1));
                    }*/
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row-1, entry.getKey().col));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, (entry.getKey().row-1), (char) (entry.getKey().col+1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, (char) (entry.getKey().col-1)));
                    moves.add(new Move(entry.getKey().row, entry.getKey().col, (entry.getKey().row-1), (char) (entry.getKey().col-1)));
                }
                case SINGLE_BLUE -> {
                    /*if (entry.getKey().col == 0){
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col+1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row+1), entry.getKey().col));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row+1), entry.getKey().col+1));
                    } else if (entry.getKey().col == 7) {
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col-1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row+1), entry.getKey().col));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row+1), entry.getKey().col-1));
                    }  else {
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col+1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row+1), entry.getKey().col));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row+1), entry.getKey().col+1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, entry.getKey().row, entry.getKey().col-1));
                        moves.add(new Move(entry.getKey().row, entry.getKey().col, (char) (entry.getKey().row+1), entry.getKey().col-1));
                    }*/
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
    }

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
    }

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
