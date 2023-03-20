package arimaa.core;

import arimaa.utils.Position;

import java.util.Objects;

/**
 * The Move class represents a move in the Arimaa game.
 */
public class Move {
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
}

