package arimaa.core;

import arimaa.utils.Color;
import arimaa.utils.Direction;

/**
 * The Player class represents a player in the Arimaa game.
 */
public class Player {
    private final int playerNumber;
    private final Color color;
    private final boolean isComputer;
    private final Direction goalDirection;
    private final String playerName;

    /**
     * Constructs a new Player object.
     * @param playerNumber The player's number (1-Gold, towards north; 2-Silver, towards south)
     * @param isComputer Boolean indicating whether the player is not human.
     */
    public Player(int playerNumber, boolean isComputer){
        this.playerNumber = playerNumber;
        if (playerNumber == 1) {
            this.color = Color.GOLD;
            this.goalDirection = Direction.NORTH;
        } else {
            this.color = Color.SILVER;
            this.goalDirection = Direction.SOUTH;
        }
        this.isComputer = isComputer;
        this.playerName = "";
    }

    public Player(int playerNumber, boolean isComputer, String playerName){
        this.playerNumber = playerNumber;
        if (playerNumber == 1) {
            this.color = Color.GOLD;
            this.goalDirection = Direction.NORTH;
        } else {
            this.color = Color.SILVER;
            this.goalDirection = Direction.SOUTH;
        }
        this.isComputer = isComputer;
        this.playerName = playerName;
    }

    /**
     * Gets the number of the player.
     * @return The number of the player.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getPlayerName(){
        return playerName;
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
