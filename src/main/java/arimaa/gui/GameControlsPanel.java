package arimaa.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameControlsPanel extends JPanel {

    private JLabel player1Name;
    private JLabel player1Time;
    private JLabel player2Name;
    private JLabel player2Time;
    private JLabel turnIndicator;
    private JLabel movesLeft;
    private ButtonGroup actionTypeGroup;
    private JRadioButton stepButton;
    private JRadioButton pushButton;
    private JRadioButton pullButton;

    private JButton finishedButton;

    public GameControlsPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        player1Name = createColoredLabel("Player 1 (name)", Color.CYAN, 14);
        player1Time = createColoredLabel("00:00:00", Color.CYAN, 18, true);
        player2Name = createColoredLabel("Player 2 (name)", Color.YELLOW, 14);
        player2Time = createColoredLabel("00:00:00", Color.YELLOW, 18, true);

        turnIndicator = new JLabel("Player 1's Turn");
        turnIndicator.setForeground(Color.BLACK);
        movesLeft = new JLabel("Arrange your pieces");

        stepButton = new JRadioButton("Step");
        stepButton.setOpaque(true);
        stepButton.setBackground(Color.ORANGE);
        pushButton = new JRadioButton("Push");
        pushButton.setOpaque(true);
        pushButton.setBackground(Color.MAGENTA);
        pullButton = new JRadioButton("Pull");
        pullButton.setOpaque(true);
        pullButton.setBackground(Color.RED);

        stepButton.setVisible(false);
        pushButton.setVisible(false);
        pullButton.setVisible(false);


        actionTypeGroup = new ButtonGroup();
        actionTypeGroup.add(stepButton);
        actionTypeGroup.add(pushButton);
        actionTypeGroup.add(pullButton);

        finishedButton = new JButton("Finished");


        JPanel group1 = createGroupPanel(Color.BLACK);
        group1.add(player1Name);
        group1.add(Box.createRigidArea(new Dimension(0, 5)));
        group1.add(player1Time);

        JPanel group2 = createGroupPanel(Color.BLACK);
        group2.add(player2Name);
        group2.add(Box.createRigidArea(new Dimension(0, 5)));
        group2.add(player2Time);

        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setOpaque(false);
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        radioButtonsPanel.add(stepButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pushButton);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        radioButtonsPanel.add(pullButton);

        JPanel group3 = createGroupPanel(Color.YELLOW);
        group3.setLayout(new BoxLayout(group3, BoxLayout.Y_AXIS));

        group3.add(Box.createVerticalGlue());
        group3.add(turnIndicator);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(movesLeft);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(radioButtonsPanel);
        group3.add(Box.createRigidArea(new Dimension(0, 5)));
        group3.add(finishedButton);
        group3.add(Box.createVerticalGlue());

        add(group1, BorderLayout.NORTH);
        add(group3, BorderLayout.CENTER);
        add(group2, BorderLayout.SOUTH);
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
