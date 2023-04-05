package arimaa.core;

import arimaa.utils.Position;

/**
 * This StepMove class represents a single 1-step move in the
 */
public class StepMove extends Move {
    /**
     * Constructs a new StepMove object.
     *
     * @param from The starting position of the piece being moved.
     * @param to   The destination position of the piece being moved.
     */
    public StepMove(Position from, Position to) {
        super(from, to);
    }

}