import java.util.Objects;



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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return start_row == move.start_row &&
                start_col == move.start_col &&
                end_row == move.end_row &&
                end_col == move.end_col;
    }




    @Override
    public int hashCode() {
        return Objects.hash(start_row, start_col, end_row, end_col);
    }

    @Override
    public String toString() {
        return (char)(start_col - 32) + "" + (start_row + 1) + "-" + (char)(end_col - 32) + (end_row + 1);
    }
}