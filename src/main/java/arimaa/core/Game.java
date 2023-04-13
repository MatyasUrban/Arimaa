package arimaa.core;

import arimaa.utils.Position;

/**
 * The Game class represents one Arimaa gameplay (with board and two players)
 */
public class Game {
    /**
     * Class constant: initial pieces position for quick board setup
     */
    public static final String[][] DEFAULT_BOARD = {
            {"r", "r", "r", "r", "r", "r", "r", "r"},
            {"e", "m", "h", "h", "d", "d", "c", "c"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"E", "M", "H", "H", "D", "D", "C", "C"},
            {"R", "R", "R", "R", "R", "R", "R", "R"}
    };

    /**
     * Instance variable: Game board
     */
    private final Board board;
    /**
     * Instance variable: String builder holding logs of individual turns and moves
     */
    private final StringBuilder stepsBuilder;
    /**
     * Instance variable: Golden player
     */
    private final Player player1;
    /**
     * Instance variable: Silver player
     */
    private final Player player2;
    /**
     * Instance variable: Player whose turn currently it is
     */
    private Player currentPlayer;
    /**
     * Instance variable: Player whose turn currently it is NOT
     */
    private Player enemyPlayer;
    /**
     * Instance variable: winner player
     */
    private Player winner;
    /**
     * Instance variable: whether the end condition has been met
     */
    private Boolean gameEnded;
    /**
     * Instance variable: holding the game phase (turn)
     */
    private int gamePhase;
    /**
     * Instance variable: holding number of moves out of 4 the current player has available during current turn
     */
    private int movesLeftThisTurn;
    /**
     * Instance variable: game listener interface to fire up specific actions in the Game Controls Panel
     */
    private GameListener gameListener;

    /**
     * Constructs a new Game
     *
     * @param player1 Golden player
     * @param player2 Silver player
     */
    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.enemyPlayer = player2;
        this.gameEnded = false;
        this.gamePhase = 1;
        this.movesLeftThisTurn = 4;
        this.stepsBuilder = new StringBuilder();
        this.winner = null;
    }

    /**
     * Sets the game listener.
     *
     * @param gameListener The game listener.
     */
    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    /**
     * Gets the game board.
     *
     * @return The game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Method to append the logging steps builder by another string.
     *
     * @param string String value to be appended.
     */
    public void appendStepsBuilder(String string){
        stepsBuilder.append(string);
    }

    /**
     * Gets the logging steps builder.
     *
     * @return The logging steps builder.
     */
    public StringBuilder getStepsBuilder(){
        return stepsBuilder;
    }

    /**
     * Gets the first player.
     *
     * @return The first player.
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Gets the second player.
     *
     * @return The second player
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player.
     *
     * @param currentPlayer The current player.
     */
    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets the enemy player.
     *
     * @return The enemy player.
     */
    public Player getEnemyPlayer(){
        return enemyPlayer;
    }

    /**
     * Sets the enemy player.
     *
     * @param enemyPlayer The enemy player.
     */
    public void setEnemyPlayer(Player enemyPlayer){
        this.enemyPlayer = enemyPlayer;
    }

    /**
     * Gets the game ended boolean.
     *
     * @return The game ended boolean.
     */
    public Boolean getGameEnded() {
        return gameEnded;
    }

    /**
     * Sets the game ended boolean.
     *
     * @param gameEnded The game ended boolean.
     */
    public void setGameEnded(boolean gameEnded){
        this.gameEnded = gameEnded;
    }

    /**
     * Gets the game phase number.
     *
     * @return The game number.
     */
    public int getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets the game phase number.
     *
     * @param phase The game phase number.
     */
    public void setGamePhase(int phase){
        gamePhase = phase;
    }

    /**
     * Gets the number of moves left this turn.
     *
     * @return The number of moves left this turn.
     */
    public int getMovesLeftThisTurn(){
        return movesLeftThisTurn;
    }

    /**
     * Sets the number of moves left this turn.
     *
     * @param movesLeftThisTurn The number of moves left this turn.
     */
    public void setMovesLeftThisTurn(int movesLeftThisTurn) {
        this.movesLeftThisTurn = movesLeftThisTurn;
    }

    /**
     * Gets the winner player.
     *
     * @return The winner player.
     */
    public Player getWinner(){
        return winner;
    }

    /**
     * Sets the winner player.
     *
     * @param winner The winner player.
     */
    public void setWinner(Player winner){
        this.winner = winner;
    }


    /**
     * Method to increment phase (1->2: log initial gold positions, 2->3: log initial silver positions)
     */
    public void incrementPhase(){
        if (gamePhase == 1){
            stepsBuilder.append(phaseToTurnBeginning());
        }
        if (gamePhase <= 2){
            StringBuilder initialPositionsBuilder = new StringBuilder();
            for(Position position : getBoard().getPositionsOfPlayersPieces(currentPlayer)){
                initialPositionsBuilder.append(getBoard().getPieceAt(position).toString()).append(position).append(" ");
            }
            String initialPositions = initialPositionsBuilder.toString();
            stepsBuilder.append(initialPositions);
        }
        gamePhase++;
        appendStepsBuilder(phaseToTurnBeginning());
        switchTurn();
    }


    /**
     * Method to switch turn from current player to enemy player and thus restores moves left, also log the beginning.
     */
    public void switchTurn(){
        Player newEnemyPlayer = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = newEnemyPlayer;
        movesLeftThisTurn = 4;
    }


    /**
     * Method to decrement the number of moves left this turn and fire up game listener to notify the controls panel that the number of moves changed.
     *
     * @param number Number determining by how much the moves should decrement (push and pull decrement by 2)
     */
    public void decrementMovesLeftThisTurnBy(int number){
        movesLeftThisTurn -= number;
        gameListener.onMovesLeftChanged(movesLeftThisTurn);
    }

    /**
     * Method to ask the board to examine winning conditions and possible end the game.
     */
    public void checkWinning(){
        boolean player1won = board.hasPlayerWon(player1, player2);
        boolean player2won = board.hasPlayerWon(player2, player1);
        if (player1won || player2won){
            winner = player1won ? player1 : player2;
            gameEnded = true;
            gameListener.onGameEnded(winner);
        }
    }

    /**
     * Converts phase number to line beginning for logging purposes.
     * PHASE    TURN
     * 1        1g
     * 2        1s
     * 3        2g
     * 4        2s
     * ...      ...
     * @return Beginning of logging line
     */
    private String phaseToTurnBeginning(){
        int turnNumber;
        if (gamePhase % 2 == 0) {
            turnNumber = gamePhase / 2;
            return "\n" + turnNumber + "s ";
        } else {
            turnNumber = (gamePhase + 1) / 2;
            return "\n" + turnNumber + "g ";
        }
    }

    public void endGameByResign(){
        setWinner(enemyPlayer);
        gameEnded = true;
        gameListener.onGameEnded(winner);
    }


    

    








}

