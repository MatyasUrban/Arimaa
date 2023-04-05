package arimaa.utils;

import java.util.ArrayList;

/**
 * The Position record represents a position on the Arimaa game board.
 *
 * @param row The row of the position.
 * @param column The column of the position.
 */
public record Position(int row, int column) {

    /**
     * Gets the row number of the position.
     *
     * @return The row number of the position.
     */
    @Override
    public int row() {
        return row;
    }

    /**
     * Gets the column number of the position.
     *
     * @return The column number of the position.
     */
    @Override
    public int column() {
        return column;
    }

    /**
     * Constant for the trap positions (any piece being here without a friendly adjacent piece shall be removed)
     */
    public static final Position[] TRAP_POSITIONS = new Position[]{
            new Position(2, 2),
            new Position(2, 5),
            new Position(5, 2),
            new Position(5, 5)
    };

    /**
     * Constant for the upper row (gold rabbit being in the row is a winning condition)
     */
    public static final Position[] GOLD_GOAL_ROW = new Position[]{
            new Position(0, 0),
            new Position(0, 1),
            new Position(0, 2),
            new Position(0, 4),
            new Position(0, 5),
            new Position(0, 6),
            new Position(0, 7)
    };

    /**
     * Constant for the bottom row (silver rabbit being in the row is a winning condition)
     */
    public static final Position[] SILVER_GOAL_ROW = new Position[]{
            new Position(7, 0),
            new Position(7, 1),
            new Position(7, 2),
            new Position(7, 4),
            new Position(7, 5),
            new Position(7, 6),
            new Position(7, 7)
    };

    /**
     * Method to get the new position after move in designated direction.
     *
     * @param direction The direction of the move.
     * @return The position after the move.
     */
    public Position move(Direction direction) {
        return new Position(row + direction.getDRow(), column + direction.getDColumn());
    }

    /**
     * Creates a Position object from a string in the format "b1".
     *
     * @param positionString The position string to convert.
     * @return The Position object corresponding to the given string, or null if the string is invalid.
     */
    public static Position fromString(String positionString) {
        if (positionString.length() != 2) {
            return null;
        }
        char rowChar = positionString.charAt(1);
        char columnChar = positionString.charAt(0);
        int rowInt = switch (rowChar) {
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
        int columnInt = switch (columnChar) {
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
     * Method to get the valid adjacent position in the specified direction.
     * If the new position is out of bounds of the board, null is returned.
     *
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

    /**
     * Method to get all valid adjacent positions in given directions arrayList.
     *
     * @param directionArrayList List of directions in which we want to get positions.
     * @return List of valid positions.
     */
    public ArrayList<Position> getAdjacentPositions(ArrayList<Direction> directionArrayList) {
        ArrayList<Position> positionsArrayList = new ArrayList<>();
        for (Direction direction : directionArrayList) {
            Position adjacentPosition = getAdjacentPosition(direction);
            if (adjacentPosition != null) {
                positionsArrayList.add(adjacentPosition);
            }
        }
        return positionsArrayList;
    }

    /**
     * Method to check if both position objects refer to the same position.
     *
     * @param o   the reference object with which to compare.
     * @return Boolean value of the check.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    /**
     * Method to get the string representation of the position.
     * column 0-1 -> a-h; row 0-1 -> 8-1
     *
     * @return String representation of the board.
     */
    @Override
    public String toString() {
        char rowChar = switch (row()) {
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
        char columnChar = switch (column()) {
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

    /**
     * Method to convert array list with position objects to array with position objects.
     *
     * @param positionArrayList Array list with positions.
     * @return Array with positions.
     */
    public static Position[] arrayListToArray(ArrayList<Position> positionArrayList) {
        Position[] positions = new Position[positionArrayList.size()];
        int i = 0;
        for (Position onePosition : positionArrayList) {
            positions[i] = onePosition;
            i++;
        }
        return positions;
    }
}
