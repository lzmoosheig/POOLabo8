package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Position;

public class Knight extends Piece{
    public Knight(PlayerColor color){
        this(color,new Position(PieceType.KNIGHT.ordinal(), color.ordinal() * 8 ));
    }

    public Knight(PlayerColor color, Position position){
        super(color, PieceType.KNIGHT, position);
    }
}
