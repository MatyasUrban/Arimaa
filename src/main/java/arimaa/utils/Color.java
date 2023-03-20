package arimaa.utils;

/**
 * The Color enum represents the possible colors for a player's pieces.
 */
public enum Color {
    GOLD("Gold"),
    SILVER("Silver");
    private final String displayName;

    Color(String displayName){
        this.displayName = displayName;
    }

    @Override
    public String toString(){
        return this.displayName;
    }

}
