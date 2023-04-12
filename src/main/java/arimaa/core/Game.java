package arimaa.core;

import arimaa.utils.Position;

public class Game {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Player enemyPlayer;
    private Boolean gameEnded;

    private Player winner;
    private int gamePhase;
    private final StringBuilder stepsBuilder;

    private int movesLeftThisTurn;
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

    private GameListener gameListener;
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
    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    public GameListener getGameListener(){
        return gameListener;
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

    public Player getEnemyPlayer(){
        return enemyPlayer;
    }

    public Boolean getGameEnded() {
        return gameEnded;
    }

    public int getGamePhase() {
        return gamePhase;
    }

    public void startGame() {
    }


    public void incrementPhase(){
        if (gamePhase == 1){
            stepsBuilder.append(createHistoryBeginningOfMovesLine());
        }
        if (gamePhase <= 2){
            StringBuilder initialPositionsBuilder = new StringBuilder();
            for(Position position : getBoard().getPositionsOfPlayersPieces(currentPlayer)){
                initialPositionsBuilder.append(getBoard().getPieceAt(position).toString()).append(position).append(" ");
            }
            String initialPositions = initialPositionsBuilder.toString();
            System.out.print(initialPositions);
            stepsBuilder.append(initialPositions);
        }
        gamePhase++;
        switchTurn();
    }


    public void switchTurn(){
        Player newEnemyPlayer = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = newEnemyPlayer;
        movesLeftThisTurn = 4;
        String beginning = createHistoryBeginningOfMovesLine();
        System.out.print(beginning);
        stepsBuilder.append(beginning);
    }



    public int getMovesLeftThisTurn(){
        return movesLeftThisTurn;
    }

    public void decrementMovesLeftThisTurnBy(int number){
        movesLeftThisTurn -= number;
        if (gameListener != null) {
            gameListener.onMovesLeftChanged(movesLeftThisTurn);
        }
    }

    public void checkWinning(){
        boolean player1won = board.hasPlayerWon(player1, player2);
        boolean player2won = board.hasPlayerWon(player2, player1);
        if (player1won || player2won){
            winner = player1won ? player1 : player2;
            gameEnded = true;
            gameListener.onGameEnded(winner);
        }
    }

    public String createHistoryBeginningOfMovesLine(){
        return "\n" + getStep() + String.valueOf(currentPlayer.getColor().getSmallChar()) + " ";
    }

    private int getStep(){
        if (gamePhase % 2 == 0) {
            return gamePhase / 2;
        } else {
            return (gamePhase / 2) + 1;
        }
    }

    public void appendStepsBuilder(String string){
        stepsBuilder.append(string);
    }

    public void endGame(){
        winner = enemyPlayer;
        gameEnded = true;
        gameListener.onGameEnded(winner);
    }

    public Player getWinner(){
        return winner;
    }

    public String getStepsString(){
        return stepsBuilder.toString();
    }

    public void setMovesLeftThisTurn(int movesLeftThisTurn) {
        this.movesLeftThisTurn = movesLeftThisTurn;
    }

    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    public void setEnemyPlayer(Player player){
        enemyPlayer = player;
    }

    public void setGamePhase(int phase){
        gamePhase = phase;
    }






}

