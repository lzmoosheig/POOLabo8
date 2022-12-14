package engine.piece;

import chess.PieceType;
import chess.PlayerColor;

public class King extends Piece {
    public King(PlayerColor color) {
        super(color, PieceType.KING);
    }
}
