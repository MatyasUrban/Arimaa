package arimaa.utils;

import arimaa.core.Move;
import arimaa.core.Piece;
import arimaa.core.StepMove;

public class NotationHelper {

    public static Position notationToPosition(String positionNotation){
        return Position.fromString(positionNotation);
    }

    public static String PositionToNotation(Position position){
        return position.toString();
    }

    public static Move notationToMove(String moveNotation){
        char piece = moveNotation.charAt(0);
        Position fromPosition = notationToPosition(moveNotation.substring(1, 3));
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getColumn();
        Direction direction = Direction.fromNotation(moveNotation.charAt(3));
        assert direction != null;
        int toRow = fromRow + direction.getDRow();
        int toCol = fromCol + direction.getDColumn();
        Position toPosition = new Position(toRow, toCol);
        return new StepMove(fromPosition, toPosition);
    }
}
