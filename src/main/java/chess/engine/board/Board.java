package chess.engine.board;

import chess.PieceType;
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
        board = new HashMap<>();
        initialize(PlayerColor.BLACK);
        initialize(PlayerColor.WHITE);
    }


    private void initialize(PlayerColor color) {
        for (Position position : Position.InitialPosition(PieceType.PAWN, color)) {
            add(position, new Pawn(color));
        }
        for (Position position : Position.InitialPosition(PieceType.ROOK, color)) {
            add(position, new Rook(color));
        }
        for (Position position : Position.InitialPosition(PieceType.KNIGHT, color)) {
            add(position, new Knight(color));
        }
        for (Position position : Position.InitialPosition(PieceType.BISHOP, color)) {
            add(position, new Bishop(color));
        }
        for (Position position : Position.InitialPosition(PieceType.QUEEN, color)) {
            add(position, new Queen(color));
        }
        for (Position position : Position.InitialPosition(PieceType.KING, color)) {
            add(position, new King(color));
        }
    }
}
