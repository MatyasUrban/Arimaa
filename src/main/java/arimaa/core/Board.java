package arimaa.core;

import arimaa.utils.Color;
import arimaa.utils.Direction;
import arimaa.utils.PieceType;
import arimaa.utils.Position;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The Board class represents the Arimaa game board.
 * It provides methods for placing pieces, making moves,
 * checking for valid moves, and determining win/lose conditions.
 */
public class Board {
    private final int BOARD_SIZE = 8;
    private final Piece[][] board;

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    /**
     * Constructs a new, empty Board object.
     */
    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    public void placePiece(Piece piece, Position position) {
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (getPieceAt(position) != null) {
            throw new IllegalArgumentException("There is already a piece at the position");
        }
        board[position.getRow()][position.getColumn()] = piece;
    }

    public Piece getPieceAt(Position position) {
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Invalid position");
        }
        return board[position.getRow()][position.getColumn()];
    }

    public void removePieceAt(Position position) {
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (getPieceAt(position) == null) {
            throw new IllegalArgumentException("No piece at the position");
        }
        board[position.getRow()][position.getColumn()] = null;
    }

    public boolean isValidPosition(Position position) {
        if (position == null) {
            return false;
        }
        int row = position.getRow();
        int col = position.getColumn();
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    /**
     * Moves a piece on the board, given a Move object.
     *
     * @param move The Move object representing the move to make.
     * @throws IllegalArgumentException If the move is invalid or illegal.
     */

    public void makeMove(Move move) {
        if (move == null) {
            throw new IllegalArgumentException("Move cannot be null");
        }

        if (move instanceof StepMove) {
            makeStepMove((StepMove) move);
        } else if (move instanceof PullMove) {
            makePullMove((PullMove) move);
        } else if (move instanceof PushMove) {
            makePushMove((PushMove) move);
        } else {
            throw new IllegalArgumentException("Unsupported move type: " + move.getClass().getSimpleName());
        }
    }

    private void makeStepMove(StepMove move) {
        Position oldPos = move.getFrom();
        Position newPos = move.getTo();

        if (!isValidPosition(oldPos) || !isValidPosition(newPos)) {
            throw new IllegalArgumentException("Invalid position in step move");
        }

        Piece piece = getPieceAt(oldPos);
        if (piece == null) {
            throw new IllegalArgumentException("No piece at the source position");
        }

        if (getPieceAt(newPos) != null) {
            throw new IllegalArgumentException("Target position is not empty");
        }

        removePieceAt(oldPos);
        placePiece(piece, newPos);
    }

    private void makePullMove(PullMove move) {
        StepMove pieceMove = new StepMove(move.getFrom(), move.getTo());
        StepMove pulledPieceMove = new StepMove(move.getPulledPieceFrom(), move.getPulledPieceTo());

        makeStepMove(pieceMove);
        makeStepMove(pulledPieceMove);
    }

    private void makePushMove(PushMove move) {
        StepMove pieceMove = new StepMove(move.getFrom(), move.getTo());
        StepMove pushedPieceMove = new StepMove(move.getPushedPieceFrom(), move.getPushedPieceTo());

        makeStepMove(pushedPieceMove);
        makeStepMove(pieceMove);
    }

    public void emptyBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
            }
        }
    }

    public Boolean hasPlayerWon(Player player){
        // check if player has a rabbit in the goal row
        for (int i = 0; i < BOARD_SIZE; i++){
            Piece piece;
            if (player.getColor() == Color.GOLD){
                piece = getPieceAt(new Position(0, i));
            } else {
                piece = getPieceAt(new Position(7, i));
            }
            if (piece.getOwner() == player && piece.getType() == PieceType.RABBIT){
                return true;
            }
        }
        return false;
    }

    public void initializeBoardFrom2DString(String[][] board2DString, Player player1, Player player2){
        for (int i = 0; i < getBOARD_SIZE(); i++){
            for (int j = 0; j < getBOARD_SIZE(); j++){
                Position position = new Position(i, j);
                String stringPiece = board2DString[i][j];
                if (Objects.equals(stringPiece, "")){
                    continue;
                };
                Player player = Character.isUpperCase(stringPiece.charAt(0)) ? player1 : player2;
                Piece piece = Piece.fromNotationPlayerSpecific(stringPiece, player);
                placePiece(piece, position);
            }
        }
    }

    public void switchPieces(Position position1, Position position2){
        Piece firstPiece = getPieceAt(position1);
        Piece secondPiece = getPieceAt(position2);
        placePiece(firstPiece, position2);
        placePiece(secondPiece, position1);
    }

    public ArrayList<Position> getPositionsOfPlayersPieces(Player player){
        ArrayList<Position> positionsArrayList = new ArrayList<>();
        for (int i = 0; i < getBOARD_SIZE(); i++){
            for (int j = 0; j < getBOARD_SIZE(); j++){
                Position position = new Position(i, j);
                Piece piece = getPieceAt(position);
                if (piece.getOwner() == player){
                    positionsArrayList.add(position);
                }
            }
        }
        return positionsArrayList;
    }

    public boolean isFriendlyPieceNearby(Position position){
        Piece piece = getPieceAt(position);
        Player owner = piece.getOwner();
        for (Position onePosition : position.getAdjacentPositions(Direction.getAllDirections())){
            Piece adjacentPiece = getPieceAt(onePosition);
            if (adjacentPiece.getOwner() == owner) return true;
        }
        return false;
    }

    public ArrayList<Position> getAdjacentStrongerEnemyPiecesPositions(Position position){
        ArrayList<Position> positionsArrayList = new ArrayList<>();
        Piece piece = getPieceAt(position);
        Player owner = piece.getOwner();
        for (Position onePosition : position.getAdjacentPositions(Direction.getAllDirections())){
            Piece adjacentPiece = getPieceAt(onePosition);
            if (adjacentPiece.getOwner() != owner && adjacentPiece.getType().isStrongerThan(piece.getType())){
                positionsArrayList.add(onePosition);
            }
        }
        return positionsArrayList;
    }

    public boolean isStrongerEnemyPieceNearby(Position position){
        return getAdjacentStrongerEnemyPiecesPositions(position).size()>0;
    }

    public boolean isPositionFrozen(Position position){
        return isStrongerEnemyPieceNearby(position) && !isFriendlyPieceNearby(position);
    }

    public boolean isFrozenAt(Position position){
        Piece piece = getPieceAt(position);
        Player owner = piece.getOwner();
        boolean isFrozen = false;
        for (Position onePosition : position.getAdjacentPositions(Direction.getAllDirections())){
            Piece adjacentPiece = getPieceAt(onePosition);
            if (adjacentPiece.getOwner() == owner){
                return false;
            } else if (adjacentPiece.getType().isStrongerThan(piece.getType())){
                isFrozen = true;
            }
        }
        return isFrozen;
    }

    public boolean isPositionEmpty(Position position){
        return getPieceAt(position) == null;
    }
    public ArrayList<StepMove> getValidStepMovesForPosition(Position position, boolean rabbitMustRespectDirection){
        ArrayList<StepMove> stepMoveArrayList = new ArrayList<>();
        Piece piece = getPieceAt(position);
        ArrayList<Direction> directionsArrayList;
        if (!rabbitMustRespectDirection){
            directionsArrayList = Direction.getAllDirections();
        } else {
            directionsArrayList = piece.getPossibleDirections();
        }
        if (isFrozenAt(position)) return new ArrayList<>();
        for (Position onePosition : position.getAdjacentPositions(directionsArrayList)){
            if(isPositionEmpty(onePosition)){
                stepMoveArrayList.add(new StepMove(position, onePosition));
            }
        }
        return stepMoveArrayList;
    }

    public boolean hasEmptyPositionsNearby(Position position){
        return getValidStepMovesForPosition(position, false).size() > 0;
    }

    public ArrayList<Position> getPositionsOfPlayersPiecesWhichCanMove(Player player){
        ArrayList<Position> positionArrayList = new ArrayList<>();
        for (Position position : getPositionsOfPlayersPieces(player)){
            if (hasEmptyPositionsNearby(position)) positionArrayList.add(position);
        }
        return positionArrayList;
    }

    public boolean canBePulled(Position position){
        ArrayList<Position> strongerEnemyNearbyPositions = getAdjacentStrongerEnemyPiecesPositions(position);
        for (Position onePosition : strongerEnemyNearbyPositions){
            if(hasEmptyPositionsNearby(onePosition)) return true;
        }
        return false;
    }

    public ArrayList<Position> getPositionsOfEnemyPiecesWhichCanBePulled(Player player, Player enemy){
        ArrayList<Position> enemyPiecesPositions = getPositionsOfPlayersPieces(enemy);
        for (Position onePosition : enemyPiecesPositions){
            if (canBePulled(onePosition)){
                enemyPiecesPositions.add(onePosition);
            }
        }
        return enemyPiecesPositions;
    }

    public ArrayList<PullMove> getValidPullMovesForPullerAndPulled(Position pullingPiecePosition, Position pulledPiecePosition){
        ArrayList<PullMove> pullMoveArrayList = new ArrayList<>();
        for (StepMove pullerStepMove : getValidStepMovesForPosition(pullingPiecePosition, false)){
            pullMoveArrayList.add(new PullMove(
                    pullerStepMove.getFrom(),
                    pullerStepMove.getTo(),
                    pulledPiecePosition,
                    pullingPiecePosition
                    ));
        }
        return pullMoveArrayList;
    }

    public boolean canBePushed(Position position){
        return hasEmptyPositionsNearby(position) && isStrongerEnemyPieceNearby(position);
    }

    public ArrayList<Position> getPositionsOfEnemyPiecesThatCanBePushed(Player player, Player enemy){
        ArrayList<Position> enemyPiecesPositions = getPositionsOfPlayersPieces(enemy);
        for (Position onePosition : enemyPiecesPositions){
            if (canBePushed(onePosition)){
                enemyPiecesPositions.add(onePosition);
            }
        }
        return enemyPiecesPositions;
    }

    public ArrayList<PushMove> getValidPushMovesForPusherAndPushed(Position pushingPiecePosition, Position pushedPiecePosition){
        ArrayList<PushMove> pushMoveArrayList = new ArrayList<>();
        for (StepMove pushedStepMove : getValidStepMovesForPosition(pushedPiecePosition, false)){
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