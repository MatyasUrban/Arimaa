package arimaa.utils;

/**
 * The Pair class represents a pair of two related elements.
 * @param <Piece> The type of the first element in the pair.
 * @param <Position> The type of the second element in the pair.
 */
public class PiecePositionPair<Piece, Position> {
    private final Piece piece;
    private final Position position;

    /**
     * Constructs a new PiecePositionPair object with the specified first and second elements.
     * @param piece  The first element in the pair.
     * @param position The second element in the pair.
     */
    public PiecePositionPair(Piece piece, Position position) {
        this.piece = piece;
        this.position = position;
    }

    /**
     * Returns the first element in the pair.
     * @return The first element in the pair.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Returns the second element in the pair.
     * @return The second element in the pair.
     */
    public Position getPosition() {
        return position;
    }
}
