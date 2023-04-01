package arimaa.gui;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private final String rules = """
            # COLORS
            
            Player 1 has yellow pieces.
            Player 2 has blue pieces.
            
            # PIECES
            
            Ordered from the strongest:
            Elephant (1x)
            Camel (1x)
            Horse (2x)
            Dog (2x)
            Cat (2x)
            Rabbit (8x)""";
    public WelcomePanel() {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel(" Welcome to Arimaa! ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        JTextArea longText = new JTextArea(rules);
        longText.setLineWrap(true);
        longText.setWrapStyleWord(true);
        longText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(longText);
        add(scrollPane, BorderLayout.CENTER);
    }

}

