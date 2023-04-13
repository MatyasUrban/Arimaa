package arimaa.core;

import arimaa.utils.Color;
import arimaa.utils.Direction;

/**
 * The Player class represents a player in the Arimaa game.
 */
public class Player {
    /**
     * Instance variable player number determines color and direction as game works always with 2 player objects
     */
    private final int playerNumber;
    /**
     * Instance variable color from enum GOLD/SILVER
     */
    private final Color color;
    /**
     * Instance variable isComputer so that we know if there is need for autonomous decision making
     */
    private final boolean isComputer;
    /**
     * Instance variable goalDirection from enum NORTH/SOUTH
     */
    private final Direction goalDirection;
    /**
     * Instance variable player name for holding inputted name
     */
    private final String playerName;

    /**
     * Constructs a new Player object given playerNumber and isComputer.
     * Used for reading history from txt.
     *
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

    /**
     * Constructs a new Player object given playerNumber, isComputer and playerName.
     * Used for playing game (single-player/multiplayer).
     *
     * @param playerNumber The player's number (1-Gold, towards north; 2-Silver, towards south)
     * @param isComputer Boolean indicating whether the player is not human.
     * @param playerName String inputted name of the player.
     */
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
     *
     * @return The number of the player.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getPlayerName(){
        return playerName;
    }

    /**
     * Gets the color of the player's pieces.
     *
     * @return The color of the player's pieces.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the player's goal direction.
     *
     * @return The goal direction.
     */
    public Direction getGoalDirection() {
        return goalDirection;
    }

    /**
     * Method to check whether this player is a computer.
     *
     * @return Boolean value of the check.
     */
    public boolean isComputer(){
        return isComputer;
    }

}
