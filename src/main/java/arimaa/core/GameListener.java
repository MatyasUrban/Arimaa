package arimaa.core;

/**
 * The GameListener interface to communicate Game -> GameControlsPanel
 */
public interface GameListener {

    /**
     * Let the destination know new MovesLeft value
     * @param movesLeft Number of moves left in the current turn.
     */
    void onMovesLeftChanged(int movesLeft);

    /**
     * Let the destination know that the game ended.
     * @param winner The winner player of the game.
     */
    void onGameEnded(Player winner);
}
