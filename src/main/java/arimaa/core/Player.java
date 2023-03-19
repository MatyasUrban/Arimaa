package arimaa.core;
/**
 * The Player class represents a player in the Arimaa game.
 */
public class Player {
    private final String name;
    private final Color color;
    private final boolean isComputer;

    /**
     * Constructs a new Player object.
     * @param name The name of the player.
     * @param color The color of the player's pieces.
     * @param isComputer Boolean indicating whether the player is not human.
     */
    public Player(String name, Color color, boolean isComputer){
        this.name = name;
        this.color = color;
        this.isComputer = isComputer;
    }

    /**
     * Gets the name of the player.
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the color of the player's pieces.
     * @return The color of the player's pieces.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the information whether this player is a computer.
     * @return Truth value whether this player is a computer.
     */
    public boolean isComputer(){
        return isComputer;
    }

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
}
