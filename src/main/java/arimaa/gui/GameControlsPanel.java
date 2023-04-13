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

/**
 * The GameControlsPanel displays information about current turn and controls for individual moves while facilitating interaction between board, game, labeledboardpanel, and gamelistener.
 */
public class GameControlsPanel extends JPanel implements GameListener {

    /**
     * Label with the second/silver/blue player time
     */
    private final JLabel playerTopTime;
    /**
     * Label with the first/golden/yellow player time
     */
    private final JLabel playerBottomTime;
    /**
     * Label indicating whose turn it is
     */
    private final JLabel turnIndicatorTextPanel;
    /**
     * Label indicating how many moves are left this turn
     */
    private final JLabel movesLeftTextPanel;
    /**
     * Button to set the board panel to switch mode
     */
    private final JRadioButton switchButton;
    /**
     * Button to set the board panel to none mode
     */
    private final JRadioButton noneButton;
    /**
     * Button to set the board panel to step mode
     */
    private final JRadioButton stepButton;
    /**
     * Button to set the board panel to push mode
     */
    private final JRadioButton pushButton;
    /**
     * Button to set the board panel to pull mode
     */
    private final JRadioButton pullButton;
    /**
     * Button to make a random move (when it's computer's turn in a single-player)
     */
    private final JButton computerMoveButton;
    /**
     * Button to indicate that you're finished with your turn and do not wish to make more moves
     */
    private final JButton finishedButton;
    /**
     * Button to resign and make the opponent the winner
     */
    private final JButton resignButton;
    /**
     * Button to generate game log (which can be later loaded for history viewing or continuous game)
     */
    private final JButton saveButton;
    /**
     * Game currently acted on by the Game Controls Panel
     */
    private final Game game;
    /**
     * Labeled Board Panel displaying the game state
     */
    private final LabeledBoardPanel labeledBoardPanel;
    /**
     * Game clock on a separate thread to track decision times
     */
    private final GameClock gameClock;
    private final JPanel gameControlsGroup;

    /**
     * Constructs a new GameControlsPanel
     *
     * @param game Game to be acted on by game controls
     * @param labeledBoardPanel Labeled board panel displaying the game
     * @param player1time Initial time of the first player
     * @param player2time Initial time of the second player
     */
    public GameControlsPanel(Game game, LabeledBoardPanel labeledBoardPanel, String player1time, String player2time) {
        this.game = game;
        this.labeledBoardPanel = labeledBoardPanel;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // TOP SECTION - player 2 info
        JLabel playerTopName = createColoredLabel("Player 2 BLUE (" + game.getPlayer2().getPlayerName() + ")", Color.CYAN, 14, false);
        playerTopTime = createColoredLabel(player2time, Color.CYAN, 18, true);
        JPanel player2infoGroup = createGroupPanel(Color.BLACK);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 7)));
        player2infoGroup.add(playerTopName);
        player2infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        player2infoGroup.add(playerTopTime);

        // MIDDLE SECTION - turn info and game controls

        // Current turn info
        turnIndicatorTextPanel = new JLabel("Player 1's Turn");
        turnIndicatorTextPanel.setForeground(Color.BLACK);
        movesLeftTextPanel = new JLabel("Arrange your pieces");

        // Radio buttons for board panel mode
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
        ButtonGroup actionTypeGroup = new ButtonGroup();
        actionTypeGroup.add(switchButton);
        actionTypeGroup.add(noneButton);
        actionTypeGroup.add(stepButton);
        actionTypeGroup.add(pushButton);
        actionTypeGroup.add(pullButton);
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

        // Buttons for computer move, finishing turn without using all moves, resigning, saving the game log to file
        computerMoveButton = new JButton("Computer move");
        computerMoveButton.setBackground(Color.PINK);
        computerMoveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, computerMoveButton.getMinimumSize().height));
        finishedButton = new JButton("Finished");
        finishedButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, finishedButton.getMinimumSize().height));
        resignButton = new JButton("Resign");
        resignButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, resignButton.getMinimumSize().height));
        saveButton = new JButton("Save Game State");
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, saveButton.getMinimumSize().height));

        // Putting the middle section together
        gameControlsGroup = createGroupPanel(Color.WHITE);
        gameControlsGroup.setLayout(new BoxLayout(gameControlsGroup, BoxLayout.Y_AXIS));
        gameControlsGroup.add(Box.createVerticalGlue());
        gameControlsGroup.add(turnIndicatorTextPanel);
        gameControlsGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControlsGroup.add(movesLeftTextPanel);
        gameControlsGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControlsGroup.add(radioButtonsPanel);
        gameControlsGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControlsGroup.add(computerMoveButton);
        gameControlsGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControlsGroup.add(finishedButton);
        gameControlsGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControlsGroup.add(resignButton);
        gameControlsGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        gameControlsGroup.add(saveButton);
        gameControlsGroup.add(Box.createVerticalGlue());

        // BOTTOM SECTION - player 1 info
        JLabel playerBottomName = createColoredLabel("Player 1 YELLOW (" + game.getPlayer1().getPlayerName() + ")", Color.YELLOW, 14, false);
        playerBottomTime = createColoredLabel(player1time, Color.YELLOW, 18, true);
        JPanel player1infoGroup = createGroupPanel(Color.BLACK);
        player1infoGroup.add(playerBottomName);
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        player1infoGroup.add(playerBottomTime);
        player1infoGroup.add(Box.createRigidArea(new Dimension(0, 27)));

        // ALL SECTIONS TOGETHER
        add(player2infoGroup, BorderLayout.NORTH);
        add(gameControlsGroup, BorderLayout.CENTER);
        add(player1infoGroup, BorderLayout.SOUTH);

        // Show/set buttons and fields relevant for the current turn
        setTurnFormatting();
        gameClock = new GameClock(this, game, player1time, player2time);
        if (game.getGamePhase() >= 3){
            onMovesLeftChanged(game.getMovesLeftThisTurn());
            System.out.println("Game phase: " + game.getGamePhase());
            gameClock.start();
        }

        // Action listeners for all buttons
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
        computerMoveButton.addActionListener(e -> {
            // initial positions - computer is satisfied with default
            if (game.getGamePhase() <= 2){
                finishedButton.doClick();
            } else {
                // computer simulates StepMoves, selects at random, and uses all moves per turn
                if (!stepButton.isSelected()){
                    stepButton.doClick();
                } else {
                    int whiteSquaresCount = labeledBoardPanel.getPositionsOfSquaresWithColor(Color.WHITE).size();
                    if (whiteSquaresCount > 0) {
                        labeledBoardPanel.clickOnRandomWhiteSquare();
                    } else {
                        finishedButton.doClick();
                    }
                }
            }
        });
        resignButton.addActionListener(e -> {
            game.appendStepsBuilder("resigns");
            game.endGameByResign();
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

    /**
     * Method to update playerTopTime with hh:mm:ss (used by clock)
     * @param time hh:mm:ss
     */
    public void updatePlayerTopTime(String time) {
        SwingUtilities.invokeLater(() -> playerTopTime.setText(time));
    }

    /**
     * Method to update playerBottomTime with hh:mm:ss (used by clock)
     * @param time hh:mm:ss
     */
    public void updatePlayerBottomTime(String time) {
        SwingUtilities.invokeLater(() -> playerBottomTime.setText(time));
    }

    /**
     * Method to display info/controls relevant for the current turn.
     * 1. Set whose player's turn it is and set the respective color
     * 2. If we are just setting initial positions, offer switch mode
     * 3. Otherwise, do not offer switch mode while offering others
     * 4. If it's the computer's turn, offer only the computer move button control
     */
    private void setTurnFormatting(){
        Color turnColor;
        if (game.getGamePhase() % 2 == 1){
            turnColor = Color.YELLOW;
            turnIndicatorTextPanel.setText("Player 1's Turn");
        } else {
            turnColor = Color.CYAN;
            turnIndicatorTextPanel.setText("Player 2's Turn");
        }
        gameControlsGroup.setBackground(turnColor);
        if (game.getGamePhase() <= 2){
            movesLeftTextPanel.setText("Arrange your pieces");
            switchButton.setVisible(true);
            noneButton.setVisible(true);
            stepButton.setVisible(false);
            pushButton.setVisible(false);
            pullButton.setVisible(false);
            noneButton.setSelected(true);
            finishedButton.setVisible(true);
            resignButton.setVisible(false);
            saveButton.setVisible(false);
            computerMoveButton.setVisible(false);

        } else {
            movesLeftTextPanel.setText("4 moves left");
            switchButton.setVisible(false);
            noneButton.setVisible(true);
            stepButton.setVisible(true);
            pushButton.setVisible(true);
            pullButton.setVisible(true);
            noneButton.setSelected(true);
            finishedButton.setVisible(true);
            resignButton.setVisible(true);
            saveButton.setVisible(true);
            computerMoveButton.setVisible(false);
        }
        if (game.getCurrentPlayer().isComputer()){
            computerMoveButton.setVisible(true);
            finishedButton.setVisible(false);
            resignButton.setVisible(false);
            switchButton.setVisible(false);
            noneButton.setVisible(false);
            stepButton.setVisible(false);
            pushButton.setVisible(false);
            pullButton.setVisible(false);
        }

    }

    /**
     * Implements onMovesLeft fired after each move (hides push/pull if not enough moves left)
     * @param movesLeft Number of moves left in the current turn.
     */
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

    /**
     * Implements the onGameEnded by hiding game controls and displaying the winner.
     * @param winner The winner player of the game.
     */
    @Override
    public void onGameEnded(Player winner){
        gameClock.stopClock();
        labeledBoardPanel.setBoardMode(BoardMode.NONE);
        labeledBoardPanel.handleModeReset();
        noneButton.setVisible(false);
        stepButton.setVisible(false);
        pushButton.setVisible(false);
        pullButton.setVisible(false);
        computerMoveButton.setVisible(false);
        finishedButton.setVisible(false);
        resignButton.setVisible(false);
        movesLeftTextPanel.setVisible(false);
        turnIndicatorTextPanel.setText(winner.getPlayerName() + " won!");
        showWinnerPopup(winner);
    }

    /**
     * Shows the winner popup with the name of the winning player.
     * @param winner The winning player.
     */
    public void showWinnerPopup(Player winner) {
        String message = winner.getPlayerName() + " won. Congratulations!";
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method to save game log to a new file.
     *
     * @param content String content to be inserted into the file.
     * @param date Date to be used in file name and file content.
     */
    public void saveToFile(String content, Date date) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a location to save the file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filter);

        // Set the default file name with the current date and time
        SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String defaultFileName = "arimaa-" + fileNameDateFormat.format(date) + ".txt";

        // Handle file saving
        fileChooser.setSelectedFile(new File(defaultFileName));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Add .txt extension if not provided by the user
            if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            // Write the file
            try (FileWriter fileWriter = new FileWriter(fileToSave)) {
                fileWriter.write(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Method to generate a game log.
     *
     * @param date Date to be displayed on the game log.
     * @return String log to be saved as file contents
     */
    public String gameToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDate = dateFormat.format(date);
        String gameState = "Game state as of: " + currentDate + "\n";
        String yellowInfo = "Yellow (g): " + game.getPlayer1().getPlayerName() + "\n";
        String yellowTime = "Yellow time: " + playerBottomTime.getText() + "\n";
        String blueInfo = "Blue (s): " + game.getPlayer2().getPlayerName() + "\n";
        String blueTime = "Blue time: " + playerTopTime.getText() + "\n";
        String steps = game.getStepsBuilder().toString();

        String result;
        if (!game.getGameEnded()) {
            result = "Result: 0-0\n";
        } else {
            result = game.getWinner() == game.getPlayer1() ? "Result: 1-0" : "Result: 0-1";
        }

        return gameState + yellowInfo + yellowTime + blueInfo + blueTime + result + steps;
    }

    /**
     * Method to edit JLabel text formatting and create such label.
     *
     * @param text Text to be displayed.
     * @param color Color of the text.
     * @param fontSize Size of the font.
     * @param bold Whether the text should be displayed in bold.
     * @return Correctly formatted JLabel.
     */
    private JLabel createColoredLabel(String text, Color color, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    /**
     * Method to create our group panels with a specific background color.
     *
     * @param color Color of the background.
     * @return Group panel as a JPanel object.
     */
    private JPanel createGroupPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }




}
