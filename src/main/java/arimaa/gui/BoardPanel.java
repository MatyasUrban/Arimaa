package arimaa.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

class BoardPanel extends JPanel {
    private final int gridSize = 8;
    private final int tileSize = 70;

    private JPanel[][] squares = new JPanel[gridSize][gridSize];


    public BoardPanel() {
        createBoard();
        int boardWidth = gridSize * tileSize; // Calculate the total width based on the tile size
        int boardHeight = gridSize * tileSize; // Calculate the total height based on the tile size
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // Set the preferred size of the Arimaa board panel
    }

    private void createBoard() {
        setLayout(new GridLayout(gridSize, gridSize));

        MouseListener squareClickListener = new MouseAdapter() {
            private JPanel previousSquare = null;
            private JLabel selectedSquare = null;


            private void resetBoardColors() {
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        Color squareColor = Color.LIGHT_GRAY;
                        if (i == 2 && j == 2 || i == 2 && j == 5 || i == 5 && j == 2 || i == 5 && j == 5) {
                            squareColor = Color.DARK_GRAY;
                        }
                        squares[i][j].setBackground(squareColor);
                    }
                }
            }
        };

        for (int i = 0; i < gridSize * gridSize; i++) {
            JPanel square = new JPanel(new BorderLayout());
            square.setPreferredSize(new Dimension(tileSize, tileSize));
            int row = i / gridSize;
            int col = i % gridSize;
            squares[row][col] = square;

            Color squareColor = Color.LIGHT_GRAY;

            // Set trap squares to dark grey
            if (i == 18 || i == 21 || i == 42 || i == 45) {
                squareColor = Color.DARK_GRAY;
            }

            square.setBackground(squareColor);
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            square.addMouseListener(squareClickListener);
            add(square);
            addInitialPieces(square, i);
        }
    }




    private void addInitialPieces(JPanel square, int position) {
        String[][] initialBoard = {
                {"r", "r", "r", "r", "r", "r", "r", "r"},
                {"e", "m", "h", "h", "d", "d", "c", "c"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"E", "M", "H", "H", "D", "D", "C", "C"},
                {"R", "R", "R", "R", "R", "R", "R", "R"}
        };

        String pieceCode = initialBoard[position / gridSize][position % gridSize];

        if (!pieceCode.isEmpty()) {
            String pieceName = switch (pieceCode) {
                case "R" -> "rabbit-G";
                case "r" -> "rabbit-S";
                case "C" -> "cat-G";
                case "c" -> "cat-S";
                case "D" -> "dog-G";
                case "d" -> "dog-S";
                case "H" -> "horse-G";
                case "h" -> "horse-S";
                case "M" -> "camel-G";
                case "m" -> "camel-S";
                case "E" -> "elephant-G";
                case "e" -> "elephant-S";
                default -> "";
            };
            ImageIcon pieceIcon = loadImageIcon("piece-icons/" + pieceName + ".png");
            if (pieceIcon != null) {
                JLabel pieceLabel = new JLabel(pieceIcon);
                square.add(pieceLabel);
            } else {
                System.err.println("Failed to load image resource: piece-icons/" + pieceCode + ".png");
            }

        }
    }

    private ImageIcon loadImageIcon(String path) {
        ImageIcon icon = null;

        try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (imageStream != null) {
                BufferedImage image = ImageIO.read(imageStream);
                icon = new ImageIcon(image);
            } else {
                System.err.println("Failed to load image resource: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error reading image resource: " + path);
            e.printStackTrace();
        }

        return icon;
    }

}
