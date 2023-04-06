package arimaa.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arimaa.core.Piece;
import arimaa.core.StepMove;
import arimaa.utils.Direction;
import arimaa.utils.Position;

public class HistoryPanel extends JPanel {

    /**
     * JTextArea to be filled with game history text.
     */
    private final JTextArea longText;

    /**
     * Green highlighter for next moves.
     */
    private final Highlighter.HighlightPainter greenHighlighter;

    /**
     * Red highlighter for previous (inverse) moves
     */
    private final Highlighter.HighlightPainter redHighlighter;

    /**
     * BoardPanel enhanced with row/column labels enabling panel-board communication
     */
    private final LabeledBoardPanel labeledBoardPanel;

    /**
     * Direction variable used to catch when user changes direction of reading.
     */
    private boolean direction = true;
    /**
     * Instance variable: starting index of the currently highlighted area of the current step
     */
    private int currentStart = -1;
    /**
     * Instance variable: ending index of the currently highlighted area of the current step
     */
    private int currentEnd = -1;

    /**
     * Constructs a new HistoryPanel (right panel for interacting with the game history)
     * @param content text with the individual steps
     * @param labeledBoardPanel Panel to communicate with the board and display individual pieces.
     */
    public HistoryPanel(String content, LabeledBoardPanel labeledBoardPanel) {
        this.labeledBoardPanel = labeledBoardPanel;
        setLayout(new BorderLayout());
        // Control buttons
        JButton nextButton = new JButton("Next");
        JButton previousButton = new JButton("Previous");
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        buttonsPanel.add(previousButton);
        buttonsPanel.add(nextButton);
        add(buttonsPanel, BorderLayout.NORTH);
        // TextArea with game history using in Arimaa notation
        longText = new JTextArea(content);
        longText.setLineWrap(false);
        longText.setWrapStyleWord(true);
        longText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(longText);
        scrollPane.getViewport().setPreferredSize(new Dimension(450, 300));
        add(scrollPane, BorderLayout.CENTER);
        // Initialize highlighters
        greenHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
        redHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        // Attach action listeners
        nextButton.addActionListener(e -> highlightCurrentMove(true));
        previousButton.addActionListener(e -> highlightCurrentMove(false));
    }

    /**
     * Method to identify next/previous/current part of text, which is a move, and act upon it
     *
     * @param nextMove If we want to find the next move
     */
    private void highlightCurrentMove(boolean nextMove) {
        longText.getHighlighter().removeAllHighlights();
        // here we detect if there is no need to find next/previous match
        if (direction != nextMove && currentStart >= 0 && currentEnd >= 0) {
            direction = nextMove;
            highlightAndImplementChanges(nextMove);
            return;
        }

        // initialize required pattern and apply matcher to text
        Pattern pattern = Pattern.compile("[A-Za-z][a-h][1-8][neswx]?");
        Matcher matcher = pattern.matcher(longText.getText());

        // set the indexes to default
        int start = -1;
        int end = -1;

        // find the indexes of the next Arimaa step
        while (matcher.find()) {
            // next move: find indexes after currentStart
            if (nextMove) {
                if (matcher.start() > currentStart) {
                    System.out.println(matcher);
                    start = matcher.start();
                    end = matcher.end();
                    break;
                }
            // previous move: find indexes before currentEnd
            } else {
                if (matcher.end() < currentEnd) {
                    start = matcher.start();
                    end = matcher.end();
                }
            }
        }

        // if we found non-default indexes, let's highlight the move and apply changes to the board
        if (start != -1 && end != -1) {
            currentStart = start;
            currentEnd = end;
            highlightAndImplementChanges(nextMove);
        } else if (!nextMove) {
            currentStart = -1;
            currentEnd = -1;
        }
    }

    /**
     * Method to highlight text and implement changes to board based on next move
     *
     * @param nextMove boolean true(next button) false(previous button)
     */
    private void highlightAndImplementChanges(boolean nextMove){
        try {
            // NextMove: we want to highlight green and possibly redo the move
            if (nextMove){
                longText.getHighlighter().addHighlight(currentStart, currentEnd, greenHighlighter);
            // PreviousMove: we want to highlight red and possibly redo inverse move
            } else {
                longText.getHighlighter().addHighlight(currentStart, currentEnd, redHighlighter);
            }
            // If the highlighted portion is not in the visible area, scroll to visible
            Rectangle2D rect = longText.modelToView2D(currentStart);
            if (rect != null) {
                longText.scrollRectToVisible(rect.getBounds());
            }
            implementChangesFromNotation(currentStart, currentEnd, nextMove);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to act on board (logically and visually) based on the found Move written in Arimaa notation
     *
     * @param start Starting index of Arimaa notation
     * @param end Ending index of Arimaa notation
     * @param nextMove Boolean value whether user clicked the NEXT button
     */
    public void implementChangesFromNotation(int start, int end, boolean nextMove){
        boolean shouldBeInverse = !nextMove;
        // 3-letter Arimaa notation signifies insertion
        if (end - start == 3){
            // extract info (Ra7 - place Gold rabbit on position a7)
            String pieceString = longText.getText().substring(start, start + 1);
            Piece piece = Piece.createPieceFromNotationWithGeneralPlayer(pieceString);
            String positionString = longText.getText().substring(start + 1, end);
            Position position = Position.fromString(positionString);
            // inverse: if we inserted piece on the board, PREVIOUS button should remove it
            if (shouldBeInverse){
                labeledBoardPanel.removePieceAt(position);
            } else {
                labeledBoardPanel.placePieceAt(piece, position);
            }
        // 4-letter Arimaa notation signifies move or deletion
        } else if (end - start > 3){
            // extract info (Rc3x - remove gold rabbit from trap at position c3)
            String pieceString = longText.getText().substring(start, start + 1);
            Piece piece = Piece.createPieceFromNotationWithGeneralPlayer(pieceString);
            String positionString = longText.getText().substring(start + 1, end - 1);
            Position positionFrom = Position.fromString(positionString);
            char directionChar = longText.getText().charAt(end - 1);
            Direction direction = Direction.fromNotation(directionChar);
            assert positionFrom != null;
            assert direction != null;
            // get new position in specified direction
            Position positionTo = positionFrom.getAdjacentPosition(direction);
            // new direction equals the previous one is the test for removal
            if (positionFrom.equals(positionTo)){
                // inverse: if we removed piece from the board (by being on trap), PREVIOUS button should place it back
                if (shouldBeInverse){
                    labeledBoardPanel.placePieceAt(piece, positionFrom);
                } else {
                    labeledBoardPanel.removePieceAt(positionFrom);
                }
            } else {
                // inverse: if we moved a piece, PREVIOUS button should move it from destination to start (in the opposition direction)
                StepMove stepMove = shouldBeInverse ? new StepMove(positionTo, positionFrom) : new StepMove(positionFrom, positionTo);
                labeledBoardPanel.movePiece(stepMove);
            }
        }
    }

}

