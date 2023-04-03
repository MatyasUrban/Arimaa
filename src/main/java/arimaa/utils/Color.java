package arimaa.utils;

/**
 * The Color enum represents the possible colors for a player's pieces.
 */
public enum Color {
    GOLD("Gold", 'G'),
    SILVER("Silver", 'S');
    private final String displayName;

    private final char bigChar;

    Color(String displayName, char bigChar){
        this.displayName = displayName;
        this.bigChar = bigChar;
    }

    public char getBigChar() {
        return bigChar;
    }

    @Override
    public String toString(){
        return this.displayName;
    }

}
