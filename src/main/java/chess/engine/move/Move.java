package chess.engine.move;

import chess.PlayerColor;
import chess.engine.board.Position;

/**
 * Classe permettant de représenter les possibilités de déplacement des pièces
 *
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class Move {

    /**
     * Permet d'avoir un coefficient positif si la couleur est blanche et négative si la couleur est noire
     *
     * @param playerColor couleur demandée
     * @return -1 si va en arrière et 1 si va en avant
     */
    public static int getCoef(PlayerColor playerColor) {
        return playerColor == PlayerColor.WHITE ? 1 : -1;
    }

    /**
     * Permet de définir si le mouvement est une diagonale
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est une diagonale
     */
    public static boolean isDiagonal(Position from, Position to) {
        return getAbsDistX(from, to) == getAbsDistY(from, to);
    }

    /**
     * Permet de définir si le mouvement est orthogonal
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est orthogonal
     */
    public static boolean isStraight(Position from, Position to) {
        return getAbsDistX(from, to) == 0 || getAbsDistY(from, to) == 0;
    }

    /**
     * Permet de récupérer la distance en valeur absolue sur X
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return La distance entre départ et destination
     */
    public static int getAbsDistX(Position from, Position to) {
        return Math.abs(getDistX(from, to));
    }

    /**
     * Permet de récupérer la distance sur X
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return La distance entre départ et destination
     */
    public static int getDistX(Position from, Position to) {
        return to.getX() - from.getX();
    }

    /**
     * Permet de récupérer la distance sur Y
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return La distance entre départ et destination
     */
    public static int getDistY(Position from, Position to) {
        return to.getY() - from.getY();
    }

    /**
     * Permet de récupérer la distance en valeur absolue sur Y
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return La distance entre départ et destination
     */
    public static int getAbsDistY(Position from, Position to) {
        return Math.abs(to.getY() - from.getY());
    }

    /**
     * Permet de récupérer la distance parcourue depuis from vers to
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return La distance parcourue entre from et to, -1 si la distance n'est pas orthogonale ou diagonale
     */
    public static int getDistance(Position from, Position to) {
        if (isStraight(from, to)) {
            return getAbsDistX(from, to) + getAbsDistY(from, to);
        }
        if (isDiagonal(from, to)) {
            return getAbsDistX(from, to);
        }
        return -1;
    }

    /**
     * Récupère les positions se trouvant sur le chemin entre from (non compris) et to (non compris)
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return un tableau de positions entre from (non compris) et to (non compris)
     */
    public static Position[] getWay(Position from, Position to) {
        int dist = getDistance(from, to);
        // Si on se déplace que d'une case pas de chemin
        if (dist == 1) {
            return null;
        }

        int xCoef = getDistX(from, to) < 0 ? -1 : 1;
        int yCoef = getDistY(from, to) < 0 ? -1 : 1;

        Position[] way = new Position[dist - 1];

        // Si le chemin est en diagonale
        if (isDiagonal(from, to)) {
            for (int i = 1; i < getAbsDistX(from, to); ++i) {
                int x = from.getX() + i * xCoef;
                int y = from.getY() + i * yCoef;
                way[i - 1] = new Position(x, y);
            }
            return way;
        }
        // Si le chemin est en ligne droite
        if (isStraight(from, to)) {
            // Si il reste sur ligne verticale
            if (getDistX(from, to) == 0) {
                for (int i = 1; i < getAbsDistY(from, to); ++i) {
                    int y = from.getY() + i * yCoef;
                    way[i - 1] = new Position(from.getX(), y);
                }
            }
            // Si il reste sur la ligne verticale
            if (getDistY(from, to) == 0) {
                for (int i = 1; i < getAbsDistX(from, to); ++i) {
                    int x = from.getX() + i * xCoef;
                    way[i - 1] = new Position(x, from.getY());
                }
            }
            return way;
        }
        throw new RuntimeException("Mouvement illégal");
    }

}
