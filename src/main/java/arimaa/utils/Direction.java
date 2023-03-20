package arimaa.utils;

/**
 * The Direction enum represents the four possible directions a piece can move in Arimaa.
 */
public enum Direction {
    NORTH(0, 1, "n"),
    SOUTH(0, -1, "s"),
    EAST(1, 0, "e"),
    WEST(-1, 0, "w");

    private final int dRow;
    private final int dColumn;
    private final String notation;

    Direction(int dRow, int dColumn, String notation) {
        this.dRow = dRow;
        this.dColumn = dColumn;
        this.notation = notation;
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
}
