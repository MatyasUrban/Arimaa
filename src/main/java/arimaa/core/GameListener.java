package arimaa.core;

public interface GameListener {
    void onGamePhaseChanged(int gamePhase);
    void onGameEnded(Player winner);
}
