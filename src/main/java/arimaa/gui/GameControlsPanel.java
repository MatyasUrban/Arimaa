package arimaa.gui;

import arimaa.core.Game;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameControlsPanel extends JPanel {

    private JLabel playerTopName;
    private JLabel playerTopTime;
    private JLabel playerBottomName;
    private JLabel playerBottomTime;
    private JLabel turnIndicator;
    private JLabel movesLeft;
    private ButtonGroup actionTypeGroup;
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

        stepButton = new JRadioButton("Step");
        stepButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, stepButton.getMinimumSize().height));

        stepButton.setOpaque(true);
        stepButton.setBackground(Color.ORANGE);
        pushButton = new JRadioButton("Push");
        pushButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, pushButton.getMinimumSize().height));

        pushButton.setOpaque(true);
        pushButton.setBackground(Color.MAGENTA);
        pullButton = new JRadioButton("Pull");
        pullButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, pullButton.getMinimumSize().height));

        pullButton.setOpaque(true);
        pullButton.setBackground(Color.RED);

        stepButton.setVisible(true);
        pushButton.setVisible(true);
        pullButton.setVisible(true);
        stepButton.setSelected(true);


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
        if (game.getGamePhase() < 3){
            radioButtonsPanel.setVisible(false);
            resignButton.setVisible(false);
        }

        add(group1, BorderLayout.NORTH);
        add(group3, BorderLayout.CENTER);
        add(group2, BorderLayout.SOUTH);
        finishedButton.addActionListener(e -> {

        });
        while (!game.getGameEnded()){
            if (game.getGamePhase() == 1){
                labeledBoardPanel.fillSquaresWithColor(game.getBoard().getPositionsOfPlayersPieces(game.getCurrentPlayer()), Color.white);
            }
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
