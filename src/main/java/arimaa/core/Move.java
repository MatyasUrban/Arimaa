package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.Position;

import java.util.Objects;

/**
 * The Move class represents a move in the Arimaa game.
 */
public abstract class Move {
    /**
     * Instance variable: coordinates of the starting position
     */
    private final Position from;
    /**
     * Instance variable: coordinates of the destination positions
     */
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
     * Method to get the starting position of the piece being moved.
     *
     * @return Position object: The starting position of the piece being moved.
     */
    public Position getFrom() {
        return from;
    }

    /**
     * Method to get the destination position of the piece being moved.
     *
     * @return Position object: The destination position of the piece being moved.
     */
    public Position getTo() {
        return to;
    }

    /**
     * Method to checked whether two moves are the same in terms of starting and destination Position objects.
     *
     * @param o   the reference object with which to compare.
     * @return Boolean value of the check.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    /**
     * Method to determine the direction of the move.
     *
     * @return The direction enum of the direction of the move.
     */
    public Direction getDirection() {
        int rowDiff = to.row() - from.row();
        int columnDiff = to.column() - from.column();

        if (rowDiff == 1 && columnDiff == 0) {
            return Direction.SOUTH;
        } else if (rowDiff == -1 && columnDiff == 0) {
            return Direction.NORTH;
        } else if (rowDiff == 0 && columnDiff == 1) {
            return Direction.EAST;
        } else if (rowDiff == 0 && columnDiff == -1) {
            return Direction.WEST;
        } else {
            return Direction.NONE;
        }
    }

    @Override
    public String toString(){
        return from + getDirection().getNotation();
    }
}

