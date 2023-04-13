package arimaa.gui;

import arimaa.core.Game;

/**
 * The GameClock class facilitates tracking how much time each player spent in total and shows live information.
 */
public class GameClock extends Thread {

    /**
     * The panel on which the live times will be displayed and updated.
     */
    private final GameControlsPanel gameControlsPanel;
    /**
     * The game associated with the times.
     */
    private final Game game;
    /**
     * Time of the first player.
     */
    private long player1Time;
    /**
     * Time of the second player.
     */
    private long player2Time;
    /**
     * Start time of the clock.
     */
    private long startTime;
    /**
     * Time spent in pause.
     */
    private long pauseTime;
    /**
     * Flag indicating whether the clock is running.
     */
    private volatile boolean running;
    /**
     * Flag indicating whether the clock is paused.
     */
    private volatile boolean paused;

    /**
     * Constructs a new clock.
     * @param gameControlsPanel The game controls panel.
     * @param game The game associated with the clock.
     * @param player1time Decision time of the first player.
     * @param player2time Decision time of the second player.
     */
    public GameClock(GameControlsPanel gameControlsPanel, Game game, String player1time, String player2time) {
        this.gameControlsPanel = gameControlsPanel;
        this.game = game;
        setElapsedTime(player1time, player2time);
        this.paused = false;
        this.running = true;
    }

    /**
     * Sets the initial time of both players from string.
     *
     * @param player1Time String time representation of the first player.
     * @param player2Time String time representation of the second player.
     */
    public void setElapsedTime(String player1Time, String player2Time) {
        this.player1Time = parseTime(player1Time);
        this.player2Time = parseTime(player2Time);
    }

    /**
     * Converts string to long of time.
     *
     * @param time String in the format of hh:mm:ss
     * @return Long representing the time.
     */
    private long parseTime(String time) {
        String[] parts = time.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
    }

    /**
     * Converts long to string of time
     * @param time Long representing the time.
     * @return String in the format of hh:mm:ss
     */
    private String formatTime(long time) {
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = (time / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Method to stop the clock by setting the running flag.
     */
    public void stopClock() {
        running = false;
    }

    /**
     * Method to pause the clock by recording the time of the pause and setting the paused flag.
     */
    public synchronized void pauseClock() {
        if (!paused) {
            pauseTime = System.currentTimeMillis();
            paused = true;
        }
    }

    /**
     * Method to resume the clock (after a paused period) by adjusting the time and paused flag.
     */
    public synchronized void resumeClock() {
        if (paused) {
            long timeSpentPaused = System.currentTimeMillis() - pauseTime;
            startTime += timeSpentPaused;
            paused = false;
        }
    }

    /**
     * Running logic of the game clock.
     */
    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        while (running && !game.getGameEnded()) {
            if (!paused) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                startTime = currentTime;
                // Updates the correct player time based on whose turn it is.
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
