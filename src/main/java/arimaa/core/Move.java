package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.Position;

import java.util.Objects;

/**
 * The Move class represents a move in the Arimaa game.
 */
public abstract class Move {
    private final Position from;
    private final Position to;

    /**
     * Constructs a new Move object.
     * @param from The starting position of the piece being moved.
     * @param to The destination position of the piece being moved.
     */
    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the starting position of the piece being moved.
     * @return The starting position of the piece being moved.
     */
    public Position getFrom() {
        return from;
    }

    /**
     * Gets the destination position of the piece being moved.
     * @return The destination position of the piece being moved.
     */
    public Position getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Move{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) &&
                Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    /**
     * Checks if two positions are adjacent on the board.
     * @param pos1 The first position.
     * @param pos2 The second position.
     * @return True if the positions are adjacent, false otherwise.
     */
    public static boolean isAdjacent(Position pos1, Position pos2) {
        int rowDiff = Math.abs(pos1.getRow() - pos2.getRow());
        int columnDiff = Math.abs(pos1.getColumn() - pos2.getColumn());
        return (rowDiff == 1 && columnDiff == 0) || (rowDiff == 0 && columnDiff == 1);
    }

    /**
     * Returns the direction of the move.
     * @return The direction of the move.
     */
    public Direction getDirection() {
        int rowDiff = to.getRow() - from.getRow();
        int columnDiff = to.getColumn() - from.getColumn();

        if (rowDiff == 1 && columnDiff == 0) {
            return Direction.NORTH;
        } else if (rowDiff == -1 && columnDiff == 0) {
            return Direction.SOUTH;
        } else if (rowDiff == 0 && columnDiff == 1) {
            return Direction.EAST;
        } else if (rowDiff == 0 && columnDiff == -1) {
            return Direction.WEST;
        }

        return null; // Should not happen for valid moves
    }
}

