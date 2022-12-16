package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;

public class Pawn extends Piece {
    public Pawn(PlayerColor color) {
        super(color, PieceType.PAWN);
    }
}
