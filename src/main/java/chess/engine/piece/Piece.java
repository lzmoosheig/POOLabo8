package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;

public abstract class Piece {
    /**
     * store the piece type
     */
    private final PieceType type;

    /**
     * Store the player color
     */
    private final PlayerColor color;

    public Piece(PlayerColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public PieceType getType() {
        return type;
    }

    public PlayerColor getColor() {
        return this.color;
    }


    //public abstract boolean moveIsValide(Position from, Position to);


}
