package chess.engine.board;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.piece.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

public class Board implements Cloneable {
    /**
     * Constant of board Size
     */
    public final static byte SIZE = 8;
    private HashMap<Position, Piece> board = new HashMap<>();

    public Board(){}
    public Board(Board other){
        board.putAll(other.getBoard());
    }
    public void add(Position position, Piece piece) {
        board.put(position, piece);
    }

    public void remove(Position position) {
        board.remove(position);
    }

    public void move(Position from, Position to){
        add(to, board.get(from));
        board.remove(from);
    }

    public Piece getPiece(Position position){
        return board.get(position);
    }
    public HashMap<Position, Piece> getBoard() {
        return new HashMap<>(board);
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
