package arimaa.utils;

public enum PieceType{
    RABBIT('r', "rabbit"),
    CAT('c', "cat"),
    DOG('d', "dog"),
    HORSE('h', "horse"),
    CAMEL('m', "camel"),
    ELEPHANT('e', "elephant");
    private final char notation;

    private final String name;

    PieceType(char notation, String name) {
        this.notation = notation;
        this.name = name;
    }

    public boolean isStrongerThan(PieceType otherType) {
        return this.ordinal() > otherType.ordinal();
    }

    public static PieceType fromNotation(char notation) {
        for (PieceType pieceType : PieceType.values()) {
            if (pieceType.getNotation() == notation) {
                return pieceType;
            }
        }
        return null;
    }

    public char getNotation(){
        return notation;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return String.valueOf(this.notation);
    }
}
