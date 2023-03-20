package arimaa.core;

import arimaa.utils.Color;
import arimaa.utils.Direction;

/**
 * The Player class represents a player in the Arimaa game.
 */
public class Player {
    private final String name;
    private final Color color;
    private final boolean isComputer;
    private final Direction goalDirection;

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
        // Set the goal direction based on the player's color
        if (color == Color.GOLD) {
            this.goalDirection = Direction.NORTH;
        } else {
            this.goalDirection = Direction.SOUTH;
        }
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
     * Gets the player's goal direction.
     * @return The goal direction.
     */
    public Direction getGoalDirection() {
        return goalDirection;
    }

    /**
     * Gets the information whether this player is a computer.
     * @return Truth value whether this player is a computer.
     */
    public boolean isComputer(){
        return isComputer;
    }
}
