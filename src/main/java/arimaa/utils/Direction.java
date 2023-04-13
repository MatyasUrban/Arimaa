package arimaa.utils;

import java.util.ArrayList;

/**
 * The Direction enum represents the four possible directions a piece can move in Arimaa.
 */
public enum Direction {
    NORTH(-1, 0, "n"),
    SOUTH(1, 0, "s"),
    EAST(0, 1, "e"),
    WEST(0, -1, "w"),
    NONE(0, 0, "x");

    private final int dRow;
    private final int dColumn;
    private final String notation;

    /**
     * Constructs direction object given dRow, dColumn, notation.
     * @param dRow Row difference = Destination row minus original row.
     * @param dColumn Column difference = Destination column minus original column.
     * @param notation String one character notation of the move.
     */
    Direction(int dRow, int dColumn, String notation) {
        this.dRow = dRow;
        this.dColumn = dColumn;
        this.notation = notation;
    }

    /**
     * Returns the opposite direction of the current direction.
     *
     * @return The opposite direction.
     */
    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case NONE -> NONE;
        };
    }

    /**
     * Gets the row difference.
     *
     * @return The row difference.
     */
    public int getDRow() {
        return dRow;
    }

    /**
     * Gets the column difference.
     *
     * @return The column difference.
     */
    public int getDColumn() {
        return dColumn;
    }

    /**
     * Gets the String one character notation.
     *
     * @return The String one character notation.
     */
    public String getNotation() {
        return notation;
    }

    /**
     * Method to create a Direction object corresponding to the given notation.
     *
     * @param notation The direction notation to convert.
     * @return The Direction object corresponding to the given notation, or null if the notation is invalid.
     */
    public static Direction fromNotation(char notation) {
        for (Direction direction : Direction.values()) {
            if (direction.getNotation().equals(String.valueOf(notation))) {
                return direction;
            }
        }

        return null;
    }

    /**
     * Method to get four directions as an array list.
     * Needed since Direction.values() is not applicable in my use case.
     *
     * @return ArrayList<Direction> of directions.
     */
    public static ArrayList<Direction> getFourDirections() {
        ArrayList<Direction> arrayList = new ArrayList<>();
        arrayList.add(NORTH);
        arrayList.add(WEST);
        arrayList.add(SOUTH);
        arrayList.add(EAST);
        return arrayList;
    };


}
