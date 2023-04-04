package arimaa.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    Direction(int dRow, int dColumn, String notation) {
        this.dRow = dRow;
        this.dColumn = dColumn;
        this.notation = notation;
    }

    /**
     * Returns the opposite direction of the current direction.
     * @return The opposite direction.
     */
    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case NONE -> NONE;
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }

    public int getDRow() {
        return dRow;
    }

    public int getDColumn() {
        return dColumn;
    }

    public String getNotation() {
        return notation;
    }

    /**
     * Returns the Direction object corresponding to the given notation.
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
    public static ArrayList<Direction> getFourDirections() {
        ArrayList<Direction> arrayList = new ArrayList<>();
        arrayList.add(NORTH);
        arrayList.add(WEST);
        arrayList.add(SOUTH);
        arrayList.add(EAST);
        return arrayList;
    };
}
