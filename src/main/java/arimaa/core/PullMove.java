package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.Position;

/**
 * The PullMove class represents a pull move in the Arimaa game.
 */
public class PullMove extends Move {
    /**
     * Instance variable: Starting position of pulled piece
     */
    private final Position pulledPieceFrom;
    /**
     * Instance variable: Destination position of the pulled piece
     */
    private final Position pulledPieceTo;

    /**
     * Constructs a new PullMove object.
     *
     * @param from            The starting position of the pulling piece.
     * @param to              The destination position of the pulling piece.
     * @param pulledPieceFrom The starting position of the pulled piece.
     * @param pulledPieceTo   The destination position of the pulled piece.
     */
    public PullMove(Position from, Position to, Position pulledPieceFrom, Position pulledPieceTo) {
        super(from, to);
        this.pulledPieceFrom = pulledPieceFrom;
        this.pulledPieceTo = pulledPieceTo;
    }

    /**
     * Gets the starting position of the pulled piece.
     *
     * @return The starting position of the pulled piece.
     */
    public Position getPulledPieceFrom() {
        return pulledPieceFrom;
    }

    /**
     * Gets the destination position of the pulled piece.
     *
     * @return The destination position of the pulled piece.
     */
    public Position getPulledPieceTo() {
        return pulledPieceTo;
    }

    /**
     * Method to get the Direction of the pulled piece (since PushMove.getDirection is of the pulling piece).
     *
     * @return The direction of the pulled piece.
     */
    public Direction getPulledPieceDirection(){
        int rowDiff = pulledPieceTo.row() - pulledPieceFrom.row();
        int columnDiff = pulledPieceTo.column() - pulledPieceFrom.column();

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
