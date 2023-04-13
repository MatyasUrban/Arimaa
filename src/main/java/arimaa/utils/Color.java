package arimaa.utils;

/**
 * The Color enum represents the possible colors for a player's pieces.
 */
public enum Color {
    GOLD("Gold"),
    SILVER("Silver");
    private final String displayName;

    /**
     * Constructs new Color object given displayName and bigChar.
     * @param displayName Name of color per Arimaa rules.
     */
    Color(String displayName){
        this.displayName = displayName;
    }

    /**
     * Gets the initial of the display name.
     * Used for retrieving images.
     *
     * @return The initial fo the display name.
     */
    public char getBigChar() {
        return displayName.charAt(0);
    }

    public char getSmallChar(){
        return displayName.toLowerCase().charAt(0);
    }

    /**
     * Method to get the string representation of the color.
     * @return The string representation of the color.
     */
    @Override
    public String toString(){
        return this.displayName;
    }
}
