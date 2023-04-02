package arimaa.utils;

public enum PieceType{
    RABBIT('r'),
    CAT('c'),
    DOG('d'),
    HORSE('h'),
    CAMEL('m'),
    ELEPHANT('e');
    private final char notation;

    PieceType(char notation) {
        this.notation = notation;
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

    @Override
    public String toString(){
        return String.valueOf(this.notation);
    }
}
