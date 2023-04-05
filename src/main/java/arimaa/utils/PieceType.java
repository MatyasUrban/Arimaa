package arimaa.utils;

/**
 * Enum representing piece type in the Arimaa game.
 */
public enum PieceType{
    RABBIT('r', "rabbit"),
    CAT('c', "cat"),
    DOG('d', "dog"),
    HORSE('h', "horse"),
    CAMEL('m', "camel"),
    ELEPHANT('e', "elephant");
    private final char notation;
    private final String name;

    /**
     * Constructs PieceType object given notation and name.
     *
     * @param notation One of the following characters: [EMHCDRemhcdr]
     * @param name String meaning of the character
     */
    PieceType(char notation, String name) {
        this.notation = notation;
        this.name = name;
    }

    /**
     * Gets the char notation of the piece type.
     *
     * @return The char notation of the piece type.
     */
    public char getNotation(){
        return notation;
    }

    /**
     * Gets the name of the piece type.
     *
     * @return The char notation of the piece type.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to determine, whether PieceType is stronger than given PieceType.
     *
     * @param otherType PieceType object compared in strength against.
     * @return Boolean value of the check.
     */
    public boolean isStrongerThan(PieceType otherType) {
        return this.ordinal() > otherType.ordinal();
    }

    /**
     * Method to create a PieceType object given its character Arimaa notation.
     *
     * @param notation Character notation of the PieceType per Arimaa rules.
     * @return PieceType object.
     */
    public static PieceType fromNotation(char notation) {
        for (PieceType pieceType : PieceType.values()) {
            if (pieceType.getNotation() == notation) {
                return pieceType;
            }
        }
        return null;
    }

    /**
     * Method to get the string representation of the piece type.
     *
     * @return The string representation of the piece type.
     */
    @Override
    public String toString(){
        return String.valueOf(this.notation);
    }
}
