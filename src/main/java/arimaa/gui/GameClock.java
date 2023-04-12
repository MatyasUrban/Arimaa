package arimaa.gui;

import arimaa.core.Game;
import arimaa.gui.GameControlsPanel;

//public class GameClock extends Thread {
//    private final GameControlsPanel gameControlsPanel;
//    private final Game game;
//    private long player1Time;
//    private long player2Time;
//    private long startTime;
//    private long pauseTime;
//    private volatile boolean running;
//    private volatile boolean paused;
//
//    public GameClock(GameControlsPanel gameControlsPanel, Game game, String player1Time, String player2Time) {
//        this.gameControlsPanel = gameControlsPanel;
//        this.game = game;
//        this.running = true;
//        this.paused = true;
//        setElapsedTime(player1Time, player2Time);
//    }
//
//    public void setElapsedTime(String player1Time, String player2Time) {
//        this.player1Time = parseTime(player1Time);
//        this.player2Time = parseTime(player2Time);
//    }
//
//    private long parseTime(String time) {
//        String[] parts = time.split(":");
//        long hours = Long.parseLong(parts[0]);
//        long minutes = Long.parseLong(parts[1]);
//        long seconds = Long.parseLong(parts[2]);
//        return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
//    }
//
//    private String formatTime(long time) {
//        long seconds = (time / 1000) % 60;
//        long minutes = (time / (1000 * 60)) % 60;
//        long hours = (time / (1000 * 60 * 60)) % 24;
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
//    }
//
//    public void stopClock() {
//        running = false;
//    }
//
//    public synchronized void pauseClock() {
//        if (!paused) {
//            pauseTime = System.currentTimeMillis();
//            paused = true;
//        }
//    }
//
//    public synchronized void resumeClock() {
//        if (paused) {
//            long timeSpentPaused = System.currentTimeMillis() - pauseTime;
//            startTime += timeSpentPaused;
//            paused = false;
//        }
//    }
//
//    @Override
//    public void run() {
//        // Debug message to check if the run() method is called
//        System.out.println("GameClock run() called");
//        startTime = System.currentTimeMillis();
//        gameControlsPanel.updatePlayerBottomTime(formatTime(player1Time));
//        gameControlsPanel.updatePlayerTopTime(formatTime(player2Time));
//
//        while (running && !game.getGameEnded()) {
//            if (!paused) {
//                long currentTime = System.currentTimeMillis();
//                long elapsedTime = currentTime - startTime;
//                startTime = currentTime;
//                if (game.getGamePhase() % 2 == 1) {
//                    player1Time += elapsedTime;
//                    gameControlsPanel.updatePlayerBottomTime(formatTime(player1Time));
//                } else {
//                    player2Time += elapsedTime;
//                    gameControlsPanel.updatePlayerTopTime(formatTime(player2Time));
//                }
//            }
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}


    public class GameClock extends Thread {
        private final GameControlsPanel gameControlsPanel;
        private final Game game;
        private long player1Time;
        private long player2Time;
        private long startTime;
        private long pauseTime;
        private volatile boolean running;
        private volatile boolean paused;


        public GameClock(GameControlsPanel gameControlsPanel, Game game, String player1time, String player2time) {
            this.gameControlsPanel = gameControlsPanel;
            this.game = game;
            setElapsedTime(player1time, player2time);
            this.paused = false;
            this.running = true;
        }

        public void setElapsedTime(String player1Time, String player2Time) {
            this.player1Time = parseTime(player1Time);
            this.player2Time = parseTime(player2Time);
        }

        private long parseTime(String time) {
            String[] parts = time.split(":");
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);
            return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
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
