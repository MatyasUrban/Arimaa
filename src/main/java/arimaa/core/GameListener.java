package arimaa.core;

public interface GameListener {

    void onMovesLeftChanged(int movesLeft);
    void onGameEnded(Player winner);
}
