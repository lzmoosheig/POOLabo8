package engine.board;

import chess.PlayerColor;
import engine.piece.*;
import java.util.HashMap;

public class Board {
    /**
     * Constant of board Size
     */
    public final static byte SIZE = 8;
    private HashMap<Position, Piece> board = new HashMap<>();


    /**
     * Add an initialized piece on board
     *
     * @param piece The piece to add
     * @param left  If the piece is on the left side of the board
     * @throws RuntimeException If the King is initialized with the 'left' parameter on true
     */
    public void add(Piece piece, boolean left) {
        //Permet de définir la position initiale d'une pièce
        Position position = new Position((left ? piece.getType().ordinal() - 1 : 7 - (piece.getType().ordinal() - 1)),
                piece.getColor().ordinal() * 7);
        add(piece, position);
    }

    public void add(Piece piece, Position position) {
        board.put(position, piece);
    }

    public HashMap<Position, Piece> getPieces() {
        //TODO Copie profonde de board à faire
        return board;
    }

    public void initialize() {
        initialize(PlayerColor.BLACK);
        initialize(PlayerColor.WHITE);
    }

    private void initialize(PlayerColor color) {
        for (int i = 0; i < Board.SIZE; ++i) {
            add(new Pawn(color), new Position(i, 1 + color.ordinal() * 5));
        }
        add(new Rook(color), true);
        add(new Rook(color), false);
        add(new Knight(color), true);
        add(new Knight(color), false);
        add(new Bishop(color), true);
        add(new Bishop(color), false);
        add(new Queen(color), true);
        add(new King(color), true);
    }


}
