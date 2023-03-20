package arimaa.utils;

import java.util.Objects;

/**
 * The Position class represents a position on the Arimaa game board.
 */
public class Position {
    private final int row;
    private final int column;

    /**
     * Constructs a new Position object with the specified row and column.
     * @param row    The row of the position.
     * @param column The column of the position.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Creates a Position object from a string in the format "b1".
     * @param positionString The position string to convert.
     * @return The Position object corresponding to the given string, or null if the string is invalid.
     */
    public static Position fromString(String positionString) {
        if (positionString.length() != 2) {
            return null;
        }

        char columnChar = positionString.charAt(0);
        char rowChar = positionString.charAt(1);

        if (columnChar < 'a' || columnChar > 'h' || rowChar < '1' || rowChar > '8') {
            return null;
        }

        int column = columnChar - 'a';
        int row = rowChar - '1';

        return new Position(row, column);
    }

    // Add getters, setters, and other necessary methods here (e.g., equals, hashCode, and toString)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        char columnChar = (char) ('a' + column);
        char rowChar = (char) ('1' + row);
        return String.valueOf(columnChar) + rowChar;
    }
}
