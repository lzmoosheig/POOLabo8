package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Position;

public class Pawn extends Piece{
    public Pawn(PlayerColor color){
    this(color,new Position(PieceType.PAWN.ordinal(), color.ordinal() * 8 ));
    }

    public Pawn(PlayerColor color, Position position){
        super(color, PieceType.PAWN, position);
    }
}
