package chess.engine.board;

import chess.PieceType;
import chess.PlayerColor;
import java.util.Objects;

/**
 * Classe permettant de définir une position sur un échiquier
 */
public class Position {
    /**
     * Stocke la coordonnée x
     */
    private int x;

    /**
     * Stocke la coordonnée y
     */
    private int y;

    /**
     * Constructeur de position
     *
     * @param x La coordonnée x
     * @param y La coordonnée y
     */
    public Position(int x, int y) {
        //TODO vérifier out of bound
        this.x = x;
        this.y = y;
    }

    /**
     * Recuperation de la coordonnée x
     *
     * @return la coordonnée x
     */
    public int getX() {
        return x;
    }

    /**
     * Recuperation de la coordonnée y
     *
     * @return la coordonnée y
     */
    public int getY() {
        return y;
    }

    /**
     * Implementation de la methode hashCode
     *
     * @return le hashCode de l'objet instancié
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Permet de comprer les attributs de la position instanciée avec celle passée en paramètre
     *
     * @param other La position à comparer
     * @return true si les attributs sont identiques
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (other instanceof Position position) {
            return x == position.x && y == position.y;
        }
        return false;
    }

    public static Position[] InitialPosition(PieceType type, PlayerColor color) {
        int pawnTeamIndex = color == PlayerColor.BLACK ? 6 : 1;
        int teamIndex = color == PlayerColor.BLACK ? 7 : 0;
        switch (type) {
            case PAWN -> {
                Position[] positions = new Position[Board.SIZE];
                for (int i = 0; i < Board.SIZE; ++i){
                    positions[i] = new Position(i, pawnTeamIndex);
                }
                return positions;
            }

            case ROOK -> {
                return new Position[] {new Position(0, teamIndex), new Position(7, teamIndex)};
            }
            case KNIGHT -> {
                return new Position[] {new Position(1, teamIndex), new Position(6, teamIndex)};
            }
            case BISHOP -> {
                return new Position[] {new Position(2, teamIndex), new Position(5, teamIndex)};
            }
            case QUEEN -> {
                return new Position[] {new Position(3, teamIndex)};
            }
            case KING -> {
                return new Position[] {new Position(4, teamIndex)};
            }
            default ->{
                return new Position[]{};
            }
        }
    }
}
