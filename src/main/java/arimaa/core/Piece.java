package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.PieceType;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The Piece record represents a single piece on the Arimaa board characterized by its type and owner.
 *
 * @param type  PieceType object determining the type (RABBIT, CAT, DOG, HORSE, CAMEL, ELEPHANT)
 * @param owner Player object determining the player who owns the piece.
 */
public record Piece(PieceType type, Player owner) {

    /**
     * Method to get the type of the piece.
     *
     * @return The type of the piece (RABBIT, CAT, DOG, HORSE, CAMEL, ELEPHANT).
     */
    @Override
    public PieceType type() {
        return type;
    }

    /**
     * Method to get the owner of the piece.
     *
     * @return The owner of the piece (Player object).
     */
    @Override
    public Player owner() {
        return owner;
    }

    /**
     * Method to get the list of possible directions for the piece based on its owner and type.
     * Gold rabbit - north, west, east; Silver rabbit - south, west, east; other pieces in all directions
     *
     * @return ArrayList<Direction> of possible directions for the piece.
     */
    public ArrayList<Direction> getPossibleDirections() {
        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(Direction.NORTH);
        directions.add(Direction.WEST);
        directions.add(Direction.SOUTH);
        directions.add(Direction.EAST);
        if (type == PieceType.RABBIT) {
            if (owner.getGoalDirection() == Direction.NORTH) {
                directions.remove(Direction.SOUTH);
            } else {
                directions.remove(Direction.NORTH);
            }
        }
        return directions;
    }

    /**
     * Static method to create a Piece object from String where owner does not matter.
     * Used for viewing Game history, read from text file. For that purpose we do not need advanced game methods requiring a player.
     *
     * @param pieceTypeString String representing the piece type [EMHCDRemhcdr].
     * @return Piece object of correct type and general player.
     */
    public static Piece createPieceFromNotationWithGeneralPlayer(String pieceTypeString) {
        // empty string returns a null object
        if (Objects.equals(pieceTypeString, "")) {
            return null;
        }
        PieceType pieceType = PieceType.fromNotation(pieceTypeString.toLowerCase().charAt(0));
        // the player number is important as it determines whether player is 1 (gold, heads north), 2 (silver, heads south)
        int playerNumber = Character.isUpperCase(pieceTypeString.charAt(0)) ? 1 : 2;
        Player player = new Player(playerNumber, false);
        return new Piece(pieceType, player);
    }

    /**
     * Static method to create a Piece object from String with a given Player owner.
     * Used for playing the game, as commonly we need to get all pieces assigned to a specific player.
     *
     * @param pieceTypeString String representing the piece type [EMHCDRemhcdr].
     * @param player          Player object determining the player.
     * @return Piece object of correct type and general player.
     */
    public static Piece createPieceFromNotationPlayerWithSpecificPlayer(String pieceTypeString, Player player) {
        if (Objects.equals(pieceTypeString, "")) {
            return null;
        }
        PieceType pieceType = PieceType.fromNotation(pieceTypeString.toLowerCase().charAt(0));
        return new Piece(pieceType, player);
    }
}
