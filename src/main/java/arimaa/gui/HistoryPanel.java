package arimaa.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arimaa.core.Move;
import arimaa.utils.Direction;
import arimaa.utils.Position;

public class HistoryPanel extends JPanel {

    private JButton previousButton;
    private JButton nextButton;
    private JTextArea longText;
    private List<Integer> moveIndices;
    private int currentIndex;
    private Highlighter.HighlightPainter moveHighlightPainter;

    private LabeledBoardPanel labeledBoardPanel;


    public HistoryPanel(String content, LabeledBoardPanel board) {
        labeledBoardPanel = board;
        setLayout(new BorderLayout());
        JButton nextButton = new JButton("Next");
        add(nextButton, BorderLayout.NORTH);
        longText = new JTextArea(content);
        longText.setLineWrap(false);
        longText.setWrapStyleWord(true);
        longText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(longText);
        scrollPane.getViewport().setPreferredSize(new Dimension(500, 300));
        add(scrollPane, BorderLayout.CENTER);
        currentIndex = -1;
        moveHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);

        nextButton.addActionListener(e -> {
            highlightCurrentMove(true);
        });
    }


    private void highlightCurrentMove(boolean nextMove) {
        longText.getHighlighter().removeAllHighlights();

        Pattern pattern = Pattern.compile("[A-Za-z][a-h][1-8][neswx]?");
        Matcher matcher = pattern.matcher(longText.getText());

        int start = -1;
        int end = -1;

        while (matcher.find()) {
            if (nextMove) {
                if (matcher.start() > currentIndex) {
                    System.out.println(matcher);
                    start = matcher.start();
                    end = matcher.end();
                    break;
                }
            } else {
                if (matcher.end() < currentIndex) {
                    start = matcher.start();
                    end = matcher.end();
                } else {
                    break;
                }
            }
        }

        if (start != -1 && end != -1) {

            currentIndex = start;
            try {

                longText.getHighlighter().addHighlight(start, end, moveHighlightPainter);
                Rectangle2D rect = longText.modelToView2D(start);
                if (rect != null) {
                    longText.scrollRectToVisible(rect.getBounds());
                }
                if (end - start == 3){

                    String pieceString = longText.getText().substring(start, start + 1);
                    String positionString = longText.getText().substring(start + 1, end);
                    Position positionFrom = Position.fromString(positionString);
                    labeledBoardPanel.placePieceAt(pieceString, positionFrom);
                } else if (end - start > 3){
                    String positionString = longText.getText().substring(start + 1, end - 1);
                    Position positionFrom = Position.fromString(positionString);
                    Direction direction = Direction.fromNotation(longText.getText().charAt(end - 1));
                    assert positionFrom != null;
                    assert direction != null;
                    Position positionTo = new Position(positionFrom.getRow() + direction.getDRow(), positionFrom.getColumn() + direction.getDColumn());
                    if (positionFrom.equals(positionTo)){
                        labeledBoardPanel.removePieceAt(positionFrom);
                    } else {
                        Move move = new Move(positionFrom, positionTo);
                        labeledBoardPanel.movePiece(move);
                    }
                    System.out.println("Position: " + positionFrom + ", Direction: " + direction);
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else if (!nextMove) {
            currentIndex = -1;
        }
    }

}

