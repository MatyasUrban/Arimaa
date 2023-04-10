package arimaa.gui;

import arimaa.core.Game;
import arimaa.core.GameListener;
import arimaa.core.Player;
import arimaa.utils.BoardMode;
import arimaa.utils.Position;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameControlsPanel extends JPanel implements GameListener {

    private JLabel playerTopName;
    private JLabel playerTopTime;
    private JLabel playerBottomName;
    private JLabel playerBottomTime;
    private JLabel turnIndicator;
    private JLabel movesLeft;
    private ButtonGroup actionTypeGroup;

    private JRadioButton switchButton;
    private JRadioButton noneButton;
    private JRadioButton stepButton;
    private JRadioButton pushButton;
    private JRadioButton pullButton;

    private JButton finishedButton;
    private JButton resignButton;

    private final Game game;

    private final LabeledBoardPanel labeledBoardPanel;

    public GameControlsPanel(Game game, LabeledBoardPanel labeledBoardPanel) {
        this.game = game;
        this.labeledBoardPanel = labeledBoardPanel;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        playerTopName = createColoredLabel("Player 2 (" + game.getPlayer2().getPlayerName() + ")", Color.CYAN, 14);
        playerTopTime = createColoredLabel("00:00:00", Color.CYAN, 18, true);
        playerBottomName = createColoredLabel("Player 1 (" + game.getPlayer1().getPlayerName() + ")", Color.YELLOW, 14);
        playerBottomTime = createColoredLabel("00:00:00", Color.YELLOW, 18, true);

        turnIndicator = new JLabel("Player 1's Turn");
        turnIndicator.setForeground(Color.BLACK);
        movesLeft = new JLabel("Arrange your pieces");

        switchButton = new JRadioButton("Switch");
        switchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, switchButton.getMinimumSize().height));
        switchButton.setOpaque(true);
        switchButton.setBackground(BoardMode.SWITCH.getColor());
        noneButton = new JRadioButton("None");
        noneButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, noneButton.getMinimumSize().height));
        noneButton.setOpaque(true);
        noneButton.setBackground(BoardMode.NONE.getColor());
        stepButton = new JRadioButton("Step");
        stepButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, stepButton.getMinimumSize().height));
        stepButton.setOpaque(true);
        stepButton.setBackground(BoardMode.STEP.getColor());
        pushButton = new JRadioButton("Push");
        pushButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, pushButton.getMinimumSize().height));
        pushButton.setOpaque(true);
        pushButton.setBackground(BoardMode.PUSH.getColor());
        pullButton = new JRadioButton("Pull");
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
        actionTypeGroup.add(stepButton);
        actionTypeGroup.add(pushButton);
        actionTypeGroup.add(pullButton);

        finishedButton = new JButton("Finished");
        finishedButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, finishedButton.getMinimumSize().height));
        resignButton = new JButton("Resign");
        resignButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, resignButton.getMinimumSize().height));



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
        radioButtonsPanel.add(stepButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pushButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pullButton);
        Color turnColor;
        if (game.getGamePhase() % 2 == 1){
            turnColor = Color.YELLOW;
        } else {
            turnColor = Color.CYAN;
        }
        JPanel group3 = createGroupPanel(turnColor);
        group3.setLayout(new BoxLayout(group3, BoxLayout.Y_AXIS));

        group3.add(Box.createVerticalGlue());
        group3.add(turnIndicator);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(movesLeft);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(radioButtonsPanel);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(finishedButton);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(resignButton);
        group3.add(Box.createVerticalGlue());


        add(group1, BorderLayout.NORTH);
        add(group3, BorderLayout.CENTER);
        add(group2, BorderLayout.SOUTH);
        finishedButton.addActionListener(e -> {
            if (game.getGamePhase() == 1) {
                // Remove MouseAdapter from player's pieces
                ArrayList<Position> playerPiecePositions = game.getBoard().getPositionsOfPlayersPieces(game.getCurrentPlayer());
                for (Position position : playerPiecePositions) {
                    for (MouseListener listener : labeledBoardPanel.getSquares()[position.row()][position.column()].getMouseListeners()) {
                        labeledBoardPanel.getSquares()[position.row()][position.column()].removeMouseListener(listener);
                    }
                }
                // Proceed to the next phase
            }
        });
        switchButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.SWITCH);
            labeledBoardPanel.fillSquaresWithColor(game.getBoard().getPositionsOfPlayersPieces(game.getCurrentPlayer()), Color.white);
        });
        noneButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.NONE);
            labeledBoardPanel.resetSquaresColors();
        });
        stepButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.STEP);
            labeledBoardPanel.fillSquaresWithColor(game.getBoard().getPositionsOfPlayersPiecesWhichCanStepMove(game.getCurrentPlayer()), Color.white);
        });
        pullButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.PULL);
            labeledBoardPanel.fillSquaresWithColor(game.getBoard().getPositionsOfEnemyPiecesWhichCanBePulled(game.getCurrentPlayer(), game.getEnemyPlayer()), Color.white);
        });
        pushButton.addActionListener(e -> {
            labeledBoardPanel.setBoardMode(BoardMode.PUSH);
            labeledBoardPanel.fillSquaresWithColor(game.getBoard().getPositionsOfEnemyPiecesThatCanBePushed(game.getCurrentPlayer(), game.getEnemyPlayer()), Color.white);
        });
    }

    // Add the following methods to implement the GameListener interface

    @Override
    public void onGamePhaseChanged(int gamePhase){
        Player player1 = game.getCurrentPlayer();
        Player enemy = game.getEnemyPlayer();
        if (gamePhase == 1){
            labeledBoardPanel.setBoardMode(BoardMode.SWITCH);
            switchButton.setSelected(true);
        }
    }

    @Override
    public void onGameEnded(Player winner) {
        JOptionPane.showMessageDialog(this, winner.getPlayerName() + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // Perform any other actions needed when the game ends, such as disabling controls
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
