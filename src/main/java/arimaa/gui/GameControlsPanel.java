package arimaa.gui;

import arimaa.core.Game;
import arimaa.core.GameListener;
import arimaa.core.Player;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GameControlsPanel extends JPanel implements GameListener {


    private JLabel playerTopName;
    private JLabel playerTopTime;
    private JLabel playerBottomName;
    private JLabel playerBottomTime;
    private JLabel turnIndicatorTextPanel;
    private JLabel movesLeftTextPanel;
    private ButtonGroup actionTypeGroup;

    private JRadioButton switchButton;
    private JRadioButton noneButton;
    private JRadioButton stepButton;
    private JRadioButton pushButton;
    private JRadioButton pullButton;

    private JButton finishedButton;
    private JButton resignButton;
    private JButton saveButton;

    private final Game game;

    private final LabeledBoardPanel labeledBoardPanel;

    private final JPanel group3;

    GameClock gameClock;

    public GameControlsPanel(Game game, LabeledBoardPanel labeledBoardPanel, String player1time, String player2time) {
        this.game = game;
        this.labeledBoardPanel = labeledBoardPanel;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        playerTopName = createColoredLabel("Player 2 BLUE (" + game.getPlayer2().getPlayerName() + ")", Color.CYAN, 14);
        playerTopTime = createColoredLabel(player2time, Color.CYAN, 18, true);
        playerBottomName = createColoredLabel("Player 1 YELLOW (" + game.getPlayer1().getPlayerName() + ")", Color.YELLOW, 14);
        playerBottomTime = createColoredLabel(player1time, Color.YELLOW, 18, true);

        turnIndicatorTextPanel = new JLabel("Player 1's Turn");
        turnIndicatorTextPanel.setForeground(Color.BLACK);
        movesLeftTextPanel = new JLabel("Arrange your pieces");

        switchButton = new JRadioButton(BoardMode.SWITCH.getModeName());
        switchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, switchButton.getMinimumSize().height));
        switchButton.setOpaque(true);
        switchButton.setBackground(BoardMode.SWITCH.getColor());
        noneButton = new JRadioButton(BoardMode.NONE.getModeName());
        noneButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, noneButton.getMinimumSize().height));
        noneButton.setOpaque(true);
        noneButton.setBackground(BoardMode.NONE.getColor());
        stepButton = new JRadioButton(BoardMode.STEP.getModeName());
        stepButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, stepButton.getMinimumSize().height));
        stepButton.setOpaque(true);
        stepButton.setBackground(BoardMode.STEP.getColor());
        pushButton = new JRadioButton(BoardMode.PUSH.getModeName());
        pushButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, pushButton.getMinimumSize().height));
        pushButton.setOpaque(true);
        pushButton.setBackground(BoardMode.PUSH.getColor());
        pullButton = new JRadioButton(BoardMode.PULL.getModeName());
        pullButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, pullButton.getMinimumSize().height));
        pullButton.setOpaque(true);
        pullButton.setBackground(BoardMode.PULL.getColor());
        switchButton.setVisible(true);
        noneButton.setVisible(true);
        stepButton.setVisible(true);
        pushButton.setVisible(true);
        pullButton.setVisible(true);
        noneButton.setSelected(true);


        actionTypeGroup = new ButtonGroup();
        actionTypeGroup.add(switchButton);
        actionTypeGroup.add(noneButton);
        actionTypeGroup.add(stepButton);
        actionTypeGroup.add(pushButton);
        actionTypeGroup.add(pullButton);

        finishedButton = new JButton("Finished");
        finishedButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, finishedButton.getMinimumSize().height));
        resignButton = new JButton("Resign");
        resignButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, resignButton.getMinimumSize().height));
        saveButton = new JButton("Save Game State");
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, saveButton.getMinimumSize().height));



        JPanel group1 = createGroupPanel(Color.BLACK);
        group1.add(Box.createRigidArea(new Dimension(0, 7)));
        group1.add(playerTopName);
        group1.add(Box.createRigidArea(new Dimension(0, 5)));
        group1.add(playerTopTime);

        JPanel group2 = createGroupPanel(Color.BLACK);
        group2.add(playerBottomName);
        group2.add(Box.createRigidArea(new Dimension(0, 5)));
        group2.add(playerBottomTime);
        group2.add(Box.createRigidArea(new Dimension(0, 27)));

        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setOpaque(false);
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        radioButtonsPanel.add(switchButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(noneButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(stepButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pushButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pullButton);
        group3 = createGroupPanel(Color.WHITE);
        group3.setLayout(new BoxLayout(group3, BoxLayout.Y_AXIS));

        group3.add(Box.createVerticalGlue());
        group3.add(turnIndicatorTextPanel);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(movesLeftTextPanel);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(radioButtonsPanel);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(finishedButton);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(resignButton);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(saveButton);
        group3.add(Box.createVerticalGlue());
        add(group1, BorderLayout.NORTH);
        add(group3, BorderLayout.CENTER);
        add(group2, BorderLayout.SOUTH);
        setTurnFormatting();
        gameClock = new GameClock(this, game, player1time, player2time);
        if (game.getGamePhase() >= 3){
            onMovesLeftChanged(game.getMovesLeftThisTurn());
            System.out.println("Game phase: " + game.getGamePhase());
            gameClock.start();
        }
        finishedButton.addActionListener(e -> {
            labeledBoardPanel.resetSquaresColors();
            labeledBoardPanel.setBoardMode(BoardMode.NONE);
            labeledBoardPanel.handleModeReset();
            game.incrementPhase();
            System.out.println("Game phase: " + game.getGamePhase());
            if (game.getGamePhase() == 3){
                gameClock.start();
            }
            setTurnFormatting();

        });
        switchButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.SWITCH);
            labeledBoardPanel.handleModeReset();
        });
        noneButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.NONE);
            labeledBoardPanel.handleModeReset();
        });
        stepButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.STEP);
            labeledBoardPanel.handleModeReset();
        });
        pullButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.PULL);
            labeledBoardPanel.handleModeReset();
        });
        pushButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.PUSH);
            labeledBoardPanel.handleModeReset();
        });
        resignButton.addActionListener(e -> {
            game.endGame();
        });
        saveButton.addActionListener(e -> {
            gameClock.pauseClock();
            Date currentDate = new Date();
            saveToFile(gameToString(currentDate), currentDate);
            if (!game.getGameEnded()){
                gameClock.resumeClock();
            }
        });
    }

    public void updatePlayerTopTime(String time) {
        SwingUtilities.invokeLater(() -> playerTopTime.setText(time));
    }

    public void updatePlayerBottomTime(String time) {
        SwingUtilities.invokeLater(() -> playerBottomTime.setText(time));
    }




    private void setTurnFormatting(){
        Color turnColor;
        if (game.getGamePhase() % 2 == 1){
            turnColor = Color.YELLOW;
            turnIndicatorTextPanel.setText("Player 1's Turn");
        } else {
            turnColor = Color.CYAN;
            turnIndicatorTextPanel.setText("Player 2's Turn");
        }
        if (game.getGamePhase() <= 2){
            movesLeftTextPanel.setText("Arrange your pieces");
            switchButton.setVisible(true);
            noneButton.setVisible(true);
            stepButton.setVisible(false);
            pushButton.setVisible(false);
            pullButton.setVisible(false);
            noneButton.setSelected(true);
            resignButton.setVisible(false);
            saveButton.setVisible(false);
        } else {
            movesLeftTextPanel.setText("4 moves left");
            switchButton.setVisible(false);
            noneButton.setVisible(true);
            stepButton.setVisible(true);
            pushButton.setVisible(true);
            pullButton.setVisible(true);
            noneButton.setSelected(true);
            resignButton.setVisible(true);
            saveButton.setVisible(true);
        }
        group3.setBackground(turnColor);
    }

    // Add the following methods to implement the GameListener interface

    @Override
    public void onMovesLeftChanged(int movesLeft){
        if(game.getMovesLeftThisTurn() > 0){
            if (game.getMovesLeftThisTurn() < 2){
                if (pushButton.isSelected() || pullButton.isSelected()){
                    labeledBoardPanel.setBoardMode(BoardMode.NONE);
                    labeledBoardPanel.handleModeReset();
                }
                pushButton.setVisible(false);
                pullButton.setVisible(false);
                movesLeftTextPanel.setText(movesLeft + " move left");
            } else {
                movesLeftTextPanel.setText(movesLeft + " moves left");
            }
        } else {
            labeledBoardPanel.resetSquaresColors();
            labeledBoardPanel.setBoardMode(BoardMode.NONE);
            labeledBoardPanel.handleModeReset();
            game.incrementPhase();
            setTurnFormatting();
        }
    }

    public void saveToFile(String content, Date date) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a location to save the file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filter);

        // Set the default file name with the current date and time
        SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String defaultFileName = "arimaa-" + fileNameDateFormat.format(date) + ".txt";
        fileChooser.setSelectedFile(new File(defaultFileName));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Add .txt extension if not provided by the user
            if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (FileWriter fileWriter = new FileWriter(fileToSave)) {
                fileWriter.write(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public String gameToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDate = dateFormat.format(date);
        String gameState = "Game state as of: " + currentDate + "\n";
        String yellowInfo = "Yellow (g): " + game.getPlayer1().getPlayerName() + "\n";
        String yellowTime = "Yellow time: " + playerBottomTime.getText() + "\n";
        String blueInfo = "Blue (s): " + game.getPlayer2().getPlayerName() + "\n";
        String blueTime = "Blue time: " + playerTopTime.getText() + "\n";
        String steps = game.getStepsString();

        String result;
        if (!game.getGameEnded()) {
            result = "Result: 0-0\n";
        } else {
            result = game.getWinner() == game.getPlayer1() ? "Result: 1-0" : "Result: 0-1";
        }

        return gameState + yellowInfo + yellowTime + blueInfo + blueTime + result + steps;
    }



    @Override
    public void onGameEnded(Player winner){
        gameClock.stopClock();
        labeledBoardPanel.setBoardMode(BoardMode.NONE);
        labeledBoardPanel.handleModeReset();
        noneButton.setVisible(false);
        stepButton.setVisible(false);
        pushButton.setVisible(false);
        pullButton.setVisible(false);
        finishedButton.setVisible(false);
        resignButton.setVisible(false);
        movesLeftTextPanel.setVisible(false);
        turnIndicatorTextPanel.setText(winner.getPlayerName() + " won!");
        showWinnerPopup(winner);
    }

    public void showWinnerPopup(Player winner) {
        String message = winner.getPlayerName() + " won. Congratulations!";
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }



    private JLabel createColoredLabel(String text, Color color, int fontSize) {
        return createColoredLabel(text, color, fontSize, false);
    }

    private JLabel createColoredLabel(String text, Color color, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    private JPanel createGroupPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}
