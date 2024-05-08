public class Move {
    public int start_row;
    public char start_col;
    public int end_row;
    public char end_col;

    public Move(int start_row, char start_col, int end_row, char end_col) {
        this.start_row = start_row;
        this.start_col = start_col;
        this.end_row = end_row;
        this.end_col = end_col;
    }

    @Override
    public String toString() {
        return start_col + "" + (start_row + 1) + "-" + end_col + (end_row + 1);
    }
}
