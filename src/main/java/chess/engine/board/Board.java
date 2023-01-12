package chess.engine.board;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.piece.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Classe permettant de représenter l'échiquier
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class Board implements Cloneable {
    public final static byte SIZE = 8; // Taille de l'échiquier
    private HashMap<Position, Piece> board = new HashMap<>();

    /**
     * Constructeur de la classe Board
     */
    public Board(){}

    /**
     * Constructeur par copie de la classe Board
     * @param other échiquier à copier
     */
    public Board(Board other){
        board.putAll(other.getBoard());
    }

    /**
     * Ajout d'une pièce sur l'échiquier
     * @param position emplacement où mettre la pièce
     * @param piece pièce à placer
     */
    public void add(Position position, Piece piece) {
        board.put(position, piece);
    }

    /**
     * Suppression du contenu d'une case de l'échiquer
     * @param position emplaçement où supprimer
     */
    public void remove(Position position) {
        board.remove(position);
    }

    /**
     * Déplacer le contenu d'une positin de l'échiquier à une autre
     * @param from
     * @param to
     */
    public void move(Position from, Position to){
        add(to, board.get(from));
        board.remove(from);
    }

    /**
     * Permet de savoir quelle pièce est à une position spécifique
     * @param position dont on veut connaître le contenu
     * @return pièce sur la position
     */
    public Piece getPiece(Position position){
        return board.get(position);
    }

    /**
     * Getter sur l'échiquier
     * @return l'échiquier sour forme de HashMap<Position, Piece>
     */
    public HashMap<Position, Piece> getBoard() {
        return new HashMap<>(board);
    }

    /**
     * Retourne une association Position-Piece
     * @param position sur laquel on veut connaître l'association
     * @return associtation sous forme Entry<Position, Piece>
     */
    public Entry<Position, Piece> getEntry(Position position){
        if (position == null){
            throw new RuntimeException("Position can't be null");
        }
        return new AbstractMap.SimpleEntry<>(position, board.get(position));
    }

    /**
     * Initialise tout l'échiquier avec toutes les pièces nécéssaires au démarrage du jeu
     */
    public void initialize() {

//        add(new Position(5,5), new Knight(PlayerColor.WHITE));
//        add(new Position(7,7), new King(PlayerColor.BLACK));
//        add(new Position(0,0), new King(PlayerColor.WHITE));
//        add(new Position(6,6), new Rook(PlayerColor.WHITE));

        board = new HashMap<>();
        initialize(PlayerColor.BLACK);
        initialize(PlayerColor.WHITE);
    }

    /**
     * Initialise toutes les pièces d'une couleur nécéssaires au démarrage du jeu
     * @param color couleur des pièces à initialiser
     */
    private void initialize(PlayerColor color) {
        for (Position position : Position.initialPosition(PieceType.PAWN, color)) {
            add(position, new Pawn(color));
        }
        for (Position position : Position.initialPosition(PieceType.ROOK, color)) {
            add(position, new Rook(color));
        }
        for (Position position : Position.initialPosition(PieceType.KNIGHT, color)) {
            add(position, new Knight(color));
        }
        for (Position position : Position.initialPosition(PieceType.BISHOP, color)) {
            add(position, new Bishop(color));
        }
        for (Position position : Position.initialPosition(PieceType.QUEEN, color)) {
            add(position, new Queen(color));
        }
        for (Position position : Position.initialPosition(PieceType.KING, color)) {
            add(position, new King(color));
        }
    }
}
