package arimaa.utils;

public enum PieceType{
    RABBIT("Rabbit", 1),
    CAT("Cat", 2),
    DOG("Dog", 3),
    HORSE("Horse", 4),
    CAMEL("Camel", 5),
    ELEPHANT("Elephant", 6);
    private final String displayName;
    private final int strength;

    PieceType(String displayName, int strength) {
        this.displayName = displayName;
        this.strength = strength;
    }

    public boolean isStrongerThan(PieceType otherType) {
        return this.ordinal() > otherType.ordinal();
    }

    @Override
    public String toString(){
        return this.displayName + " " + this.strength;
    }
}
