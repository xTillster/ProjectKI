import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Test
    void fenToBoardEmptyBoard() {
        String fen = "6/8/8/8/8/8/8/6";
        Board.fenToBoard(fen);

        // Verify the board is empty
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertNull(Board.board[i][j]);
            }
        }
    }

    @Test
    public void fenToBoardSingleRedPieces() {
        String fen = "6/8/8/8/8/8/8/r0r0r0r02";
        Board.fenToBoard(fen);

        // Verify the specific positions have SINGLE_RED pieces
        assertEquals(Figures.SINGLE_RED, Board.board[7][1]);
        assertEquals(Figures.SINGLE_RED, Board.board[7][2]);
        assertEquals(Figures.SINGLE_RED, Board.board[7][3]);
        assertEquals(Figures.SINGLE_RED, Board.board[7][4]);
    }

    @Test
    public void fenToBoardSingleBluePieces() {
        String fen = "b0b0b0b02/8/8/8/8/8/8/6";
        Board.fenToBoard(fen);

        // Verify the specific positions have SINGLE_BLUE pieces
        assertEquals(Figures.SINGLE_BLUE, Board.board[0][1]);
        assertEquals(Figures.SINGLE_BLUE, Board.board[0][2]);
        assertEquals(Figures.SINGLE_BLUE, Board.board[0][3]);
        assertEquals(Figures.SINGLE_BLUE, Board.board[0][4]);
    }

    @Test
    public void fenToBoardStackedPieces() {
        String fen = "6/8/8/8/8/8/rb7/6";
        Board.fenToBoard(fen);

        // Verify the specific positions have MIXED pieces
        assertEquals(Figures.MIXED_BLUE, Board.board[6][0]);
    }


    @Test
    public void fenToBoardCornersAreBlank() {
        String fen = "6/8/8/8/8/8/8/r0r0r0r02";
        Board.fenToBoard(fen);

        // Verify corners are blank
        assertNull(Board.board[0][0]);
        assertNull(Board.board[0][7]);
        assertNull(Board.board[7][0]);
        assertNull(Board.board[7][7]);
    }

    @Test
    public void boardToStringEmptyBoard() {
        boardToStringSetUp();
        Board.board = new Figures[8][8];
        Board.boardToString();

        String expectedOutput = """
                8|    |    |    |    |    |    |    |    |
                7|    |    |    |    |    |    |    |    |
                6|    |    |    |    |    |    |    |    |
                5|    |    |    |    |    |    |    |    |
                4|    |    |    |    |    |    |    |    |
                3|    |    |    |    |    |    |    |    |
                2|    |    |    |    |    |    |    |    |
                1|    |    |    |    |    |    |    |    |
                    a    b    c    d    e    f    g    h
                """;

        assertEquals(normalize(expectedOutput), normalize(outContent.toString()));

        boardToStringTearDown();
    }

    @Test
    public void boardToStringSingleRedPieces() {
        boardToStringSetUp();
        Board.board[7][1] = Figures.SINGLE_RED;
        Board.board[7][3] = Figures.SINGLE_RED;
        Board.board[7][5] = Figures.SINGLE_RED;
        Board.board[7][7] = Figures.SINGLE_RED;

        Board.boardToString();

        String expectedOutput =
                """
                        8|    | r0 |    | r0 |    | r0 |    | r0 |
                        7|    |    |    |    |    |    |    |    |
                        6|    |    |    |    |    |    |    |    |
                        5|    |    |    |    |    |    |    |    |
                        4|    |    |    |    |    |    |    |    |
                        3|    |    |    |    |    |    |    |    |
                        2|    |    |    |    |    |    |    |    |
                        1|    |    |    |    |    |    |    |    |
                            a    b    c    d    e    f    g    h
                        """;

        assertEquals(normalize(expectedOutput), normalize(outContent.toString()));
        boardToStringTearDown();
    }

    @Test
    public void boardToStringMixedPieces() {
        boardToStringSetUp();
        Board.board[7][1] = Figures.MIXED_RED;
        Board.board[7][3] = Figures.MIXED_BLUE;

        Board.boardToString();

        String expectedOutput = """
                8|    | br |    | rb |    |    |    |    |
                7|    |    |    |    |    |    |    |    |
                6|    |    |    |    |    |    |    |    |
                5|    |    |    |    |    |    |    |    |
                4|    |    |    |    |    |    |    |    |
                3|    |    |    |    |    |    |    |    |
                2|    |    |    |    |    |    |    |    |
                1|    |    |    |    |    |    |    |    |
                    a    b    c    d    e    f    g    h
                """;

        assertEquals(normalize(expectedOutput), normalize(outContent.toString()));
        boardToStringTearDown();
    }

    @Test
    public void boardToStringComplexBoard() {
        boardToStringSetUp();
        Board.board[7][0] = Figures.SINGLE_RED;
        Board.board[7][1] = Figures.DOUBLE_RED;
        Board.board[7][2] = Figures.SINGLE_BLUE;
        Board.board[7][3] = Figures.DOUBLE_BLUE;
        Board.board[7][4] = Figures.MIXED_RED;
        Board.board[7][5] = Figures.MIXED_BLUE;

        Board.boardToString();

        String expectedOutput = """
                8| r0 | rr | b0 | bb | br | rb |    |    |
                7|    |    |    |    |    |    |    |    |
                6|    |    |    |    |    |    |    |    |
                5|    |    |    |    |    |    |    |    |
                4|    |    |    |    |    |    |    |    |
                3|    |    |    |    |    |    |    |    |
                2|    |    |    |    |    |    |    |    |
                1|    |    |    |    |    |    |    |    |
                    a    b    c    d    e    f    g    h
                """;

        assertEquals(normalize(expectedOutput), normalize(outContent.toString()));
        boardToStringTearDown();
    }

    @Test
    void isGameFinishedNoRedFigures() {
        Board.fenToBoard("6/8/8/8/8/b07/8/6");
        assertTrue(Board.isGameFinished(0));
    }

    @Test
    void isGameFinishedNoBlueFigures() {
        Board.fenToBoard("6/8/8/r07/8/8/8/6");
        assertTrue(Board.isGameFinished(1));
    }

    @Test
    void isGameFinishedNoLegalMoves() {
        //Blue figures blocked by red figures or in a mixed stack with red on top
        Board.fenToBoard("6/8/8/r0b0r05/1r07/br7/br7/6");
        System.out.println(Board.getLegalMoves(Board.getPossibleMoves(1)));
        assertTrue(Board.isGameFinished(1));
    }

    @Test
    void isGameFinishedRedFigureOnBlueHomeRow() {
        Board.fenToBoard("r05/8/br7/8/rr2rb4/b07/8/6");
        assertTrue(Board.isGameFinished(0));
    }


    @Test
    void isGameFinishedBlueFigureOnRedHomeRow() {
        Board.fenToBoard("6/8/br7/8/rr2rb4/b07/8/b05");
        assertTrue(Board.isGameFinished(1));
    }


    @Test
    void getPossibleMoves() {
    }

    @Test
    void getLegalMoves() {
    }

    @Test
    void isNormalMove() {
    }

    @Test
    void isNormalCapture() {
    }

    @Test
    void makeMove() {
    }

    private void boardToStringSetUp() {
        // Redirect System.out to capture the printed output
        System.setOut(new PrintStream(outContent));
        Board.board = new Figures[8][8];
    }

    private void boardToStringTearDown() {
        // Restore the original System.out
        System.setOut(originalOut);
    }

    private String normalize(String input) {
        return input.replace("\r\n", "\n").replace("\r", "\n");
    }

    private void getPossibleMoveSetUp() {
        // Initialize the board and figureMap before each test
        Board.board = new Figures[8][8];
        Board.figureMap = new HashMap[2];
        Board.figureMap[0] = new HashMap<>();
        Board.figureMap[1] = new HashMap<>();
    }
}