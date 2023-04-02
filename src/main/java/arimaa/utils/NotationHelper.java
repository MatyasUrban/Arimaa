package arimaa.utils;

import arimaa.core.Move;
import arimaa.core.Piece;

public class NotationHelper {

    public static Position notationToPosition(String positionNotation){
//        char rowChar = positionNotation.charAt(1);
//        char columnChar = positionNotation.charAt(0);
//        int rowInt = switch (rowChar){
//            case '1' -> 7;
//            case '2' -> 6;
//            case '3' -> 5;
//            case '4' -> 4;
//            case '5' -> 3;
//            case '6' -> 2;
//            case '7' -> 1;
//            case '8' -> 0;
//            default -> 0;
//        };
//        int columnInt = switch (columnChar){
//            case 'a' -> 0;
//            case 'b' -> 1;
//            case 'c' -> 2;
//            case 'd' -> 3;
//            case 'e' -> 4;
//            case 'f' -> 5;
//            case 'g' -> 6;
//            case 'h' -> 7;
//            default -> 0;
//        };
        return Position.fromString(positionNotation);
    }

    public static String PositionToNotation(Position position){
//        int rowInt = position.getRow();
//        int columnInt = position.getColumn();
//        char rowChar = switch (rowInt){
//            case 0 -> '8';
//            case 1 -> '7';
//            case 2 -> '6';
//            case 3 -> '5';
//            case 4 -> '4';
//            case 5 -> '3';
//            case 6 -> '2';
//            case 7 -> '1';
//            default -> '1';
//        };
//        char columnChar = switch (columnInt) {
//            case 0 -> 'a';
//            case 1 -> 'b';
//            case 2 -> 'c';
//            case 3 -> 'd';
//            case 4 -> 'e';
//            case 5 -> 'f';
//            case 6 -> 'g';
//            case 7 -> 'h';
//            default -> 'a';
//        };
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
        return new Move(fromPosition, toPosition);
    }
}
