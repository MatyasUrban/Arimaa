package arimaa.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arimaa.core.Piece;
import arimaa.core.StepMove;
import arimaa.utils.Direction;
import arimaa.utils.Position;

public class HistoryPanel extends JPanel {

    private JButton previousButton;
    private JButton nextButton;
    private JTextArea longText;

    private boolean direction = true;
    private List<Integer> moveIndices;
    private Highlighter.HighlightPainter greenHighlighter;

    private Highlighter.HighlightPainter redHighlighter;

    private LabeledBoardPanel labeledBoardPanel;


    public HistoryPanel(String content, LabeledBoardPanel labeledBoardPanel) {
        this.labeledBoardPanel = labeledBoardPanel;
        setLayout(new BorderLayout());
        JButton nextButton = new JButton("Next");
        JButton previousButton = new JButton("Previous");
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        buttonsPanel.add(previousButton);
        buttonsPanel.add(nextButton);
        add(buttonsPanel, BorderLayout.NORTH);
        longText = new JTextArea(content);
        longText.setLineWrap(false);
        longText.setWrapStyleWord(true);
        longText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(longText);
        scrollPane.getViewport().setPreferredSize(new Dimension(450, 300));
        add(scrollPane, BorderLayout.CENTER);
        greenHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
        redHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

        nextButton.addActionListener(e -> {
            highlightCurrentMove(true);
        });
        previousButton.addActionListener(e -> {
            highlightCurrentMove(false);
        });
    }


    private int currentStart = -1;
    private int currentEnd = -1;

    private void highlightCurrentMove(boolean nextMove) {
        if (direction != nextMove && currentStart >= 0 && currentEnd >= 0) {
            direction = nextMove;
            longText.getHighlighter().removeAllHighlights();
            try {
                if (nextMove){
                    longText.getHighlighter().addHighlight(currentStart, currentEnd, greenHighlighter);
                } else {
                    longText.getHighlighter().addHighlight(currentStart, currentEnd, redHighlighter);
                }
                implementChangesFromNotation(currentStart, currentEnd, nextMove);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            return;
        }

        longText.getHighlighter().removeAllHighlights();

        Pattern pattern = Pattern.compile("[A-Za-z][a-h][1-8][neswx]?");
        Matcher matcher = pattern.matcher(longText.getText());

        int start = -1;
        int end = -1;

        while (matcher.find()) {
            if (nextMove) {
                if (matcher.start() > currentStart) {
                    System.out.println(matcher);
                    start = matcher.start();
                    end = matcher.end();
                    break;
                }
            } else {
                if (matcher.end() < currentEnd) {
                    start = matcher.start();
                    end = matcher.end();
                }
            }
        }

        if (start != -1 && end != -1) {
            currentStart = start;
            currentEnd = end;

            try {
                if (nextMove){
                    longText.getHighlighter().addHighlight(currentStart, currentEnd, greenHighlighter);
                } else {
                    longText.getHighlighter().addHighlight(currentStart, currentEnd, redHighlighter);
                }
                Rectangle2D rect = longText.modelToView2D(currentStart);
                if (rect != null) {
                    longText.scrollRectToVisible(rect.getBounds());
                }
                implementChangesFromNotation(currentStart, currentEnd, nextMove);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else if (!nextMove) {
            currentStart = -1;
            currentEnd = -1;
        }
    }

    public void implementChangesFromNotation(int start, int end, boolean nextMove){
        boolean shouldBeInversed = !nextMove;
        if (end - start == 3){
            String pieceString = longText.getText().substring(start, start + 1);
            Piece piece = Piece.createPieceFromNotationWithGeneralPlayer(pieceString);
            String positionString = longText.getText().substring(start + 1, end);
            Position position = Position.fromString(positionString);
            if (shouldBeInversed){
                labeledBoardPanel.removePieceAt(position);
            } else {
                labeledBoardPanel.placePieceAt(piece, position);
            }
        } else if (end - start > 3){
            String pieceString = longText.getText().substring(start, start + 1);
            Piece piece = Piece.createPieceFromNotationWithGeneralPlayer(pieceString);
            String positionString = longText.getText().substring(start + 1, end - 1);
            Position positionFrom = Position.fromString(positionString);
            Direction direction = Direction.fromNotation(longText.getText().charAt(end - 1));
            assert positionFrom != null;
            assert direction != null;
            Position positionTo = new Position(positionFrom.row() + direction.getDRow(), positionFrom.column() + direction.getDColumn());
            if (positionFrom.equals(positionTo)){
                if (shouldBeInversed){
                    labeledBoardPanel.placePieceAt(piece, positionFrom);
                } else {
                    labeledBoardPanel.removePieceAt(positionFrom);
                }
            } else {
                StepMove stepMove = shouldBeInversed ? new StepMove(positionTo, positionFrom) : new StepMove(positionFrom, positionTo);
                labeledBoardPanel.movePiece(stepMove);
            }
        }
    }

}

