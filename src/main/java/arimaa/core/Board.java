package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.PiecePositionPair;
import arimaa.utils.PieceType;
import arimaa.utils.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Board class represents the game board for an Arimaa game.
 */
public class Board {

    public static final int ROWS = 8;
    public static final int COLUMNS = 8;

    private final Piece[][] board;

    /**
     * Constructs a new empty Board object.
     */
    public Board() {
        board = new Piece[ROWS][COLUMNS];
    }

    /**
     * Gets the piece at the specified position.
     * @param position The position to get the piece from.
     * @return The piece at the specified position, or null if there's no piece at that position.
     */
    public Piece getPieceAt(Position position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the piece at the specified position.
     * @param position The position to set the piece at.
     * @param piece    The piece to set at the specified position.
     */
    public void setPieceAt(Position position, Piece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Checks if the specified position is within the board boundaries.
     * @param position The position to check.
     * @return True if the position is within the board boundaries, false otherwise.
     */
    public boolean isInBounds(Position position) {
        int row = position.getRow();
        int column = position.getColumn();

        return row >= 0 && row < ROWS && column >= 0 && column < COLUMNS;
    }

    /**
     * Creates a deep copy of the current board object.
     * @return A deep copy of the current board object.
     */
    public Board deepCopy() {
        Board newBoard = new Board();
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                Position position = new Position(row, column);
                newBoard.setPieceAt(position, getPieceAt(position));
            }
        }
        return newBoard;
    }

    /**
     * Initializes the board with the starting positions of the pieces for each player.
     * @param goldPieces   A list of gold player pieces and their starting positions.
     * @param silverPieces A list of silver player pieces and their starting positions.
     */
    public void initializeBoard(List<PiecePositionPair<Piece, Position>> goldPieces, List<PiecePositionPair<Piece, Position>> silverPieces) {
        for (PiecePositionPair<Piece, Position> pair : goldPieces) {
            setPieceAt(pair.getPosition(), pair.getPiece());
        }
        for (PiecePositionPair<Piece, Position> pair : silverPieces) {
            setPieceAt(pair.getPosition(), pair.getPiece());
        }
    }

    /**
     * Determines the available moves for a piece at the specified position.
     *
     * @param position The position of the piece.
     * @return A list of available moves for the piece.
     */
    public List<Move> getAvailableMoves(Position position) {
        List<Move> availableMoves = new ArrayList<>();

        Piece piece = getPieceAt(position);
        if (piece == null) {
            return availableMoves;
        }

        List<Direction> possibleDirections = piece.getPossibleDirections();

        for (Direction direction : possibleDirections) {
            Position newPosition = position.move(direction);
            if (isInBounds(newPosition) && getPieceAt(newPosition) == null) {
                availableMoves.add(new StepMove(position, newPosition));
            }
        }

        // Add push and pull moves here (if necessary)

        return availableMoves;
    }

    /**
     * Checks if the specified player has reached their goal.
     *
     * @param player The player to check.
     * @return True if the player has reached their goal, false otherwise.
     */
    public boolean hasReachedGoal(Player player) {
        int goalRow = player.getGoalDirection() == Direction.NORTH ? 0 : ROWS - 1;

        for (int column = 0; column < COLUMNS; column++) {
            Piece piece = getPieceAt(new Position(goalRow, column));
            if (piece != null && piece.getOwner().equals(player) && piece.getType() == PieceType.RABBIT) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the given position is a trap.
     *
     * @param position The position to be checked.
     * @return True if the position is a trap, false otherwise.
     */
    public boolean isTrap(Position position) {
        int row = position.getRow();
        int column = position.getColumn();

        return (row == 2 && column == 2) || (row == 2 && column == 5)
                || (row == 5 && column == 2) || (row == 5 && column == 5);
    }

    /**
     * Checks if a piece can push or pull another piece.
     * @param pushingPiece The piece attempting to push or pull.
     * @param pushedPiece The piece being pushed or pulled.
     * @return True if the pushingPiece can push or pull the pushedPiece, false otherwise.
     */
    public boolean canPushOrPull(Piece pushingPiece, Piece pushedPiece) {
        if (pushingPiece == null || pushedPiece == null) {
            return false;
        }

        // Check if the pushing piece is stronger than the pushed piece
        return pushingPiece.getType().isStrongerThan(pushedPiece.getType());
    }

    /**
     * Determines if a piece can move to the specified position.
     * @param piece The piece to be moved.
     * @param currentPosition The current position of the piece.
     * @param newPosition The position to move the piece to.
     * @return True if the piece can move to the newPosition, false otherwise.
     */
    public boolean canMoveTo(Piece piece, Position currentPosition, Position newPosition) {
        // Check if the new position is within the board boundaries
        if (newPosition.getRow() < 0 || newPosition.getRow() > 7 || newPosition.getColumn() < 0 || newPosition.getColumn() > 7) {
            return false;
        }

        // Check if the new position is not occupied by a friendly piece
        Piece destinationPiece = getPieceAt(newPosition);
        if (destinationPiece != null && destinationPiece.getOwner().getColor() == piece.getOwner().getColor()) {
            return false;
        }

        // Check if the piece can step onto the new position or push/pull opponent pieces
        if (destinationPiece != null && piece.getType().ordinal() > destinationPiece.getType().ordinal()) {
            return canPushOrPull(piece, destinationPiece);
        }

        // Check if the new position is a trap
        if (isTrap(newPosition)) {
            // If the new position is a trap, the piece can only move to it if there's a friendly piece adjacent to it
            for (Direction direction : Direction.values()) {
                Position adjacentPosition = currentPosition.getAdjacentPosition(direction);
                Piece adjacentPiece = getPieceAt(adjacentPosition);

                if (adjacentPiece != null && adjacentPiece.getOwner().getColor() == piece.getOwner().getColor()) {
                    return true;
                }
            }

            // If there is no friendly piece adjacent to the trap, the piece cannot move to it
            return false;
        }

        return true;
    }

    /**
     * Gets the available moves for the specified piece at the given position considering up to 4 steps.
     *
     * @param piece The piece for which to get the available moves.
     * @param currentPosition The current position of the piece.
     * @param steps The remaining number of steps.
     * @return A list of valid moves for the piece.
     */
    public List<List<Move>> getAvailableMoves(Piece piece, Position currentPosition, int steps) {
        List<List<Move>> availableMoves = new ArrayList<>();

        if (steps <= 0) {
            return availableMoves;
        }

        // Check each direction for valid moves
        for (Direction direction : piece.getPossibleDirections()) {
            Position newPosition = currentPosition.getAdjacentPosition(direction);

            // Check if the piece can move to the new position
            if (canMoveTo(piece, currentPosition, newPosition)) {
                List<Move> moves = new ArrayList<>();
                moves.add(new StepMove(currentPosition, newPosition));
                availableMoves.add(moves);

                // Continue to explore further steps
                List<List<Move>> furtherMoves = getAvailableMoves(piece, newPosition, steps - 1);
                for (List<Move> moveList : furtherMoves) {
                    List<Move> combinedMoves = new ArrayList<>(moves);
                    combinedMoves.addAll(moveList);
                    availableMoves.add(combinedMoves);
                }
            } else {
                Piece otherPiece = getPieceAt(newPosition);
                if (otherPiece != null && canPushOrPull(piece, otherPiece)) {
                    // Check for push and pull moves
                    for (Direction otherDirection : Direction.values()) {
                        Position otherNewPosition = newPosition.getAdjacentPosition(otherDirection);
                        if (canMoveTo(otherPiece, newPosition, otherNewPosition)) {
                            availableMoves.add(Arrays.asList(new PushMove(currentPosition, newPosition, newPosition, otherNewPosition)));
                        }
                        Position pullPosition = currentPosition.getAdjacentPosition(otherDirection.opposite());
                        if (canMoveTo(piece, currentPosition, pullPosition)) {
                            availableMoves.add(Arrays.asList(new PullMove(currentPosition, newPosition, newPosition, pullPosition)));
                        }
                    }
                }
            }
        }

        return availableMoves;
    }

}
