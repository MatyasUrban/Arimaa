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

public class CombinedGUI {

    private static JFrame frame; // Add this line to create a static frame field
    private static LabeledBoardPanel labeledBoardPanel;
    private static JPanel currentPanel;



    public static void changeRightPanel(JPanel newPanel) {
        frame.remove(currentPanel);
        frame.add(newPanel, BorderLayout.EAST);
        currentPanel = newPanel;
        frame.pack();
        frame.repaint();
    }



    private static void showNewGameDialog(JFrame parentFrame, boolean vsComputer) {
        JDialog multiplayerDialog = new JDialog(parentFrame, "New multiplayer game", true);
        multiplayerDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel player1Label = new JLabel("Player 1 (Yellow):");
        JTextField player1TextField = new JTextField(20);

        JLabel player2Label = new JLabel("Player 2 (Blue):");
        JTextField player2TextField = new JTextField(20);
        if (vsComputer) {
            player2TextField.setText("Computer");
            player2TextField.setEditable(false);
        }

        JButton startGameButton = new JButton("Start game");
        startGameButton.addActionListener(e -> {
            // Your code to start a new multiplayer game with the entered names
            String player1Name = player1TextField.getText().replaceAll("\\s", ""); // Remove spaces
            String player2Name = player2TextField.getText().replaceAll("\\s", ""); // Remove spaces
            multiplayerDialog.dispose();
            Player player1 = new Player(1, false, player1Name);
            Player player2 = new Player(2, true, player2Name);
            // Multiplayer game
            Game game = new Game(player1, player2);
            game.getBoard().populateBoardFrom2DString(Game.DEFAULT_BOARD, player1, player2);
            GameControlsPanel gameControlsPanel = new GameControlsPanel(game, labeledBoardPanel, "00:00:00", "00:00:00");
            game.setGameListener(gameControlsPanel);
            labeledBoardPanel.setGame(game);
            changeRightPanel(gameControlsPanel);
            game.startGame();

        });

        // Add components to the dialog using GridBagConstraints
        c.gridx = 0;
        c.gridy = 0;
        multiplayerDialog.add(player1Label, c);

        c.gridx = 1;
        c.gridy = 0;
        multiplayerDialog.add(player1TextField, c);

        c.gridx = 0;
        c.gridy = 1;
        multiplayerDialog.add(player2Label, c);

        c.gridx = 1;
        c.gridy = 1;
        multiplayerDialog.add(player2TextField, c);

        c.gridx = 1;
        c.gridy = 2;
        multiplayerDialog.add(startGameButton, c);

        multiplayerDialog.pack();
        multiplayerDialog.setLocationRelativeTo(parentFrame);
        multiplayerDialog.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Arimaa");
            createAndShowGUI();
        });

    }

    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create the menu bar and menu items
        JMenuBar menuBar = new JMenuBar();

        JMenu newGameMenu = new JMenu("Start a new game");
        JMenuItem multiplayerItem = new JMenuItem("Multiplayer");

        JMenuItem singleplayerItem = new JMenuItem("Singleplayer");
        newGameMenu.add(multiplayerItem);
        newGameMenu.add(singleplayerItem);

        JMenu openSavedGameMenu = new JMenu("Open a saved game");
        JMenuItem continuePlayingItem = new JMenuItem("Continue playing");
        JMenuItem viewStepsItem = new JMenuItem("View steps");
        openSavedGameMenu.add(continuePlayingItem);
        openSavedGameMenu.add(viewStepsItem);

        menuBar.add(newGameMenu);
        menuBar.add(openSavedGameMenu);

        // Set the menu bar for the JFrame
        frame.setJMenuBar(menuBar);
        labeledBoardPanel = new LabeledBoardPanel(new Game(new Player(1, false), new Player(2, false)));
        frame.add(labeledBoardPanel, BorderLayout.CENTER);

        WelcomePanel welcomePanel = new WelcomePanel();
        frame.add(welcomePanel, BorderLayout.EAST);
        currentPanel = welcomePanel;

        frame.setResizable(false);
        frame.pack(); // Pack the components of the JFrame
        frame.setVisible(true);

        // Add action listeners to handle menu clicks
        // Move this block of code to the end of the main method
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
                if (isValidArimaaFile(selectedFile)) {
                    // Read the file content starting from the 9th line
                    String name1 = "", time1 = "", name2 = "", time2 = "", result = "";
                    ArrayList<String> moveLinesArrayList = new ArrayList<>();
                    ArrayList<String> moves = new ArrayList<>();
                    String turn = "";
                    int movesLeft = 4;
                    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                        String line;
                        int lineNumber = -1;

                        String lastLine = "";

                        while ((line = br.readLine()) != null) {
                            lineNumber++;
                            lastLine = line;
                            if (0 < lineNumber && lineNumber < 6){
                                switch (lineNumber){
                                    case 1 -> name1 = line.substring("Yellow (g): ".length()).trim();
                                    case 2 -> time1 = line.substring("Yellow time: ".length()).trim();
                                    case 3 -> name2 = line.substring("Blue (s): ".length()).trim();
                                    case 4 -> time2 = line.substring("Blue time: ".length()).trim();
                                    case 5 -> result = line.substring("Result: ".length()).trim();
                                }
                            } else if (lineNumber > 6){
                                moveLinesArrayList.add(line);
                                if (line.length() > 3) {
                                    moves.addAll(List.of(line.substring(3).trim().replaceAll("[\n]+$", "").split(" ")));
                                }
                            }
                        }
                        String[] lastLineSplit = lastLine.split(" ");
                        int itemNumber = -1;
                        int movesMade = 0;
                        for (String item : lastLineSplit){
                            itemNumber++;
                            if (itemNumber == 0){
                                turn = item;
                            } else if (!item.substring(3).equals("x")){
                                movesMade++;
                            }
                        }
                        movesLeft -= movesMade;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    Player player1 = new Player(1, false, name1);
                    Player player2;
                    if (name2.equals("Computer")){
                        player2 = new Player(2, true, name2);
                    } else {
                        player2 = new Player(2, false, name2);
                    }
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
                    for (String line : moveLinesArrayList){
                        game.appendStepsBuilder(line);
                    }
                    for (String move : moves){
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
                    GameControlsPanel gameControlsPanel = new GameControlsPanel(game, labeledBoardPanel, time1, time2);
                    game.setGameListener(gameControlsPanel);
                    labeledBoardPanel.setGame(game);
                    changeRightPanel(gameControlsPanel);
                    game.startGame();
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

                if (isValidArimaaFile(selectedFile)) {
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

                    // Pass the content to the HistoryPanel
                    labeledBoardPanel.setGame(new Game(new Player(1, false), new Player(2, false)));
                    changeRightPanel(new HistoryPanel(content.toString(), labeledBoardPanel));
                } else {
                    JOptionPane.showMessageDialog(frame, "This file is not a valid Arimaa game.", "Invalid file", JOptionPane.WARNING_MESSAGE);
                    changeRightPanel(new WelcomePanel());
                }
            }
        });

    }

    private static boolean isValidArimaaFile(File file) {
        String fileName = file.getName();

        // Check if it's a txt file
        if (!fileName.endsWith(".txt")) {
            return false;
        }

        // Check if the file name follows the required format
        String regex = "arimaa-\\d{2}-\\d{2}-\\d{4}-\\d{2}-\\d{2}-\\d{2}\\.txt";
        if (!fileName.matches(regex)) {
            return false;
        }

        // Check if the content is valid using GameValidator
        // that takes a File object as a parameter and returns a boolean
        if (false) {
            return false;
        }

        return true;
    }

}
