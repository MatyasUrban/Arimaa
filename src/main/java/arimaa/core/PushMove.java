package arimaa.core;

import arimaa.utils.Position;

public class PushMove extends Move {
    private final Position pushedPieceFrom;
    private final Position pushedPieceTo;

    /**
     * Constructs a new PushMove object.
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
     * @return The starting position of the pushed piece.
     */
    public Position getPushedPieceFrom() {
        return pushedPieceFrom;
    }

    /**
     * Gets the destination position of the pushed piece.
     * @return The destination position of the pushed piece.
     */
    public Position getPushedPieceTo() {
        return pushedPieceTo;
    }

}
