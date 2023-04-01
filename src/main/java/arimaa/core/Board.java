package arimaa.core;

import arimaa.utils.Direction;
import arimaa.utils.PieceType;
import arimaa.utils.Position;

/**
 * The Board class represents the Arimaa game board.
 * It provides methods for placing pieces, making moves,
 * checking for valid moves, and determining win/lose conditions.
 */
public class Board {
    public static final int BOARD_SIZE = 8;
    private final Piece[][] board;

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


}