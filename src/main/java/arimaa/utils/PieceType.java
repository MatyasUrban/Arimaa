package arimaa.utils;

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
