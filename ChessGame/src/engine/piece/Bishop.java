package engine.piece;


import chess.PieceType;
import chess.PlayerColor;
import engine.board.Position;

public class Bishop extends Piece {
    public Bishop(PlayerColor color){
        this(color,new Position(PieceType.BISHOP.ordinal(), color.ordinal() * 8 ));
    }

    public Bishop(PlayerColor color, Position position){
        super(color, PieceType.BISHOP, position);
    }
}
