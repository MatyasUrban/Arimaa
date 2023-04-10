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
import java.util.stream.Collectors;

/**
 * The BoardPanel class defines outlook and behavior of visual board grid.
 */
class BoardPanel extends JPanel {

    /**
     * Static constant: Board size (useful for generating UI)
     */
    private static final int GRID_SIZE = Board.BOARD_SIZE;



    private Game game;
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
    public BoardPanel(Game game) {
        this.game = game;
        this.board = this.game.getBoard();
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

    public void setGame(Game game){
        this.game = game;
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
            Piece piece = game.getBoard().getPieceAt(position);
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
        game.getBoard().placePiece(piece, position);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Method to remove a piece from the board (both visual and logical)
     *
     * @param position Position of the removal.
     */
    public void removePieceAt(Position position){
        game.getBoard().removePieceAt(position);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Method to move a piece on the board (both visually and logically.
     *
     * @param stepMove StepMove object with specified starting and destination positions.
     */
    public void stepMovePiece(StepMove stepMove){
        game.getBoard().makeMove(stepMove);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    public void movePiece(StepMove stepMove){
        game.getBoard().makeMove(stepMove);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    public void pullMovePieces(PullMove pullMove){
        game.getBoard().makeMove(pullMove);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    public void pushMovePieces(PushMove pushMove){
        game.getBoard().makeMove(pushMove);
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
        game.getBoard().switchPieces(position1, position2);
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

    public ArrayList<Position> getPositionsOfSquaresWithColor(Color color){
        ArrayList<Position> positionArrayList = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Color squareColor = squares[i][j].getBackground();
                if (squareColor == color){
                    positionArrayList.add(new Position(i, j));
                }
            }
        }
        return positionArrayList;
    }

    /**
     * Method to empty the logical board and the grid (remove piece icons and restore colors)
     */
    public void emptyTheBoard(){
        game.getBoard().emptyBoard();
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
            Position position = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel square = (JPanel) e.getSource();
                Position clickedPosition = getPositionFromSquare(square);
                if (clickedOnWrongSquare(square)) {
                    handleModeReset();
                    return;
                }
                ArrayList<Position> positionArrayList = getPositionsOfSquaresWithColor(currentMode.getColor());
                switch (currentMode) {
                    case SWITCH:
                        handleSwitchMode(clickedPosition, positionArrayList);
                        break;
                    case STEP:
                        handleStepMode(clickedPosition, positionArrayList);
                        break;
                    case PULL:
                        handlePullMode(clickedPosition, positionArrayList);
                        break;
                    case PUSH:
                        handlePushMode(clickedPosition, positionArrayList);
                        break;
                    default:
                        break;
                }
            }
        };
    }


    private void handleSwitchMode(Position squarePosition, ArrayList<Position> selectedPositions){
        if (selectedPositions.size() == 0) {
            fillSquareWithColor(squarePosition, currentMode.getColor());
        } else if (selectedPositions.size() == 1){
            switchPieces(selectedPositions.get(0), squarePosition);
            handleModeReset();
        }
    }

    private boolean clickedOnWrongSquare(JPanel square){
        Color color = square.getBackground();
        return color == Color.LIGHT_GRAY || color == Color.DARK_GRAY || color == currentMode.getColor();
    }

    private void handleStepMode(Position squarePosition, ArrayList<Position> selectedPositions){
        if (selectedPositions.size() == 0){
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Position>) game.getBoard().getValidStepMovesByItselfForPosition(squarePosition).stream().map(StepMove::getTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 1){
            stepMovePiece(new StepMove(selectedPositions.get(0), squarePosition));
            checkTraps();
            handleModeReset();
        }
    }

    private void handlePullMode(Position squarePosition, ArrayList<Position> selectedPositions){
        if (selectedPositions.size() == 0){
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor(game.getBoard().getPositionsOfPossiblePullingPieces(squarePosition), Color.WHITE);
        } else if (selectedPositions.size() == 1){
            resetSquaresColors();
            selectedPositions.add(squarePosition);
            fillSquaresWithColor(selectedPositions, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Position>) game.getBoard().getValidPullMovesForPullerAndPulled(squarePosition, selectedPositions.get(0)).stream().map(PullMove::getTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 2){
            Position pulledPiecePosition = null;
            Position pullingPiecePosition = null;
            for (Position position : selectedPositions){
                if (game.getBoard().getPieceAt(position).owner() == game.getCurrentPlayer()){
                    pullingPiecePosition = position;
                } else {
                    pulledPiecePosition = position;
                }
            }
            pullMovePieces(new PullMove(pullingPiecePosition, squarePosition, pulledPiecePosition, pullingPiecePosition));
            checkTraps();
            handleModeReset();
        }
    }

    private void handlePushMode(Position squarePosition, ArrayList<Position> selectedPositions){
        if (selectedPositions.size() == 0){
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor(game.getBoard().getPositionsOfPossiblePushingPieces(squarePosition), Color.WHITE);
        } else if (selectedPositions.size() == 1){
            resetSquaresColors();
            selectedPositions.add(squarePosition);
            fillSquaresWithColor(selectedPositions, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Position>) game.getBoard().getValidPushMovesForPusherAndPushed(squarePosition, selectedPositions.get(0)).stream().map(PushMove::getPushedPieceTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 2){
            Position pushedPiecePosition = null;
            Position pushingPiecePosition = null;
            for (Position position : selectedPositions){
                if (game.getBoard().getPieceAt(position).owner() == game.getCurrentPlayer()){
                    pushingPiecePosition = position;
                } else {
                    pushedPiecePosition = position;
                }
            }
            pushMovePieces(new PushMove(pushingPiecePosition, pushedPiecePosition, pushedPiecePosition, squarePosition));
            checkTraps();
            handleModeReset();
        }
    }

    public void handleModeReset(){
        resetSquaresColors();
        switch (currentMode){
            case SWITCH -> fillSquaresWithColor(game.getBoard().getPositionsOfPlayersPieces(game.getCurrentPlayer()), Color.WHITE);
            case STEP -> fillSquaresWithColor(game.getBoard().getPositionsOfPlayersPiecesWhichCanStepMove(game.getCurrentPlayer()), Color.WHITE);
            case PULL -> fillSquaresWithColor(game.getBoard().getPositionsOfEnemyPiecesWhichCanBePulled(game.getCurrentPlayer(), game.getEnemyPlayer()), Color.WHITE);
            case PUSH -> fillSquaresWithColor(game.getBoard().getPositionsOfEnemyPiecesThatCanBePushed(game.getCurrentPlayer(), game.getEnemyPlayer()), Color.WHITE);
        }
    }

    private void checkTraps(){
        for(Position position : Position.TRAP_POSITIONS){
            if (game.getBoard().getPieceAt(position) != null && !game.getBoard().isFriendlyPieceNearby(position)){
                removePieceAt(position);
            }
        }
    }








}
