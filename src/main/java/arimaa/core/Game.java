package arimaa.core;

import arimaa.utils.Position;

public class Game {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Boolean gameEnded;
    private int gamePhase;

    private GameListener gameListener;
    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.gameEnded = false;
        this.gamePhase = 1;
    }
    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    public GameListener getGameListener(){
        return gameListener;
    }

    public Game(){
        this.board = new Board();
        this.player1 = new Player(1, false);
        this.player2 = new Player(2, false);
        this.currentPlayer = player1;
        this.gameEnded = false;
        this.gamePhase = 1;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Boolean getGameEnded() {
        return gameEnded;
    }

    public int getGamePhase() {
        return gamePhase;
    }

    public void startGame() {
        gameListener.onGamePhaseChanged(gamePhase);
        arrangePieces(player1);

        gameListener.onGamePhaseChanged(gamePhase);
        arrangePieces(player2);

        gameListener.onGamePhaseChanged(gamePhase);
        while (!board.hasPlayerWon(player1, player2) && !board.hasPlayerWon(player2, player1)) {
            // It's the current player's turn
            playTurn(currentPlayer);

            // Switch to the other player
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            gamePhase += 1;
            gameListener.onGamePhaseChanged(gamePhase);
        }


        if (board.hasPlayerWon(player1, player2)) {
            System.out.println("Player 1 wins!");
        } else {
            System.out.println("Player 2 wins!");
        }
        gameListener.onGameEnded(board.hasPlayerWon(player1, player2) ? player1 : player2);
    }

    private void arrangePieces(Player player) {
        // TODO: Implement the logic for a player to arrange their pieces on the board.
        // You may want to prompt the user for input, or use an automatic arrangement
        // based on a predefined configuration.
    }

    private void playTurn(Player player) {
        // TODO: Implement the logic for a player's turn.
        // You may want to prompt the user for input, or use an AI or algorithm to
        // generate a valid move. Be sure to validate the move with board.isValidMove(move)
        // before executing it with board.makeMove(move).
    }


}

