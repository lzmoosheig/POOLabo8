package chess.engine.board;

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
}
