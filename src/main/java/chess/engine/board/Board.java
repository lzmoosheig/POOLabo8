package chess.engine.board;

import chess.PlayerColor;
import chess.engine.piece.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

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
    private void add(Piece piece, boolean left) {
        //Permet de définir la position initiale d'une pièce
        Position position = new Position((left ? piece.getType().ordinal() - 1 : 7 - (piece.getType().ordinal() - 1)),
                piece.getColor().ordinal() * 7);
        add(position, piece);
    }

    public void addFromLeft(Piece piece){
        add(piece, true);
    }

    public void addFromRight(Piece piece){
        add(piece, false);
    }

    public void add(Position position, Piece piece) {
        board.put(position, piece);
    }

    public void move(Entry<Position, Piece> from, Entry<Position, Piece> to){
        board.remove(from.getKey());
        add(to.getKey(), from.getValue());
    }

    public HashMap<Position, Piece> getBoard() {
        //TODO Copie profonde de board à faire
        return board;
    }

    public Entry<Position, Piece> getEntry(Position position){
        if (position == null){
            throw new RuntimeException("Position can't be null");
        }
        return new AbstractMap.SimpleEntry<>(position, board.get(position));
    }

    public void initialize() {
        initialize(PlayerColor.BLACK);
        initialize(PlayerColor.WHITE);
    }

    private void initialize(PlayerColor color) {
        for (int i = 0; i < Board.SIZE; ++i) {
            add(new Position(i, 1 + color.ordinal() * 5), new Pawn(color));
        }
        addFromLeft(new Rook(color));
        addFromRight(new Rook(color));
        addFromLeft(new Knight(color));
        addFromRight(new Knight(color));
        addFromLeft(new Bishop(color));
        addFromRight(new Bishop(color));
        addFromLeft(new Queen(color));
        addFromLeft(new King(color));
    }


}
