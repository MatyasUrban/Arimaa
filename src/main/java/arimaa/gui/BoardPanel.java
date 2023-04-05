package arimaa.gui;

import arimaa.core.*;
import arimaa.utils.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class BoardPanel extends JPanel {

    private Board board;
    private final int gridSize;

    private final JPanel[][] squares;


    public BoardPanel(Board board) {
        this.board = board;
        gridSize = Board.BOARD_SIZE;
        squares = new JPanel[gridSize][gridSize];
        setLayout(new GridLayout(gridSize, gridSize));
        int tileSize = 70;
        for (int i = 0; i < gridSize * gridSize; i++) {
            JPanel square = new JPanel(new BorderLayout());
            square.setPreferredSize(new Dimension(tileSize, tileSize));
            int row = i / gridSize;
            int col = i % gridSize;
            squares[row][col] = square;
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(square);
        }
        resetSquaresColors();
        int boardWidth = gridSize * tileSize; // Calculate the total width based on the tile size
        int boardHeight = gridSize * tileSize; // Calculate the total height based on the tile size
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // Set the preferred size of the Arimaa board panel
        fillSquaresWithBoard();

    }

    public void setBoard(Board board){
        this.board = board;
        fillSquaresWithBoard();
        resetSquaresColors();
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
            String pieceName = piece.type().getName();
            char ownerChar = piece.owner().getColor().getBigChar();
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


    public void placePieceAt(Piece piece, Position position) {
        board.placePiece(piece, position);
        fillSquaresWithBoard();
        resetSquaresColors();
    }


    public void removePieceAt(Position position){
        board.removePieceAt(position);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    public void switchPieces(Position position1, Position position2){
        board.switchPieces(position1, position2);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    public void movePiece(StepMove stepMove){
        board.makeMove(stepMove);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    private void resetSquaresColors() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Position position = new Position(i, j);
                JPanel square = squares[i][j];
                Color squareColor = Color.LIGHT_GRAY;
                if (i == 2 && j == 2 || i == 2 && j == 5 || i == 5 && j == 2 || i == 5 && j == 5) {
                    squareColor = Color.DARK_GRAY;
                }
                fillSquareWithColor(position, squareColor);
            }
        }
    }

    public void fillSquareWithColor(Position position, Color color){
        JPanel square = squares[position.row()][position.column()];
        square.setBackground(color);
        square.revalidate();
        square.repaint();
    }

    public void fillSquaresWithColor(ArrayList<Position> positions, Color color){
        for(Position position : positions){
            fillSquareWithColor(position, color);
        }
    }

    public void emptyTheBoard(){
        board.emptyBoard();
        fillSquaresWithBoard();
    }



}
