package arimaa.gui;

import javax.swing.*;
import java.awt.*;

public class CombinedGUI {


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

            // Close the dialog
            multiplayerDialog.dispose();
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
            JFrame frame = new JFrame("Arimaa Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Create the menu bar and menu items
            JMenuBar menuBar = new JMenuBar();

            JMenu newGameMenu = new JMenu("Start a new game");
            JMenuItem multiplayerItem = new JMenuItem("Multiplayer");
            multiplayerItem.addActionListener(e -> {
                showMultiplayerDialog(frame);
            });;
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
            frame.add(new LabeledBoardPanel());
            GameControlsPanel gameControlsPanel = new GameControlsPanel();
            frame.add(new WelcomePanel(), BorderLayout.EAST);
            frame.setResizable(false);
            frame.pack(); // Pack the components of the JFrame
            frame.setVisible(true);
        });

    }
}
