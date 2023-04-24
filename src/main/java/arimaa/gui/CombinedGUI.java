package arimaa.gui;

import arimaa.core.Game;
import arimaa.core.Piece;
import arimaa.core.Player;
import arimaa.core.StepMove;
import arimaa.utils.Direction;
import arimaa.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CombinedGUI class is the main frame class managing the entire user-program interaction, displaying and organizing several panels.
 */
public class CombinedGUI {

    /**
     * Key GUI frame of the program.
     */
    private static JFrame frame;
    /**
     * Currently used 8x8 visual board with labels.
     */
    private static LabeledBoardPanel labeledBoardPanel;
    /**
     * Currently used right panel (differs by what user is doing: initial-WelcomePanel, play-GameControlsPanel, view steps-HistoryPanel).
     */
    private static JPanel currentRightPanel;

    /**
     * Main method of our application.
     * @param args arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Arimaa");
            createAndShowGUI();
        });

    }

    /**
     * Method to change the right panel with a given panel.
     * @param newPanel The newly given panel.
     */
    public static void changeRightPanel(JPanel newPanel) {
        frame.remove(currentRightPanel);
        frame.add(newPanel, BorderLayout.EAST);
        currentRightPanel = newPanel;
        frame.pack();
        frame.repaint();
    }



    private static void showNewGameDialog(JFrame parentFrame, boolean vsComputer) {
        String gameType = vsComputer ? "single-player" : "multiplayer";
        JDialog newGameDialog = new JDialog(parentFrame, "New " + gameType + " game", true);
        newGameDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Player 1 field
        JLabel player1Label = new JLabel("Player 1 (Yellow):");
        JTextField player1TextField = new JTextField(20);
        player1TextField.setDocument(new javax.swing.text.PlainDocument() {
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (getLength() + str.length() <= 20 && !str.contains(" ")) {
                    super.insertString(offs, str, a);
                }
            }
        });

        // Player 2 field
        JLabel player2Label = new JLabel("Player 2 (Blue):");
        JTextField player2TextField = new JTextField(20);
        player1TextField.setDocument(new javax.swing.text.PlainDocument() {
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (getLength() + str.length() <= 20 && !str.contains(" ")) {
                    super.insertString(offs, str, a);
                }
            }
        });

        if (vsComputer) {
            player2TextField.setText("Computer");
            player2TextField.setEditable(false);
        }
        JButton startGameButton = new JButton("Start game");
        // Action listener fot the start button
        startGameButton.addActionListener(e -> {
            newGameDialog.dispose();
            String player1Name = player1TextField.getText().replaceAll("\\s", "");
            player1Name = player1Name.equals("Computer") || player1Name.equals("computer") ? "oneName" : player1Name;
            String player2Name = player2TextField.getText().replaceAll("\\s", "");
            player2Name = !vsComputer && player2Name.equals("Computer") || player2Name.equals("computer") ? "twoName" : player2Name;
            startNewGame(player1Name, player2Name, vsComputer);

        });
        // Lay the components out
        c.gridx = 0;
        c.gridy = 0;
        newGameDialog.add(player2Label, c);
        c.gridx = 1;
        c.gridy = 0;
        newGameDialog.add(player2TextField, c);
        c.gridx = 0;
        c.gridy = 1;
        newGameDialog.add(player1Label, c);
        c.gridx = 1;
        c.gridy = 1;
        newGameDialog.add(player1TextField, c);
        c.gridx = 1;
        c.gridy = 2;
        newGameDialog.add(startGameButton, c);
        newGameDialog.pack();
        newGameDialog.setLocationRelativeTo(parentFrame);
        newGameDialog.setVisible(true);
    }

    /**
     * Method to start a new single-player/multiplayer game.
     *
     * @param player1name name of the first player
     * @param player2name name of the second player
     * @param vsComputer boolean whether this game is vs computer (single-player)
     */
    private static void startNewGame(String player1name, String player2name, boolean vsComputer){
        Player player1 = new Player(1, false, player1name);
        Player player2 = new Player(2, vsComputer, player2name);
        Game game = new Game(player1, player2);
        game.getBoard().populateBoardFrom2DString(Game.DEFAULT_BOARD, player1, player2);
        GameControlsPanel gameControlsPanel = new GameControlsPanel(game, labeledBoardPanel, "00:00:00", "00:00:00");
        game.setGameListener(gameControlsPanel);
        labeledBoardPanel.setGame(game);
        changeRightPanel(gameControlsPanel);
    }

    /**
     * Method to create and show frame GUI including comprehensive listeners analyzing loaded arimaa files and setting up game/ showing game history.
     */
    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The menu
        JMenuBar menuBar = new JMenuBar();

        JMenu newGameMenu = new JMenu("Start a new game");
        JMenuItem multiplayerItem = new JMenuItem("Multiplayer");
        newGameMenu.add(multiplayerItem);
        JMenuItem singleplayerItem = new JMenuItem("Single-player");
        newGameMenu.add(singleplayerItem);

        JMenu loadSavedGameMenu = new JMenu("Load a saved game");
        JMenuItem continuePlayingItem = new JMenuItem("for playing");
        loadSavedGameMenu.add(continuePlayingItem);
        JMenuItem viewStepsItem = new JMenuItem("for viewing");
        loadSavedGameMenu.add(viewStepsItem);

        menuBar.add(newGameMenu);
        menuBar.add(loadSavedGameMenu);
        frame.setJMenuBar(menuBar);

        labeledBoardPanel = new LabeledBoardPanel(new Game(new Player(1, false), new Player(2, false)));
        frame.add(labeledBoardPanel, BorderLayout.CENTER);

        WelcomePanel welcomePanel = new WelcomePanel();
        frame.add(welcomePanel, BorderLayout.EAST);
        currentRightPanel = welcomePanel;

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        multiplayerItem.addActionListener(e -> {
            showNewGameDialog(frame, false);
        });
        singleplayerItem.addActionListener(e -> {
            showNewGameDialog(frame, true);
        });

        continuePlayingItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (isValidArimaaFileName(selectedFile)) {
                    // initialize info to be extracted from the file
                    String name1 = "";
                    String time1 = "";
                    String name2 = "";
                    String time2 = "";
                    String result = "";
                    ArrayList<String> moveLinesArrayList = new ArrayList<>();
                    ArrayList<String> movesArrayList = new ArrayList<>();
                    String turn = "";
                    int movesLeft = 4;
                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                        String line;
                        int lineNumber = -1;
                        String lastLine = "";
                        while ((line = br.readLine()) != null) {
                            lineNumber++;
                            lastLine = line;
                            // first five lines 6 lines describe game info
                            if (0 < lineNumber && lineNumber < 6){
                                switch (lineNumber){
                                    case 1 -> name1 = line.substring("Yellow (g): ".length()).trim();
                                    case 2 -> time1 = line.substring("Yellow time: ".length()).trim();
                                    case 3 -> name2 = line.substring("Blue (s): ".length()).trim();
                                    case 4 -> time2 = line.substring("Blue time: ".length()).trim();
                                    case 5 -> result = line.substring("Result: ".length()).trim();
                                }
                            // 8th line and further describe individual turns
                            } else if (lineNumber > 6){
                                moveLinesArrayList.add(line);
                                // if line has actual moves, add them for later processing
                                List<String> tentativeMoves = new ArrayList<>(List.of(line.replaceAll("[\n]+$", "").replaceAll("resigns", "").split(" ")));
                                if (tentativeMoves.size()>1){
                                    tentativeMoves.remove(0);
                                    movesArrayList.addAll(tentativeMoves);
                                }

                            }
                        }
                        if (result.equals("1-0") || result.equals("0-1")){
                            JOptionPane.showMessageDialog(frame, "You cannot continue playing a game that has already ended.", "Invalid file", JOptionPane.WARNING_MESSAGE);
                            changeRightPanel(new WelcomePanel());
                            return;
                        }
                        // Get the moves left this turn by examining last line of the log
                        String[] lastLineSplit = lastLine.split(" ");
                        int itemNumber = -1;
                        int movesMade = 0;
                        for (String item : lastLineSplit){
                            itemNumber++;
                            if (itemNumber == 0){
                                turn = item;
                            // item automatically removed from the board is logged but does not count towards moves left
                            } else if (!item.substring(3).equals("x")){
                                movesMade++;
                            }
                        }
                        movesLeft -= movesMade;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Create new players based on extracted names
                    Player player1 = new Player(1, false, name1);
                    Player player2;
                    if (name2.equals("Computer")){
                        player2 = new Player(2, true, name2);
                    } else {
                        player2 = new Player(2, false, name2);
                    }

                    // Create new game and set its state
                    Game game = new Game(player1, player2);
                    game.setMovesLeftThisTurn(movesLeft);
                    if (turn.charAt(1) == 's'){
                        game.setCurrentPlayer(player2);
                        game.setEnemyPlayer(player1);
                        game.setGamePhase(Character.getNumericValue(turn.charAt(0)) *2);
                    } else {
                        game.setCurrentPlayer(player1);
                        game.setEnemyPlayer(player2);
                        game.setGamePhase(Character.getNumericValue(turn.charAt(0))*2-1);
                    }
                    // Append the log to the new game so that it starts logging right where it left off
                    game.appendStepsBuilder("\n");
                    for (String line : moveLinesArrayList){
                        game.appendStepsBuilder("\n"+line);
                    }
                    // Apply the extracted moves to the game board to get the final state
                    for (String move : movesArrayList){
                        if (move.length() == 3){
                            // extract info (Ra7 - place Gold rabbit on position a7)
                            String pieceString = move.substring(0, 1);
                            Player pieceOwner;
                            if (pieceString.toUpperCase().equals(pieceString)){
                                pieceOwner = player1;
                            } else {
                                pieceOwner = player2;
                            }
                            Piece piece = Piece.createPieceFromNotationPlayerWithSpecificPlayer(pieceString, pieceOwner);
                            String positionString = move.substring(1,3);
                            Position position = Position.fromString(positionString);
                            assert position != null;
                            game.getBoard().placePiece(piece, position);
                        } else if (move.length() == 4){
                            // extract info (dc3n - move Silver dog on position c3 one place north)
                            String positionString = move.substring(1, 3);
                            Position positionFrom = Position.fromString(positionString);
                            char directionChar = move.charAt(3);
                            Direction direction = Direction.fromNotation(directionChar);
                            assert positionFrom != null;
                            assert direction != null;
                            Position positionTo = positionFrom.getAdjacentPosition(direction);
                            if (positionFrom.equals(positionTo)){
                                game.getBoard().removePieceAt(positionFrom);
                            } else {
                                StepMove stepMove = new StepMove(positionFrom, positionTo);
                                game.getBoard().makeMove(stepMove);
                            }
                        }
                    }
                    // Create GameControlsPanel with our analyzed game and set game listener
                    GameControlsPanel gameControlsPanel = new GameControlsPanel(game, labeledBoardPanel, time1, time2);
                    game.setGameListener(gameControlsPanel);
                    // Display changes
                    labeledBoardPanel.setGame(game);
                    changeRightPanel(gameControlsPanel);
                } else {
                    JOptionPane.showMessageDialog(frame, "This file is not a valid Arimaa game.", "Invalid file", JOptionPane.WARNING_MESSAGE);
                    changeRightPanel(new WelcomePanel());
                }
            }
        });

        viewStepsItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                if (isValidArimaaFileName(selectedFile)) {
                    // Read the file content starting from the 9th line
                    StringBuilder content = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                        String line;
                        int lineNumber = 0;

                        while ((line = br.readLine()) != null) {
                            lineNumber++;
                            if (lineNumber > 7) {
                                content.append(line).append("\n");
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // display changes
                    labeledBoardPanel.setGame(new Game(new Player(1, false), new Player(2, false)));
                    changeRightPanel(new HistoryPanel(content.toString(), labeledBoardPanel));
                } else {
                    JOptionPane.showMessageDialog(frame, "This file is not a valid Arimaa game.", "Invalid file", JOptionPane.WARNING_MESSAGE);
                    changeRightPanel(new WelcomePanel());
                }
            }
        });

    }

    /**
     * Method to check whether the loaded file was generated by the program by examining the file name and file type.
     *
     * @param file The file to be loaded.
     * @return Boolean value of the check.
     */
    private static boolean isValidArimaaFileName(File file) {
        String fileName = file.getName();
        // Check if it's a txt file
        if (!fileName.endsWith(".txt")) return false;
        // Check if the file name follows the required format
        String regex = "arimaa-\\d{2}-\\d{2}-\\d{4}-\\d{2}-\\d{2}-\\d{2}\\.txt";
        return fileName.matches(regex);
    }

}
