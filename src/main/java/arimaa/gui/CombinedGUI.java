package arimaa.gui;

import arimaa.core.Game;
import arimaa.core.Player;
import arimaa.utils.GameValidator;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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



    private static void showMultiplayerDialog(JFrame parentFrame) {
        JDialog multiplayerDialog = new JDialog(parentFrame, "New multiplayer game", true);
        multiplayerDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel player1Label = new JLabel("Player 1 (Yellow):");
        JTextField player1TextField = new JTextField(20);

        JLabel player2Label = new JLabel("Player 2 (Blue):");
        JTextField player2TextField = new JTextField(20);

        JButton startGameButton = new JButton("Start game");
        startGameButton.addActionListener(e -> {
            // Your code to start a new multiplayer game with the entered names
            String player1Name = player1TextField.getText().replaceAll("\\s", ""); // Remove spaces
            String player2Name = player2TextField.getText().replaceAll("\\s", ""); // Remove spaces
            multiplayerDialog.dispose();
            Player player1 = new Player(1, false, player1Name);
            Player player2 = new Player(2, false, player2Name);
            // Multiplayer game
            Game game = new Game(player1, player2);
            game.getBoard().populateBoardFrom2DString(Game.DEFAULT_BOARD, player1, player2);
            GameControlsPanel gameControlsPanel = new GameControlsPanel(game, labeledBoardPanel);
            game.setGameListener(gameControlsPanel);
            labeledBoardPanel.setBoard(game.getBoard());
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
        multiplayerItem.addActionListener(e -> {
            showMultiplayerDialog(frame);
        });
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
        labeledBoardPanel = new LabeledBoardPanel(new Game().getBoard());
        frame.add(labeledBoardPanel, BorderLayout.CENTER);

        WelcomePanel welcomePanel = new WelcomePanel();
        frame.add(welcomePanel, BorderLayout.EAST);
        currentPanel = welcomePanel;

        frame.setResizable(false);
        frame.pack(); // Pack the components of the JFrame
        frame.setVisible(true);

        // Add action listeners to handle menu clicks
        // Move this block of code to the end of the main method
        singleplayerItem.addActionListener(e -> {
            // Add your singleplayer game logic here
            // For now, just changing the right panel to GameControlsPanel
            changeRightPanel(new GameControlsPanel(new Game(), labeledBoardPanel));
        });

        continuePlayingItem.addActionListener(e -> {
            // Add your continue playing game logic here
            // For now, just changing the right panel to GameControlsPanel
            changeRightPanel(new GameControlsPanel(new Game(), labeledBoardPanel));
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
                    labeledBoardPanel.setBoard(new Game().getBoard());
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
        if (!GameValidator.isValidGame(file)) {
            return false;
        }

        return true;
    }

}
