package engine.piece;


import chess.PieceType;
import chess.PlayerColor;
import engine.board.Position;

public class King extends Piece{
    public King(PlayerColor color){
        this(color,new Position(PieceType.KING.ordinal(), color.ordinal() * 8 ));
    }

    public King(PlayerColor color, Position position){
        super(color, PieceType.KING, position);
    }
}
