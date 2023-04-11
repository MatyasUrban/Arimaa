package arimaa.utils;

import arimaa.core.Game;
import arimaa.gui.GameControlsPanel;

public class GameClock extends Thread {
    private final GameControlsPanel gameControlsPanel;
    private final Game game;
    private long player1Time;
    private long player2Time;
    private long startTime;
    private long pauseTime;
    private volatile boolean running;
    private volatile boolean paused;



    public GameClock(GameControlsPanel gameControlsPanel, Game game) {
        this.gameControlsPanel = gameControlsPanel;
        this.game = game;
        this.player1Time = 0;
        this.player2Time = 0;
        this.running = true;
    }

    private String formatTime(long time) {
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = (time / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    public void stopClock() {
        running = false;
    }

    public synchronized void pauseClock() {
        if (!paused) {
            pauseTime = System.currentTimeMillis();
            paused = true;
        }
    }

    public synchronized void resumeClock() {
        if (paused) {
            long timeSpentPaused = System.currentTimeMillis() - pauseTime;
            startTime += timeSpentPaused;
            paused = false;
        }
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        while (running && !game.getGameEnded()) {
            if (!paused) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                startTime = currentTime;
                if (game.getGamePhase() % 2 == 1) {
                    player1Time += elapsedTime;
                    gameControlsPanel.updatePlayerBottomTime(formatTime(player1Time));
                } else {
                    player2Time += elapsedTime;
                    gameControlsPanel.updatePlayerTopTime(formatTime(player2Time));
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
