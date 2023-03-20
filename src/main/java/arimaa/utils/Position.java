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
     * Gets the row number of the position.
     * @return The row number of the position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the new position after move in designated direction.
     * @param direction The direction of the move.
     * @return The position after the move.
     */
    public Position move(Direction direction) {
        return new Position(row + direction.getDRow(), column + direction.getDColumn());
    }

    /**
     * Gets the column number of the position.
     * @return The column number of the position.
     */
    public int getColumn() {
        return column;
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

    /**
     * Gets the adjacent position in the specified direction.
     * @param direction The direction to get the adjacent position.
     * @return The adjacent position in the specified direction, or null if the position is outside the board boundaries.
     */
    public Position getAdjacentPosition(Direction direction) {
        int newRow = this.row + direction.getDRow();
        int newColumn = this.column + direction.getDColumn();

        if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
            return new Position(newRow, newColumn);
        }

        return null;
    }

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
