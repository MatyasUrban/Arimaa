package arimaa.gui;

import arimaa.core.*;
import arimaa.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * The LabeledBoardPanel class represents the game board extended with row and column labels and facilitate communication between game board and side panel.
 */
class LabeledBoardPanel extends JPanel {
    /**
     * Instance variable: the visual game board panel
     */
    private final BoardPanel boardPanel;

    /**
     * Constructs a new LabeledBoardPanel
     *
     * @param game Game whose state will the game board visualize
     */
    public LabeledBoardPanel(Game game) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        boardPanel = new BoardPanel(game);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(boardPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        add(createRowLabels(), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(createColumnLabels(), constraints);
    }

    /**
     * Method to create row labels for our visual game board.
     *
     * @return JPanel with row labels.
     */
    private JPanel createRowLabels() {
        JPanel rowLabelsPanel = new JPanel(new GridLayout(8, 1));
        for (int i = 8; i >= 1; i--) {
            JLabel rowLabel = new JLabel(Integer.toString(i), SwingConstants.CENTER);
            int leftPadding = 5;
            int rightPadding = 5;
            rowLabel.setBorder(BorderFactory.createEmptyBorder(0, leftPadding, 0, rightPadding));
            rowLabelsPanel.add(rowLabel);
        }
        return rowLabelsPanel;
    }

    /**
     * Method to create column labels for our visual game board.
     *
     * @return JPanel with column labels.
     */
    private JPanel createColumnLabels() {
        JPanel columnLabelsPanel = new JPanel(new GridLayout(1, 8));
        char[] columns = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (char column : columns) {
            JLabel columnLabel = new JLabel(Character.toString(column), SwingConstants.CENTER);
            int topPadding = 2;
            int bottomPadding = 2;
            columnLabel.setBorder(BorderFactory.createEmptyBorder(topPadding, 0, bottomPadding, 0));
            columnLabelsPanel.add(columnLabel);
        }
        return columnLabelsPanel;
    }

    public void placePieceAt(Piece piece, Position position){
        boardPanel.placePieceAt(piece, position);
    }

    public void removePieceAt(Position position){
        boardPanel.removePieceAt(position);
    }

    public void movePiece(StepMove stepMove){
        boardPanel.movePiece(stepMove);
    }

    public void setBoardMode(BoardMode boardMode){
        boardPanel.setBoardMode(boardMode);
    }

    public void resetSquaresColors(){
        boardPanel.resetSquaresColors();
    }

    public void setGame(Game game){
        boardPanel.setGame(game);
    }

    public void handleModeReset(){
        boardPanel.handleModeReset();
    }

    public ArrayList<Position> getPositionsOfSquaresWithColor(Color color) {
        return boardPanel.getPositionsOfSquaresWithColor(color);
    }

    public void clickOnRandomWhiteSquare(){
        boardPanel.clickOnRandomWhiteSquare();
    }

}
