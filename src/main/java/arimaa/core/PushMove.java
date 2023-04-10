package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.Position;

/**
 * The PushMove class represents a push move in the Arimaa game.
 */
public class PushMove extends Move {
    /**
     * Instance variable: Starting position of pushed piece
     */
    private final Position pushedPieceFrom;
    /**
     * Instance variable: Destination position of the pushed piece
     */
    private final Position pushedPieceTo;

    /**
     * Constructs a new PushMove object.
     *
     * @param from            The starting position of the pushing piece.
     * @param to              The destination position of the pushing piece.
     * @param pushedPieceFrom The starting position of the pushed piece.
     * @param pushedPieceTo   The destination position of the pushed piece.
     */
    public PushMove(Position from, Position to, Position pushedPieceFrom, Position pushedPieceTo) {
        super(from, to);
        this.pushedPieceFrom = pushedPieceFrom;
        this.pushedPieceTo = pushedPieceTo;
    }

    /**
     * Gets the starting position of the pushed piece.
     *
     * @return The starting position of the pushed piece.
     */
    public Position getPushedPieceFrom() {
        return pushedPieceFrom;
    }

    /**
     * Gets the destination position of the pushed piece.
     *
     * @return The destination position of the pushed piece.
     */
    public Position getPushedPieceTo() {
        return pushedPieceTo;
    }

    public Direction getPushedPieceDirection(){
        int rowDiff = pushedPieceTo.row() - pushedPieceFrom.row();
        int columnDiff = pushedPieceTo.column() - pushedPieceFrom.column();

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

}
