import arimaa.core.Board;
import arimaa.core.Player;
import arimaa.core.StepMove;
import arimaa.utils.Direction;
import arimaa.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * The ArimaaRulesTest class evaluates and ensures that the model respects most of the Arimaa game rules.
 */
public class ArimaaRulesTest {

    /**
     * Board used for testing
     */
    private Board board;
    /**
     * First golden player
     */
    private Player player1;
    /**
     * Second silver player
     */
    private Player player2;

    /**
     * The testSetUp Method initializes testing environment
     */
    @BeforeEach
    public void testSetUp(){
        board = new Board();
        player1 = new Player(1, false);
        player2 = new Player(2, false);
    }

    /**
     * TEST1: MOVE DIRECTIONS | This test evaluates that pieces get the correct directions to work with
     */
    @Test
    public void moveDirectionTest(){
        String[][] testBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "r", "", "c", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "R", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        // Any piece expect of rabbit can move in all 4 directions
        HashSet<Direction> expectedDirections = new HashSet<>();
        expectedDirections.add(Direction.NORTH);
        expectedDirections.add(Direction.WEST);
        expectedDirections.add(Direction.SOUTH);
        expectedDirections.add(Direction.EAST);
        Position silverCatPosition = new Position(3, 3);
        Assertions.assertEquals(expectedDirections, new HashSet<>(board.getPieceAt(silverCatPosition).getPossibleDirections()));
        // Silver rabbit cannot move north
        expectedDirections.remove(Direction.NORTH);
        Position silverRabbitPosition = new Position(3, 1);
        Assertions.assertEquals(expectedDirections, new HashSet<>(board.getPieceAt(silverRabbitPosition).getPossibleDirections()));
        // Golden rabbit cannot move south
        expectedDirections.add(Direction.NORTH);
        expectedDirections.remove(Direction.SOUTH);
        Position goldenRabbitPosition = new Position(5, 3);
        Assertions.assertEquals(expectedDirections, new HashSet<>(board.getPieceAt(goldenRabbitPosition).getPossibleDirections()));
    }

    /**
     * TEST2: FREEZING | This test verifies that freezing respects Arimaa rules
     */
    @Test
    public void freezingTest(){
        // Silver cat is frozen when adjacent to golden dog
        String[][] testBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "c", "D", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        Position silverCatPosition = new Position(4, 3);
        Assertions.assertTrue(board.isPositionFrozen(silverCatPosition));
        // It is however not frozen, when there is a friendly piece nearby
        testBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "r", "c", "D", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        Assertions.assertFalse(board.isPositionFrozen(silverCatPosition));
        // Also same or weaker piece should not freeze the position
        testBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "c", "C", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        Assertions.assertFalse(board.isPositionFrozen(silverCatPosition));
    }

    /**
     * TEST3: WINNING | This test evaluates the 3 main winning conditions
     */
    @Test
    public void winningTest(){
        // Golden player won, as their rabbit reached the goal
        String[][] testBoard = new String[][]{
                {"R", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        Assertions.assertTrue(board.hasPlayerWon(player1, player2));
        // Golden player won, as the silver player has no rabbits left
        testBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"e", "m", "h", "h", "d", "d", "c", "c"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"E", "M", "H", "H", "D", "D", "C", "C"},
                {"R", "R", "R", "R", "R", "R", "R", "R"}
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        Assertions.assertTrue(board.hasPlayerWon(player1, player2));
        // Golden player won, as all opponent's pieces are frozen
        testBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"m", "h", "d", "d", "c", "c", "", ""},
                {"E", "M", "H", "H", "D", "D", "C", "C"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""}
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        Assertions.assertTrue(board.hasPlayerWon(player1, player2));
    }

    /**
     * TEST4: STEP MOVE | This test evaluates that only viable pieces are selected for step move
     */
    @Test
    public void stepMoveTest(){
        // From golden player's pieces, only those in second row from the base can step move
        String[][] testBoard = new String[][]{
                {"r", "r", "r", "r", "r", "r", "r", "r"},
                {"e", "m", "h", "h", "d", "d", "c", "c"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"E", "M", "H", "H", "D", "D", "C", "C"},
                {"R", "R", "R", "R", "R", "R", "R", "R"}
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        HashSet<Position> expectedPositions = new HashSet<>();
        for (int j = 7; j >= 0; j--){
            expectedPositions.add(new Position(6,j));
        }
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPlayersPiecesWhichCanStepMove(player1)));
    }

    /**
     * TEST5: PULL MOVE | This test evalueates that only viable pieces are selected for pull move
     */
    @Test
    public void pullMoveTest(){
        String[][] testBoard = new String[][]{
                {"r", "r", "r", "r", "r", "r", "r", "r"},
                {"e", "m", "", "h", "", "d", "c", "c"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"E", "h", "d", "H", "", "", "", ""},
                {"", "M", "H", "", "D", "D", "C", "C"},
                {"R", "R", "R", "R", "R", "R", "R", "R"}
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        HashSet<Position> expectedPositions = new HashSet<>();
        Position silverHorsePosition = new Position(5, 1);
        Position silverDogPosition = new Position(5, 2);
        expectedPositions.add(silverHorsePosition);
        expectedPositions.add(silverDogPosition);
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfEnemyPiecesWhichCanBePulled(player1, player2)));
        expectedPositions.clear();
        expectedPositions.add(new Position(5, 0));
        expectedPositions.add(new Position(6, 1));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePullingPieces(silverHorsePosition)));
        expectedPositions.clear();
        expectedPositions.add(new Position(5,3));
        expectedPositions.add(new Position(6,2));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePullingPieces(silverDogPosition)));
    }

    /**
     * TEST6: PUSH MOVE | This test evaluates that only viable pieces are selected for push move
     */
    @Test
    public void pushMoveTest(){
        String[][] testBoard = new String[][]{
                {"r", "r", "r", "r", "r", "r", "r", "r"},
                {"e", "m", "", "h", "", "d", "c", "c"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"E", "h", "d", "H", "", "", "", ""},
                {"", "M", "H", "", "D", "D", "C", "C"},
                {"R", "R", "R", "R", "R", "R", "R", "R"}
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        HashSet<Position> expectedPositions = new HashSet<>();
        Position silverHorsePosition = new Position(5, 1);
        Position silverDogPosition = new Position(5, 2);
        expectedPositions.add(silverHorsePosition);
        expectedPositions.add(silverDogPosition);
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfEnemyPiecesThatCanBePushed(player1, player2)));
        expectedPositions.clear();
        expectedPositions.add(new Position(5, 0));
        expectedPositions.add(new Position(6, 1));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePushingPieces(silverHorsePosition)));
        expectedPositions.clear();
        expectedPositions.add(new Position(5,3));
        expectedPositions.add(new Position(6,2));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePushingPieces(silverDogPosition)));
    }
}
