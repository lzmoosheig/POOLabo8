package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Position;

public abstract class Piece {

    /**
     * store the position
     */
    private Position position;

    /**
     * store the piece type
     */
    private PieceType type;

    /**
     * Store the piece color
     */
    private PlayerColor color;

    public Piece(PlayerColor color, PieceType type, Position position) {
        this.color = color;
        this.type = type;
        this.position = position;
    }

    public PieceType getType(){
        return type;
    }
    public PlayerColor getColor() {
        return this.color;
    }

    public Position getPosition(){
        return position;
    }

    /**
     * Set the position of the piece
     *
     * @param position The position to set
     * @throws RuntimeException if Position parameter is null
     */
    public void setPosition(Position position) {
        if (position == null) throw new RuntimeException("Position can't be null");
        this.position = position;
    }

    //public abstract boolean moveIsValide(Position from, Position to);


}
