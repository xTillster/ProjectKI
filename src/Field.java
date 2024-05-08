import java.util.Objects;

public class Field {
    public int row;
    public char col;

    public Field(int row, char col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return col + "" + (row + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return row == field.row && col == field.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
