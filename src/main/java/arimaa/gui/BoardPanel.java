package arimaa.gui;

import arimaa.core.*;
import arimaa.utils.BoardMode;
import arimaa.utils.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * The BoardPanel class defines outlook and behavior of visual board grid.
 */
class BoardPanel extends JPanel {

    /**
     * Static constant: Board size (useful for generating UI)
     */
    private static final int GRID_SIZE = Board.BOARD_SIZE;

    /**
     * Instance variable: Actual logical game board object with pieces and methods.
     */
    private Board board;

    /**
     * Instance variable: Panel of interactive visual board positions.
     */
    private final JPanel[][] squares;

    private BoardMode currentMode = BoardMode.NONE;


    /**
     * Constructs a visual BoardPanel object representing the logical board.
     *
     * @param board Board object Position[][] filled with Piece objects and nulls.
     */
    public BoardPanel(Board board) {
        this.board = board;
        squares = new JPanel[GRID_SIZE][GRID_SIZE];
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        // Create the grid
        int tileSize = 70;
        MouseAdapter mouseAdapter = createMouseAdapter();
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            JPanel square = new JPanel(new BorderLayout());
            square.setPreferredSize(new Dimension(tileSize, tileSize));
            int row = i / GRID_SIZE;
            int col = i % GRID_SIZE;
            squares[row][col] = square;
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            square.addMouseListener(mouseAdapter);
            add(square);
        }
        // Fill grid with default colors
        resetSquaresColors();
        // Set the size of the grid
        int boardWidth = GRID_SIZE * tileSize;
        int boardHeight = GRID_SIZE * tileSize;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // Fills squares with piece pictures according to the logical board
        fillSquaresWithBoard();
    }

    /**
     * Sets the logical board, which the BoardPanel shall represent, and displays it accordingly.
     *
     * @param board Board object of the logical game board.
     */
    public void setBoard(Board board){
        this.board = board;
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    public void setBoardMode(BoardMode boardMode){
        this.currentMode = boardMode;
    }

    /**
     * Method to traverse the logical board and fill the visual grid with appropriate pictures.
     */
    public void fillSquaresWithBoard(){
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            int row = i / GRID_SIZE;
            int col = i % GRID_SIZE;
            Position position = new Position(row, col);
            Piece piece = board.getPieceAt(position);
            JPanel square = squares[row][col];
            fillPositionWithPiece(piece, square);
        }
    }

    /**
     * Method to fill a specific grid square with picture according to the piece.
     *
     * @param piece Piece object with PieceType (animal) and Owner (color).
     * @param square JPanel object of the square in the grid.
     */
    private void fillPositionWithPiece(Piece piece, JPanel square){
        square.removeAll();
        // Board positions with no piece shall be represented with no picture
        if (piece != null){
            String pieceName = piece.type().getName();
            char ownerChar = piece.owner().getColor().getBigChar();
            String photoName = pieceName + "-" + ownerChar;
            ImageIcon pieceIcon = loadImageIcon("piece-icons/" + photoName + ".png");
            JLabel pieceLabel = new JLabel(pieceIcon);
            square.add(pieceLabel);
        }
        square.revalidate();
        square.repaint();
    }

    /**
     * Method to handle locating and loading image from path.
     * @param path String path to the image.
     * @return ImageIcon object with the actual image of the piece
     */
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

    /**
     * Method to place a piece onto the board (both visual and logical)
     *
     * @param piece Piece to be placed.
     * @param position Position the piece shall be placed at.
     */
    public void placePieceAt(Piece piece, Position position) {
        board.placePiece(piece, position);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Method to remove a piece from the board (both visual and logical)
     *
     * @param position Position of the removal.
     */
    public void removePieceAt(Position position){
        board.removePieceAt(position);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Method to move a piece on the board (both visually and logically.
     *
     * @param stepMove StepMove object with specified starting and destination positions.
     */
    public void movePiece(StepMove stepMove){
        board.makeMove(stepMove);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Method to switch two pieces at selected positions (both visually and logically)
     * Used for initial arrangement of pieces before the game starts.
     *
     * @param position1 Position of the first piece.
     * @param position2 Position of the second piece.
     */
    public void switchPieces(Position position1, Position position2){
        board.switchPieces(position1, position2);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Method to restore the grid squares colors.
     */
    public void resetSquaresColors() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Color squareColor = Color.LIGHT_GRAY;
                if (i == 2 && j == 2 || i == 2 && j == 5 || i == 5 && j == 2 || i == 5 && j == 5) {
                    squareColor = Color.DARK_GRAY;
                }
                fillSquareWithColor(new Position(i, j), squareColor);
            }
        }
    }

    /**
     * Method to fill a specific grid square with given color.
     *
     * @param position Position of the square to be filled.
     * @param color Color of the new filling.
     */
    public void fillSquareWithColor(Position position, Color color){
        JPanel square = squares[position.row()][position.column()];
        square.setBackground(color);
        square.revalidate();
        square.repaint();
    }

    /**
     * Method to fill specific positions with specific color.
     *
     * @param positions List of positions which shall be filled.
     * @param color Color to be used.
     */
    public void fillSquaresWithColor(ArrayList<Position> positions, Color color){
        for(Position position : positions){
            fillSquareWithColor(position, color);
        }
    }

    /**
     * Method to empty the logical board and the grid (remove piece icons and restore colors)
     */
    public void emptyTheBoard(){
        board.emptyBoard();
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    public Position getPositionFromSquare(JPanel square) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (squares[row][col].equals(square)) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    public void addMouseListenerToSquares(MouseListener listener) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                squares[row][col].addMouseListener(listener);
            }
        }
    }

    public JPanel[][] getSquares(){
        return squares;
    }

    public void switchPiecesMode(Game game, Player player, Runnable onFinish) {
        ArrayList<Position> playerPiecePositions = game.getBoard().getPositionsOfPlayersPieces(player);
        fillSquaresWithColor(playerPiecePositions, Color.WHITE);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            Position selectedPosition = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel square = (JPanel) e.getSource();
                Position clickedPosition = getPositionFromSquare(square);
                Color squareColor = square.getBackground();

                if (selectedPosition == null) {
                    if (playerPiecePositions.contains(clickedPosition)) {
                        selectedPosition = clickedPosition;
                        fillSquareWithColor(clickedPosition, Color.RED);
                    }
                } else {
                    if (playerPiecePositions.contains(clickedPosition) && !clickedPosition.equals(selectedPosition)) {
                        switchPieces(selectedPosition, clickedPosition);
                        fillSquaresWithColor(playerPiecePositions, Color.WHITE);
                        selectedPosition = null;
                    } else {
                        fillSquaresWithColor(playerPiecePositions, Color.WHITE);
                        selectedPosition = null;
                    }
                }
            }
        };

        for (Position position : playerPiecePositions) {
            squares[position.row()][position.column()].addMouseListener(mouseAdapter);
        }

        onFinish.run();
    }

    private MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {
            Position position1 = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel square = (JPanel) e.getSource();
                Position clickedPosition = getPositionFromSquare(square);

                switch (currentMode) {
                    case SWITCH:
                        handleSwitchMode(clickedPosition);
                        break;
                    case STEP:
                        // Implement logic for step mode
                        break;
                    case PULL:
                        // Implement logic for pull mode
                        break;
                    case PUSH:
                        // Implement logic for push mode
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void handleSwitchMode(Position clickedPosition){
        // if no green squares, and the clicked one is white, set back to green
        // if one green square, and the clicked one is white, switch, set the board to switch
    }

    private void handleStepMode(Position clickedPosition){
        // if
    }

    private void handlePullMode(Position clickedPosition){}

    private void handlePushMode(Position clickedPosition){}








}
