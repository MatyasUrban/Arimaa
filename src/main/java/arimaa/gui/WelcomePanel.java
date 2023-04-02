package arimaa.gui;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private final String rules = """
            # COLORS
 
            - Player 1 (g) has yellow pieces
            - Player 2 (s) has blue pieces
            
            # PIECES
            
            - Ordered from the strongest:
            - Elephant (1x) e
            - Camel (1x) m
            - Horse (2x) h
            - Dog (2x) d
            - Cat (2x) c
            - Rabbit (8x) b
            
            #MOVEMENT
            
            - 4 single moves per turn
            - You cannot move your rabbits back
            - All other pieces can move anywhere
            - Piece is frozen when adjacent to stronger enemy piece with no friendly piece around
            - Piece can stay on or go over a trap if a friendly piece is standing next to the trap
            - Any piece can be pushed or pulled by a stronger enemy piece in any direction onto empty square
            
            #WINNING
             
            - Enemy resigned
            - Rabbit reached the goal
            - Enemy lost all rabbits
            - Enemy cannot move
            
            """;
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
