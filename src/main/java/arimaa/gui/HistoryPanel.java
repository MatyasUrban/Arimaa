package arimaa.gui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HistoryPanel extends JPanel {

    private JButton previousButton;
    private JButton nextButton;
    private JTextArea gameNotation;

    public HistoryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");

        gameNotation = new JTextArea();
        gameNotation.setEditable(false);
        gameNotation.setFont(new Font("Monospaced", Font.PLAIN, 12));
        gameNotation.setLineWrap(true);
        gameNotation.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(gameNotation);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2));
        buttonsPanel.add(previousButton);
        buttonsPanel.add(nextButton);

        add(buttonsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}

