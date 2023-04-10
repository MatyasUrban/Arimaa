package arimaa.gui;

import arimaa.core.*;
import arimaa.utils.BoardMode;
import arimaa.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

class LabeledBoardPanel extends JPanel {

    private BoardPanel boardPanel;

    public LabeledBoardPanel(Board board) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        boardPanel = new BoardPanel(board);

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

    public JPanel getBoardPanel(){
        return boardPanel;
    }


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

//    public void resetBoardToDefault(){
//        boardPanel.resetBoardToDefault();
//    }

    public void setBoard(Board board){
        boardPanel.setBoard(board);

    }

    public void emptyTheBoard() {
        boardPanel.emptyTheBoard();
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

    public void fillSquareWithColor(Position position, Color color){
        boardPanel.fillSquareWithColor(position, color);
    }

    public void fillSquaresWithColor(ArrayList<Position> positions, Color color){
        boardPanel.fillSquaresWithColor(positions, color);
    }

    public void fillSquareWithBoard(){
        boardPanel.fillSquaresWithBoard();
    }

    public Position getPositionBySquare(JPanel sqaure){
        return boardPanel.getPositionFromSquare(sqaure);
    }

    public void addMouseListenerToSquares(MouseListener mouseListener){
        boardPanel.addMouseListenerToSquares(mouseListener);
    }

    public void switchPiecesMode(Game game, Player player, Runnable onFinish){
        boardPanel.switchPiecesMode(game, player, onFinish);
    }

    public JPanel[][] getSquares(){
        return boardPanel.getSquares();
    }

    public void setBoardMode(BoardMode boardMode){boardPanel.setBoardMode(boardMode);}

    public void resetSquaresColors(){
        boardPanel.resetSquaresColors();
    }

}
