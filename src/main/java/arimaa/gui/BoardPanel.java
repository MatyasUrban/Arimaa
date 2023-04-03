package arimaa.gui;

import arimaa.core.Board;
import arimaa.core.Move;
import arimaa.core.Piece;
import arimaa.core.StepMove;
import arimaa.utils.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

class BoardPanel extends JPanel {

    private Board board = new Board();
    private final int gridSize = board.getBOARD_SIZE();
    private final int tileSize = 70;

    private JPanel[][] squares = new JPanel[gridSize][gridSize];

    private static final String[][] defaultBoard = {
            {"r", "r", "r", "r", "r", "r", "r", "r"},
            {"e", "m", "h", "h", "d", "d", "c", "c"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"E", "M", "H", "H", "D", "D", "C", "C"},
            {"R", "R", "R", "R", "R", "R", "R", "R"}
    };


    private String[][] instanceBoard = new String[8][8];



    public BoardPanel() {
        setLayout(new GridLayout(gridSize, gridSize));
        for (int i = 0; i < gridSize * gridSize; i++) {
            JPanel square = new JPanel(new BorderLayout());
            square.setPreferredSize(new Dimension(tileSize, tileSize));
            int row = i / gridSize;
            int col = i % gridSize;
            squares[row][col] = square;

            Color squareColor = Color.LIGHT_GRAY;
            if (i == 18 || i == 21 || i == 42 || i == 45) {
                squareColor = Color.DARK_GRAY;
            }

            square.setBackground(squareColor);
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(square);
        }
        updateEntireInstanceBoard(defaultBoard);
        fillBoardWithPieces(instanceBoard);
        int boardWidth = gridSize * tileSize; // Calculate the total width based on the tile size
        int boardHeight = gridSize * tileSize; // Calculate the total height based on the tile size
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // Set the preferred size of the Arimaa board panel
    }





    private void fillBoardWithPieces(String[][] pieces2Darray) {
        for (int i = 0; i < gridSize * gridSize; i++) {
            int row = i / gridSize;
            int col = i % gridSize;
            JPanel square = squares[row][col];
            String pieceCode = pieces2Darray[i / gridSize][i % gridSize];
            fillSquareWithPiece(square, pieceCode);
        }
    }

    private void fillSquareWithPiece(JPanel square, String piece){
        square.removeAll(); // Remove previous components in the square
        if (!piece.isEmpty()) {
            String pieceName = switch (piece) {
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
                System.err.println("Failed to load image resource: piece-icons/" + piece + ".png");
            }
        }
        square.revalidate();
        square.repaint();
    }

    private void fillSquaresWithBoard(){
        for (int i = 0; i < gridSize * gridSize; i++) {
            int row = i / gridSize;
            int col = i % gridSize;
            Position position = new Position(row, col);
            Piece piece = board.getPieceAt(position);
            JPanel square = squares[row][col];
            fillPositionWithPiece(piece, square);
        }
    }

    private void fillPositionWithPiece(Piece piece, JPanel square){
        square.removeAll();
        if (piece != null){
            String pieceName = piece.getType().getName();
            char ownerChar = piece.getOwner().getColor().getBigChar();
            String photoName = pieceName + "-" + ownerChar;
            ImageIcon pieceIcon = loadImageIcon("piece-icons/" + photoName + ".png");
            if (pieceIcon != null) {
                JLabel pieceLabel = new JLabel(pieceIcon);
                square.add(pieceLabel);
            } else {
                System.err.println("Failed to load image resource: piece-icons/" + piece + ".png");
            }
        }
        square.revalidate();
        square.repaint();
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

    public void updateEntireInstanceBoard(String[][] piece2Darray){
        instanceBoard = piece2Darray;
    }

    public void updatePieceInstanceBoard(String piece, int row, int col){
        instanceBoard[row][col] = piece;
    }

    public void placePieceAt(Piece piece, Position position) {
        board.placePiece(piece, position);
        fillSquaresWithBoard();
    }


    public void removePieceAt(Position position){
        board.removePieceAt(position);
        fillSquaresWithBoard();
    }

    public void movePiece(StepMove stepMove){
        board.makeMove(stepMove);
        fillSquaresWithBoard();
    }

    private void resetBoardColors() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                JPanel square = squares[i][j];
                Color squareColor = Color.LIGHT_GRAY;
                if (i == 2 && j == 2 || i == 2 && j == 5 || i == 5 && j == 2 || i == 5 && j == 5) {
                    squareColor = Color.DARK_GRAY;
                }
                square.setBackground(squareColor);
                square.revalidate();
                square.repaint();
            }
        }
    }


    public void resetBoardToDefault() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Position position = new Position(i, j);
                String pieceString = BoardPanel.defaultBoard[i][j];
                Piece piece = Piece.fromNotation(pieceString);
                board.placePiece(piece, position);
            }
        }
        fillSquaresWithBoard();
    }

    public void emptyTheBoard(){
        board.emptyBoard();
        fillSquaresWithBoard();
    }



}
