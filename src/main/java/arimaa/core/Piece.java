package arimaa.core;
/**
 * Represents a single piece on the Arimaa board with its type and owner.
 */
public class Piece {
    public enum PieceType{
        RABBIT("Rabbit"),
        CAT("Cat"),
        DOG("Dog"),
        HORSE("Horse"),
        CAMEL("Camel"),
        ELEPHANT("Elephant");
        private final String displayName;

        PieceType(String displayName){
            this.displayName = displayName;
        }

        @Override
        public String toString(){
            return this.displayName;
        }
    }

    private final PieceType type;
    private final Player owner;

    /**
     * Constructs a new Piece with the specified type and owner.
     * @param type  The type of the piece (RABBIT, CAT, DOG, HORSE, CAMEL, ELEPHANT).
     * @param owner The owner of the piece (Player object).
     */
    public Piece(PieceType type, Player owner){
        this.type = type;
        this.owner = owner;
    }

    /**
     * Returns the type of the piece.
     * @return The type of the piece (RABBIT, CAT, DOG, HORSE, CAMEL, ELEPHANT).
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Returns the owner of the piece.
     * @return The owner of the piece (Player object).
     */
    public Player getOwner() {
        return owner;
    }
}
