package arimaa.core;

import arimaa.utils.Color;
import arimaa.utils.Direction;
import arimaa.utils.PieceType;
import arimaa.utils.Position;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The Board class represents the Arimaa game board.
 * It provides methods for placing pieces, making moves, and so much more.
 * checking for valid moves, and determining win/lose conditions.
 */
public class Board {
    /*
    Class constant variable: width = height = 8
     */
    public static final int BOARD_SIZE = 8;
    /*
    Instance constant variable: 2D array of Piece objects
     */
    private final Piece[][] board;

    /**
     * Constructs a new, empty Board object.
     */
    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    /**
     * Method to place a piece onto the board.
     *
     * @param piece Piece object to be placed.
     * @param position Position object determining where piece should be placed on the board.
     */
    public void placePiece(Piece piece, Position position) {
        board[position.row()][position.column()] = piece;
    }

    /**
     * Method to get what's stored at specific position on the board.
     *
     * @param position Position object determining the lookup position.
     * @return Piece object if there is a piece is placed on the board at the position, else null value.
     */
    public Piece getPieceAt(Position position) {
        return board[position.row()][position.column()];
    }

    /**
     * Method to remove a piece from the board (by setting that position to null).
     *
     * @param position Position object determining coordinates of deletion.
     */
    public void removePieceAt(Position position) {
        board[position.row()][position.column()] = null;
    }

    /**
     * Method to determine whether given position is withing board range. (row 0-7, column 0-7)
     *
     * @param position Position object determining the checkup value.
     * @return Boolean value of the check.
     */
    public boolean isValidPosition(Position position) {
        if (position == null) {
            return false;
        }
        int row = position.row();
        int col = position.column();
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    /**
     * Method to move a piece on the board, given a Move object.
     *
     * @param move The Move object representing the move to make (can be given in child classes: StepMove, PushMove, PullMove)
     * @throws IllegalArgumentException If the move is invalid or illegal.
     */
    public void makeMove(Move move) {
        if (move instanceof StepMove) {
            makeStepMove((StepMove) move);
        } else if (move instanceof PullMove) {
            makePullMove((PullMove) move);
        } else if (move instanceof PushMove) {
            makePushMove((PushMove) move);
        }
    }

    /**
     * Method to facilitate a StepMove (movement of one piece to an adjacent position).
     *
     * @param move Move object with the old and new position.
     */
    private void makeStepMove(StepMove move) {
        Position position = move.getFrom();
        Position newPosition = move.getTo();
        Piece piece = getPieceAt(position);
        removePieceAt(position);
        placePiece(piece, newPosition);
    }

    /**
     * Method to facilitate a PullMove (StepMove of pulling piece, pulled piece moves into pulling piece's original position)
     *
     * @param move PullMove object with the old and new positions of both pieces.
     */
    private void makePullMove(PullMove move) {
        StepMove pullerPieceMove = new StepMove(move.getFrom(), move.getTo());
        StepMove pulledPieceMove = new StepMove(move.getPulledPieceFrom(), move.getPulledPieceTo());
        makeStepMove(pullerPieceMove);
        makeStepMove(pulledPieceMove);
    }

    /**
     * Method to facilitate a PushMove (Moving pushed piece and pushing piece moves into pushed piece's original position)
     *
     * @param move PushMove object with the old and new positions of both pieces)
     */
    private void makePushMove(PushMove move) {
        StepMove pushingPieceMove = new StepMove(move.getFrom(), move.getTo());
        StepMove pushedPieceMove = new StepMove(move.getPushedPieceFrom(), move.getPushedPieceTo());
        makeStepMove(pushedPieceMove);
        makeStepMove(pushingPieceMove);
    }

    /**
     * Method to empty the board (set all positions to null)
     */
    public void emptyBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
            }
        }
    }

    /**
     * Method to check whether a player has already won.
     * 1. Player's rabbit is in the goal row.
     * 2. Enemy lost all of their rabbits.
     *
     * @param player Player object with the player for which we'd like to run a check.
     * @return Boolean value whether this player has won.
     */
    public Boolean hasPlayerWon(Player player, Player enemy){
        // 1. Player's rabbit is in the goal row.
        Position[] goalRowPositions = player.getColor() == Color.GOLD ? Position.GOLD_GOAL_ROW : Position.SILVER_GOAL_ROW;
        for (Position position : goalRowPositions){
            Piece piece = getPieceAt(position);
            if (piece != null && piece.owner() == player && piece.type() == PieceType.RABBIT){
                return true;
            }
        }
        // 2. Enemy lost all of their rabbits.
        ArrayList<Position> enemyPiecesPositions = getPositionsOfPlayersPieces(enemy);
        for (Position position : enemyPiecesPositions){
            if (getPieceAt(position).type() == PieceType.RABBIT){
                return false;
            }
        }
        return true;
    }

    /**
     * Method to populate the board given a String[][] of Arimaa pieces denoted by notation.
     * Example. "" will insert null object into the board. "M" will insert a gold (player's 1 camel into the board).
     *
     * @param board2DString String[][] object with empty string or 1-letter strings from [EeMmHhDdCcRr].
     * @param player1 Player object representing the first player (uppercase, starts in the 7-8th row, heads to the 1st row).
     * @param player2 Player object representing the second player (lowercase, starts in the 1-2th row, heads to the 8th row).
     */
    public void populateBoardFrom2DString(String[][] board2DString, Player player1, Player player2){
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                Position position = new Position(i, j);
                String stringPiece = board2DString[i][j];
                if (Objects.equals(stringPiece, "")){
                    continue;
                };
                Player player = Character.isUpperCase(stringPiece.charAt(0)) ? player1 : player2;
                Piece piece = Piece.createPieceFromNotationPlayerWithSpecificPlayer(stringPiece, player);
                placePiece(piece, position);
            }
        }
    }

    /**
     * Method to switch pieces on the board given two positions.
     * Used before the game for the player's to arrange their starting positions.
     *
     * @param position1 Position object determining the location of the first piece on the board.
     * @param position2 Position object determining the location of the second piece on the board.
     */
    public void switchPieces(Position position1, Position position2){
        Piece firstPiece = getPieceAt(position1);
        Piece secondPiece = getPieceAt(position2);
        placePiece(firstPiece, position2);
        placePiece(secondPiece, position1);
    }

    /**
     * Method to get position's of all pieces belonging to one player being currently on the board.
     *
     * @param player Player object whose pieces are searched for.
     * @return ArrayList<Position> Positions of player's pieces.
     */
    public ArrayList<Position> getPositionsOfPlayersPieces(Player player){
        ArrayList<Position> positionsArrayList = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                Position position = new Position(i, j);
                Piece piece = getPieceAt(position);
                if (piece != null && piece.owner() == player){
                    positionsArrayList.add(position);
                }
            }
        }
        return positionsArrayList;
    }

    /**
     * Method to check whether there is a friendly piece in a valid adjacent position.
     * Used for unfreezing pieces and going over traps.
     *
     * @param position Position object for the examined position.
     * @return Boolean value of the check.
     */
    public boolean isFriendlyPieceNearby(Position position){
        Piece piece = getPieceAt(position);
        Player owner = piece.owner();
        // This iterates for all valid adjacent positions (meaning for a position in the corner only 2 positions are checked)
        for (Position onePosition : position.getAdjacentPositions(Direction.getFourDirections())){
            Piece adjacentPiece = getPieceAt(onePosition);
            if (adjacentPiece != null && adjacentPiece.owner() == owner) return true;
        }
        return false;
    }

    /**
     * Method to get all stronger enemy pieces for all valid adjacent positions to the one examined.
     * Used for freezing, pulling and pushing pieces.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public ArrayList<Position> getPositionsOfStrongerAdjacentEnemyPieces(Position position){
        ArrayList<Position> positionsArrayList = new ArrayList<>();
        Piece piece = getPieceAt(position);
        Player owner = piece.owner();
        // This iterates for all valid adjacent positions (meaning for a position in the corner only 2 positions are checked)
        for (Position onePosition : position.getAdjacentPositions(Direction.getFourDirections())){
            Piece adjacentPiece = getPieceAt(onePosition);
            if (adjacentPiece != null && adjacentPiece.owner() != owner && adjacentPiece.type().isStrongerThan(piece.type())){
                positionsArrayList.add(onePosition);
            }
        }
        return positionsArrayList;
    }

    /**
     * Method to check whether there is a stronger enemy piece in a valid adjacent position.
     * Used for unfreezing pieces.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public boolean isStrongerEnemyPieceNearby(Position position){
        return getPositionsOfStrongerAdjacentEnemyPieces(position).size()>0;
    }

    /**
     * Method to check whether there is a piece being on a given position is frozen.
     * Used for denying a piece the right to move.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public boolean isPositionFrozen(Position position){
        return isStrongerEnemyPieceNearby(position) && !isFriendlyPieceNearby(position);
    }

    /**
     * Method to check whether given position on the board is null (there is currently no piece at that position).
     * Used for checking whether a piece can move around.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public boolean isPositionEmpty(Position position){
        return getPieceAt(position) == null;
    }

    /**
     * Method to get all valid Step Moves for the current position, when the move is done by the player owning the piece.
     * Used for determining where the owner of the piece can step move this piece.
     *
     * @param position Position object of the examined position.
     * @return ArrayList<StepMove> Legal step moves from the current position.
     */
    public ArrayList<StepMove> getValidStepMovesByItselfForPosition(Position position){
        ArrayList<StepMove> stepMoveArrayList = new ArrayList<>();
        Piece piece = getPieceAt(position);
        // This gets all legal directions for the piece (based on player's goal direction and piece type)
        ArrayList<Direction> directionsArrayList = piece.getPossibleDirections();
        if (isPositionFrozen(position)) return new ArrayList<>();
        // This iterates for all valid adjacent positions (meaning for a position in the corner only 2 positions are checked)
        for (Position onePosition : position.getAdjacentPositions(directionsArrayList)){
            if(isPositionEmpty(onePosition)){
                stepMoveArrayList.add(new StepMove(position, onePosition));
            }
        }
        return stepMoveArrayList;
    }


    /**
     * Method to get all valid Step Moves for the current position, when the move is done by the enemy (pushing/pulling).
     * Used for determining, where the enemy can push/pull this piece.
     *
     * @param position Position object of the examined position.
     * @return ArrayList<StepMove> Legal step moves from the current position.
     */
    public ArrayList<StepMove> getValidStepMovesByPushingPullingForPosition(Position position){
        ArrayList<StepMove> stepMoveArrayList = new ArrayList<>();
        // This gets north, west, south, east directions
        ArrayList<Direction> directionsArrayList = Direction.getFourDirections();
        // This iterates for all valid adjacent positions (meaning for a position in the corner only 2 positions are checked)
        for (Position onePosition : position.getAdjacentPositions(directionsArrayList)){
            if(isPositionEmpty(onePosition)){
                stepMoveArrayList.add(new StepMove(position, onePosition));
            }
        }
        return stepMoveArrayList;

    }

    /**
     * Method to determine whether piece at this position has the capacity to move.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public boolean canStepMove(Position position){
        return getValidStepMovesByItselfForPosition(position).size() > 0;
    }

    /**
     * Method to determine whether piece at this position has the capacity to be moved.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public boolean canBeMoved(Position position){
        return getValidStepMovesByPushingPullingForPosition(position).size() > 0;
    }

    /**
     * Method to determine positions of pieces able to step move by itself.
     *
     * @param player Player object of the player owning the piece.
     * @return ArrayList<Position> Positions of pieces able to step move.
     */
    public ArrayList<Position> getPositionsOfPlayersPiecesWhichCanStepMove(Player player){
        ArrayList<Position> positionArrayList = new ArrayList<>();
        for (Position position : getPositionsOfPlayersPieces(player)){
            if (canStepMove(position)) positionArrayList.add(position);
        }
        return positionArrayList;
    }

    /**
     * Method to determine positions of pieces, which can be moved (as they have adjacent space around them).
     *
     * @param player Player object of the player owning the piece.
     * @return ArrayList<Position> Positions of pieces able to be moved.
     */
    public ArrayList<Position> getPositionsOfPlayersPiecesWhichCanBeMoved(Player player){
        ArrayList<Position> positionArrayList = new ArrayList<>();
        for (Position position : getPositionsOfPlayersPieces(player)){
            if (canBeMoved(position)) positionArrayList.add(position);
        }
        return positionArrayList;
    }

    /**
     * Method to check whether a piece at a given position can be pulled.
     * Meaning this piece is adjacent to a stronger enemy piece, and this enemy piece can step move.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public boolean canBePulled(Position position){
        ArrayList<Position> strongerEnemyNearbyPositions = getPositionsOfStrongerAdjacentEnemyPieces(position);
        // We need to iterate over stronger adjacent pieces to maker sure they can step move as well.
        for (Position onePosition : strongerEnemyNearbyPositions){
            if(canStepMove(onePosition)) return true;
        }
        return false;
    }

    /**
     * Method to determine candidates for pulled pieces.
     *
     * @param player Player object player considering the pull move.
     * @param enemy Player object enemy owning a piece that could be pulled.
     * @return ArrayList<Position> positions of all the enemy pieces which can be pulled.
     */
    public ArrayList<Position> getPositionsOfEnemyPiecesWhichCanBePulled(Player player, Player enemy){
        ArrayList<Position> enemyPiecesPositions = getPositionsOfPlayersPieces(enemy);
        ArrayList<Position> pullablePositions = new ArrayList<>();
        for (Position onePosition : enemyPiecesPositions){
            if (canBePulled(onePosition)){
                pullablePositions.add(onePosition);
            }
        }
        return pullablePositions;
    }

    /**
     * Method to determine candidates for pulling pieces, given a position of chosen pulled piece.
     *
     * @param pulledPiecePosition Position object of the examined position.
     * @return Boolean value of the check.
     */
    public ArrayList<Position> getPositionsOfPossiblePullingPieces(Position pulledPiecePosition){
        ArrayList<Position> pullingPieces = new ArrayList<>();
        for (Position pullingPiecePosition : getPositionsOfStrongerAdjacentEnemyPieces(pulledPiecePosition)){
            if (canStepMove(pullingPiecePosition)) pullingPieces.add(pullingPiecePosition);
        }
        return pullingPieces;
    }

    /**
     * Method to determine final possible pull moves given enemy piece position and stronger friendly piece position.
     *
     * @param pullingPiecePosition position from getPositionsOfPossiblePullingPieces(pulledPiecePosition)
     * @param pulledPiecePosition position from getPositionsOfEnemyPiecesWhichCanBePulled(enemy)
     * @return ArrayList<PullMove> list of possible pull moves
     */
    public ArrayList<PullMove> getValidPullMovesForPullerAndPulled(Position pullingPiecePosition, Position pulledPiecePosition){
        ArrayList<PullMove> pullMoveArrayList = new ArrayList<>();
        for (StepMove pullerStepMove : getValidStepMovesByItselfForPosition(pullingPiecePosition)){
            pullMoveArrayList.add(new PullMove(
                    pullerStepMove.getFrom(),
                    pullerStepMove.getTo(),
                    pulledPiecePosition,
                    pullingPiecePosition
                    ));
        }
        return pullMoveArrayList;
    }

    /**
     * Method to check whether a piece at a given position can be pushed.
     * Meaning this piece can be moved and is adjacent to a stronger enemy piece.
     *
     * @param position Position object of the examined position.
     * @return Boolean value of the check.
     */
    public boolean canBePushed(Position position){
        return canBeMoved(position) && isStrongerEnemyPieceNearby(position);
    }

    /**
     * Method to determine candidates for pushed pieces.
     *
     * @param player Player object player considering the push move.
     * @param enemy Player object enemy owning a piece that could be pushed.
     * @return ArrayList<Position> positions of all the enemy pieces which can be pushed.
     */
    public ArrayList<Position> getPositionsOfEnemyPiecesThatCanBePushed(Player player, Player enemy){
        ArrayList<Position> enemyPiecesPositions = getPositionsOfPlayersPieces(enemy);
        ArrayList<Position> pushablePositions = new ArrayList<>();
        for (Position onePosition : enemyPiecesPositions){
            if (canBePushed(onePosition)){
                pushablePositions.add(onePosition);
            }
        }
        return pushablePositions;
    }

    /**
     * Method to determine candidates for pushing pieces, given a position of chosen pushed piece.
     *
     * @param pushedPiecePosition Position object of the examined position.
     * @return Boolean value of the check.
     */
    public ArrayList<Position> getPositionsOfPossiblePushingPieces(Position pushedPiecePosition){
        return getPositionsOfStrongerAdjacentEnemyPieces(pushedPiecePosition);
    }

    /**
     * Method to determine final possible push moves given enemy piece position and stronger friendly piece position.
     *
     * @param pushingPiecePosition position from getPositionsOfPossiblePushingPieces(pushedPiecePosition)
     * @param pushedPiecePosition position from getPositionsOfEnemyPiecesWhichCanBePushed(enemy)
     * @return ArrayList<PullMove> list of possible push moves
     */
    public ArrayList<PushMove> getValidPushMovesForPusherAndPushed(Position pushingPiecePosition, Position pushedPiecePosition){
        ArrayList<PushMove> pushMoveArrayList = new ArrayList<>();
        for (StepMove pushedStepMove : getValidStepMovesByPushingPullingForPosition(pushedPiecePosition)){
            pushMoveArrayList.add(new PushMove(
                    pushingPiecePosition,
                    pushedPiecePosition,
                    pushedStepMove.getFrom(),
                    pushedStepMove.getTo()
            ));
        }
        return pushMoveArrayList;
    }

}