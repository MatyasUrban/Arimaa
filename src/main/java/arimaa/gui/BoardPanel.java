package arimaa.gui;

import arimaa.core.*;
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
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The BoardPanel class defines outlook and behavior of visual board grid.
 */
class BoardPanel extends JPanel {

    /**
     * Static constant: Board size (useful for generating UI)
     */
    private static final int GRID_SIZE = Board.BOARD_SIZE;
    /**
     * Instance variable: Game whose state this board panel will visualize.
     */
    private Game game;

    /**
     * Instance variable: Panel of interactive visual board positions.
     */
    private final JPanel[][] squares;

    /**
     * Instance variable: Mode of interactivity with the board panel.
     */
    private BoardMode currentMode = BoardMode.NONE;


    /**
     * Constructs a new visual BoardPanel object representing the logical board.
     *
     * @param game Game whose state is to be visualized.
     */
    public BoardPanel(Game game) {
        this.game = game;
        squares = new JPanel[GRID_SIZE][GRID_SIZE];
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        // Create the grid with mouse listeners attached to each square.
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
     * Sets the current game and updates the visual board.
     *
     * @param game Game object.
     */
    public void setGame(Game game){
        this.game = game;
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Sets the board mode of interactivity with the visual board panel.
     *
     * @param boardMode Enum board mode.
     */
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

    /*
    SIMPLE MOVING AND REMOVING FOR STEPS HISTORY FACILITATED BY HISTORY PANEL
     */

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
     * Method to move a piece on the board (both visual and logical)
     *
     * @param stepMove StepMove object.
     */
    public void movePiece(StepMove stepMove){
        game.getBoard().makeMove(stepMove);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /*
    COMPLEX MOVES FOR GAME INTERACTIVITY
     */

    /**
     * Method to move a piece on the board (both visually and logically).
     *
     * @param stepMove StepMove object with specified starting and destination positions.
     */
    public void stepMovePiece(StepMove stepMove){
        String moveString = game.getBoard().getPieceAt(stepMove.getFrom()).toString() + stepMove + " ";
        game.appendStepsBuilder(moveString);
        game.getBoard().makeMove(stepMove);
        fillSquaresWithBoard();
        resetSquaresColors();
    }

    /**
     * Method to facilitate a pull move by destructuring it into step moves and checking traps in between.
     *
     * @param pullMove PullMove object.
     */
    public void pullMovePieces(PullMove pullMove){
        StepMove pullingPieceMove = new StepMove(pullMove.getFrom(), pullMove.getTo());
        stepMovePiece(pullingPieceMove);
        checkTraps();
        StepMove pulledPieceMove = new StepMove(pullMove.getPulledPieceFrom(), pullMove.getPulledPieceTo());
        stepMovePiece(pulledPieceMove);
    }

    /**
     * Method to facilitate a push move by destructuring it into step moves and checking traps in between.
     *
     * @param pushMove PullMove object.
     */
    public void pushMovePieces(PushMove pushMove){
        StepMove pushedPieceMove = new StepMove(pushMove.getPushedPieceFrom(), pushMove.getPushedPieceTo());
        stepMovePiece(pushedPieceMove);
        checkTraps();
        StepMove pushingPieceMove = new StepMove(pushMove.getFrom(), pushMove.getTo());
        stepMovePiece(pushingPieceMove);
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
     * Method to fill a specific grid square with a given color.
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
     * Method to fill specific positions with a given color.
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
     * Method to get coordinates of squares with a given color.
     *
     * @param color Color of the search.
     * @return ArrayList of Position objects.
     */
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

    /**
     * Method to create a position object (coordinates) given a board square.
     *
     * @param square Specific square in the grid.
     * @return Position object.
     */
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

    /**
     * Creates mouse adapter and describes behavior on square click depending on the current mode.
     *
     * @return The mouse adapter object.
     */
    private MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {

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
                    case SWITCH -> handleSwitchMode(clickedPosition, positionArrayList);
                    case STEP -> {
                        if (game.getMovesLeftThisTurn() < 1) break;
                        handleStepMode(clickedPosition, positionArrayList);
                    }
                    case PULL -> {
                        if (game.getMovesLeftThisTurn() < 2) break;
                        handlePullMode(clickedPosition, positionArrayList);
                    }
                    case PUSH -> {
                        if (game.getMovesLeftThisTurn() < 2) break;
                        handlePushMode(clickedPosition, positionArrayList);
                    }
                }
            }
        };
    }

    /**
     * Method to determine, whether the clicked square color is wrong (the square is not available for click or has been already selected).
     *
     * @param square The clicked square.
     * @return Boolean value of the check.
     */
    private boolean clickedOnWrongSquare(JPanel square){
        Color color = square.getBackground();
        return color == Color.LIGHT_GRAY || color == Color.DARK_GRAY || color == currentMode.getColor();
    }

    /**
     * Method to handle switch mode (setting up the initial positions of pieces)
     * 1. If no piece already selected, the clicked position will be the first of two chosen squares for switch
     * 2. If one piece already selected, the clicked position will be the second piece, execute switch
     *
     * @param squarePosition Position of the currently clicked square.
     * @param selectedPositions List of already selected (user-clicked) and active positions on the board.
     */
    private void handleSwitchMode(Position squarePosition, ArrayList<Position> selectedPositions){
        if (selectedPositions.size() == 0) {
            fillSquareWithColor(squarePosition, currentMode.getColor());
        } else if (selectedPositions.size() == 1){
            switchPieces(selectedPositions.get(0), squarePosition);
            handleModeReset();
        }
    }



    /**
     * Method to handle step mode (user indicated they want to make a step move)
     * 1. If no piece already selected, the clicked position will be the piece we'd like to move by one place
     * 2. If piece selected, the clicked position will be the destination of the step move
     * Finally: Execute step move, check traps, check winning, decrement moves
     *
     * @param squarePosition Position of the currently clicked square.
     * @param selectedPositions List of already selected (user-clicked) and active positions on the board.
     */
    private void handleStepMode(Position squarePosition, ArrayList<Position> selectedPositions){
        if (selectedPositions.size() == 0){
            resetSquaresColors();
            fillSquareWithColor(squarePosition, currentMode.getColor());
            fillSquaresWithColor((ArrayList<Position>) game.getBoard().getValidStepMovesByItselfForPosition(squarePosition).stream().map(StepMove::getTo).collect(Collectors.toList()), Color.WHITE);
        } else if (selectedPositions.size() == 1){
            stepMovePiece(new StepMove(selectedPositions.get(0), squarePosition));
            checkTraps();
            handleModeReset();
            game.checkWinning();
            game.decrementMovesLeftThisTurnBy(1);
        }
    }

    /**
     * Method to handle the pull mode (user indicated they want to make a pull move)
     * 1. If none already selected, the clicked position will be the piece we'd like to pull, and display pulling pieces options
     * 2. If pulled piece selected, the clicked position will be the pulling piece, and display destination options
     * 3. If pulled and pulling pieces selected, the clicked position will be the chosen direction of the pull
     * Finally: Execute pull move, check traps, check winning, decrement moves
     *
     * @param squarePosition Position of the currently clicked square.
     * @param selectedPositions List of already selected (user-clicked) and active positions on the board.
     */
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
            game.checkWinning();
            game.decrementMovesLeftThisTurnBy(2);
        }
    }

    /**
     * Method to handle the push mode (user indicated they want to make a push move)
     * 1. If none already selected, the clicked position will be the piece we'd like to push, and display pushing pieces options
     * 2. If pushed piece selected, the clicked position will be the pushing piece, and display destination options
     * 3. If pushed and pushing pieces selected, the clicked position will be the chosen direction of the push
     * Finally: Execute push move, check traps, check winning, decrement moves
     *
     * @param squarePosition Position of the currently clicked square.
     * @param selectedPositions List of already selected (user-clicked) and active positions on the board.
     */
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
            game.checkWinning();
            game.decrementMovesLeftThisTurnBy(2);
        }
    }

    /**
     * Method to reset the board colors based on the current mode of interactivity.
     */
    public void handleModeReset(){
        resetSquaresColors();
        switch (currentMode){
            case SWITCH -> fillSquaresWithColor(game.getBoard().getPositionsOfPlayersPieces(game.getCurrentPlayer()), Color.WHITE);
            case STEP -> fillSquaresWithColor(game.getBoard().getPositionsOfPlayersPiecesWhichCanStepMove(game.getCurrentPlayer()), Color.WHITE);
            case PULL -> fillSquaresWithColor(game.getBoard().getPositionsOfEnemyPiecesWhichCanBePulled(game.getCurrentPlayer(), game.getEnemyPlayer()), Color.WHITE);
            case PUSH -> fillSquaresWithColor(game.getBoard().getPositionsOfEnemyPiecesThatCanBePushed(game.getCurrentPlayer(), game.getEnemyPlayer()), Color.WHITE);
        }
    }

    /**
     * Method to remove pieces from traps.
     * stepMovePiece is used for logging (moving onto the same square signifies a removal).
     */
    private void checkTraps(){
        for(Position position : Position.TRAP_POSITIONS){
            if (game.getBoard().getPieceAt(position) != null && !game.getBoard().isFriendlyPieceNearby(position)){
                stepMovePiece(new StepMove(position, position));
                removePieceAt(position);
            }
        }
    }

    /**
     * Method for the computer mode to select random square with valid move and click it.
     */
    public void clickOnRandomWhiteSquare() {
        ArrayList<Position> positions = getPositionsOfSquaresWithColor(Color.WHITE);
        Random random = new Random();
        Position position = positions.get(random.nextInt(positions.size()));
        JPanel square = squares[position.row()][position.column()];
        MouseEvent event = new MouseEvent(square, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 1,1, 1, false);
        for (MouseListener listener : square.getMouseListeners()) {
            listener.mouseClicked(event);
        }
    }









}
