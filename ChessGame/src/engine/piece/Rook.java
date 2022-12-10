package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Position;

public class Rook extends Piece{
    public Rook(PlayerColor color){
        this(color,new Position(PieceType.ROOK.ordinal(), color.ordinal() * 8 ));
    }

    public Rook(PlayerColor color, Position position){
        super(color, PieceType.ROOK, position);
    }
//    @Override
//    public boolean moveIsValide(Position from, Position to) {
//        return false;
//    }
}
