package chess.engine.board;

import chess.PieceType;
import chess.PlayerColor;

import java.util.Objects;

/**
 * Classe permettant de représenter une position sur l'échiquier
 *
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Constructeur de la classe position
     *
     * @param x La coordonnée x (largeur de l'échiquier)
     * @param y La coordonnée y (hauteur de l'échiquier)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter de la coordonnée X
     *
     * @return la coordonnée x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter de la coordonnée Y
     *
     * @return la coordonnée y
     */
    public int getY() {
        return y;
    }

    /**
     * Implementation de la méthode hashCode
     *
     * @return le hashCode de l'objet instancié
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Permet de comparer les attributs de la position instanciée avec celle passée en paramètre
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

    /**
     * Permet de connaître toutes les positions d'un type de pièce d'une couleur donnée
     *
     * @param type  de(s) pièce(s) dont on veut connaître la/les position(s)
     * @param color de(s) pièce(s) dont on veut connaître la/les position(s)
     * @return un tableau de Position de toutes les pièces du type et de la couleur
     */
    public static Position[] initialPosition(PieceType type, PlayerColor color) {
        int pawnTeamIndex = color == PlayerColor.BLACK ? 6 : 1;
        int teamIndex = color == PlayerColor.BLACK ? 7 : 0;
        switch (type) {
            case PAWN -> {
                Position[] positions = new Position[Board.SIZE];
                for (int i = 0; i < Board.SIZE; ++i) {
                    positions[i] = new Position(i, pawnTeamIndex);
                }
                return positions;
            }

            case ROOK -> {
                return new Position[]{new Position(0, teamIndex), new Position(7, teamIndex)};
            }
            case KNIGHT -> {
                return new Position[]{new Position(1, teamIndex), new Position(6, teamIndex)};
            }
            case BISHOP -> {
                return new Position[]{new Position(2, teamIndex), new Position(5, teamIndex)};
            }
            case QUEEN -> {
                return new Position[]{new Position(3, teamIndex)};
            }
            case KING -> {
                return new Position[]{new Position(4, teamIndex)};
            }
            default -> {
                return new Position[]{};
            }
        }
    }

    /**
     * Permet de savoir si une position se situe bien sûr l'échiquier
     *
     * @return vrai si la position est sur l'échiquier
     */
    public boolean isValidPosition() {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    /**
     * Implementation de la méthode toString()
     *
     * @return position dans le format "x : <x> - y : <y>"
     */
    @Override
    public String toString() {
        return "x : " + x + " - y : " + y;
    }
}
