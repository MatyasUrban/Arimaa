package arimaa.utils;

import java.util.ArrayList;
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
        char rowChar = positionString.charAt(1);
        char columnChar = positionString.charAt(0);
        int rowInt = switch (rowChar){
            case '1' -> 7;
            case '2' -> 6;
            case '3' -> 5;
            case '4' -> 4;
            case '5' -> 3;
            case '6' -> 2;
            case '7' -> 1;
            case '8' -> 0;
            default -> 0;
        };
        int columnInt = switch (columnChar){
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            default -> 0;
        };

        return new Position(rowInt, columnInt);
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

    public ArrayList<Position> getAdjacentPositions(ArrayList<Direction> directionArrayList){
        ArrayList<Position> positionsArrayList = new ArrayList<>();
        for (Direction direction : directionArrayList){
            Position adjacentPosition = getAdjacentPosition(direction);
            if (adjacentPosition != null){
                positionsArrayList.add(adjacentPosition);
            }
        }
        return positionsArrayList;
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
        char rowChar = switch (getRow()){
            case 0 -> '8';
            case 1 -> '7';
            case 2 -> '6';
            case 3 -> '5';
            case 4 -> '4';
            case 5 -> '3';
            case 6 -> '2';
            case 7 -> '1';
            default -> '1';
        };
        char columnChar = switch (getColumn()) {
            case 0 -> 'a';
            case 1 -> 'b';
            case 2 -> 'c';
            case 3 -> 'd';
            case 4 -> 'e';
            case 5 -> 'f';
            case 6 -> 'g';
            case 7 -> 'h';
            default -> 'a';
        };
        return String.valueOf(columnChar) + rowChar;
    }

    public static Position[] arrayListToArray(ArrayList<Position> positionArrayList){
        Position[] positions = new Position[positionArrayList.size()];
        int i = 0;
        for(Position onePosition : positionArrayList){
            positions[i] = onePosition;
            i++;
        }
        return positions;
    }

    public static final Position[] trapPositions = new Position[]{
            new Position(2,2),
            new Position(2,5),
            new Position(5,2),
            new Position(5,5)
    };
    public static final Position[] goldGoalRow = new Position[]{
            new Position(0, 0),
            new Position(0, 1),
            new Position(0, 2),
            new Position(0, 4),
            new Position(0, 5),
            new Position(0, 6),
            new Position(0, 7)
    };
    public static final Position[] silverGoalRow = new Position[]{
            new Position(7, 0),
            new Position(7, 1),
            new Position(7, 2),
            new Position(7, 4),
            new Position(7, 5),
            new Position(7, 6),
            new Position(7, 7)
    };
}
