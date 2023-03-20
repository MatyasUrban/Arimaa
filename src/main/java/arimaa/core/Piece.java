package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.PieceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single piece on the Arimaa board with its type and owner.
 */
public class Piece {

    private final PieceType type;
    private final Player owner;

    /**
     * Constructs a new Piece with the specified type and owner.
     * @param type  The type of the piece (RABBIT, CAT, DOG, HORSE, CAMEL, ELEPHANT).
     * @param owner The owner of the piece (Player object).
     */
    public Piece(PieceType type, Player owner){
        this.type = type;
        this.owner = owner;
    }

    /**
     * Returns the type of the piece.
     * @return The type of the piece (RABBIT, CAT, DOG, HORSE, CAMEL, ELEPHANT).
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Returns the owner of the piece.
     * @return The owner of the piece (Player object).
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Creates the string representation of the current piece.
     * @return The string representation of the current piece
     */
    @Override
    public String toString() {
        return String.format("%s %s", owner.getColor(), type);
    }

    /**
     * Checks whether pieces are the same in terms of owner and type.
     * @param object The object being compared.
     * @return Truth value of object comparison.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Piece piece = (Piece) object;
        return type == piece.type && Objects.equals(owner, piece.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, owner);
    }

    /**
     * Returns the list of possible directions for the piece based on its owner and type.
     * @return A list of possible directions for the piece.
     */
    public List<Direction> getPossibleDirections() {
        List<Direction> directions = new ArrayList<>();

        if (type == PieceType.RABBIT) {
            // Rabbits cannot move backward
            directions.addAll(Arrays.asList(Direction.NORTH, Direction.EAST, Direction.WEST));
            if (owner.getGoalDirection() == Direction.SOUTH) {
                directions.remove(Direction.NORTH);
                directions.add(Direction.SOUTH);
            }
        } else {
            // All other pieces can move in any of the four directions
            directions.addAll(Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));
        }

        // Filter out directions moving away from the goal direction
        directions.removeIf(direction -> direction.opposite() == owner.getGoalDirection());

        return directions;
    }
}
