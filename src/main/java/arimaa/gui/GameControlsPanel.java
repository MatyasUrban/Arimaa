package arimaa.gui;

import arimaa.core.Game;
import arimaa.core.GameListener;
import arimaa.utils.BoardMode;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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

    private final Game game;

    private final LabeledBoardPanel labeledBoardPanel;

    private final JPanel group3;

    public GameControlsPanel(Game game, LabeledBoardPanel labeledBoardPanel) {
        this.game = game;
        this.labeledBoardPanel = labeledBoardPanel;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        playerTopName = createColoredLabel("Player 2 BLUE (" + game.getPlayer2().getPlayerName() + ")", Color.CYAN, 14);
        playerTopTime = createColoredLabel("00:00:00", Color.CYAN, 18, true);
        playerBottomName = createColoredLabel("Player 1 YELLOW (" + game.getPlayer1().getPlayerName() + ")", Color.YELLOW, 14);
        playerBottomTime = createColoredLabel("00:00:00", Color.YELLOW, 18, true);

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
        group3.add(Box.createVerticalGlue());
        add(group1, BorderLayout.NORTH);
        add(group3, BorderLayout.CENTER);
        add(group2, BorderLayout.SOUTH);
        setTurnFormatting();
        finishedButton.addActionListener(e -> {
            labeledBoardPanel.resetSquaresColors();
            labeledBoardPanel.setBoardMode(BoardMode.NONE);
            labeledBoardPanel.handleModeReset();
            game.incrementPhase();
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
        } else {
            movesLeftTextPanel.setText("4 moves left");
            switchButton.setVisible(false);
            noneButton.setVisible(true);
            stepButton.setVisible(true);
            pushButton.setVisible(true);
            pullButton.setVisible(true);
            noneButton.setSelected(true);
        }
        group3.setBackground(turnColor);
    }

    // Add the following methods to implement the GameListener interface

    @Override
    public void onMovesLeftChanged(int movesLeft){
        if(game.getMovesLeftThisTurn() > 0){
            movesLeftTextPanel.setText(movesLeft + " moves lemoves left");
            if (game.getMovesLeftThisTurn() < 2){
                pushButton.setVisible(false);
                pullButton.setVisible(false);
            }
        } else {
            labeledBoardPanel.resetSquaresColors();
            labeledBoardPanel.setBoardMode(BoardMode.NONE);
            labeledBoardPanel.handleModeReset();
            game.incrementPhase();
            setTurnFormatting();
        }
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
