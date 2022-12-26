package chess.engine.move;

import chess.engine.board.Position;


/**
 * Interface permettant de distribuer des méthodes static pour les mouvements orthogonaux
 */
public interface DiagonalMove extends Move {
    /**
     * Permet de définir si le mouvement est une diagonale
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est une diagonale
     */
    static boolean isDiagonal(Position from, Position to) {
        return Move.getAbsDistX(from, to) == Move.getAbsDistY(from, to);
    }

    /**
     * Permet de récupérer la distance en nombre de cases sur un tracé en diagonale
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return La distance en nombre de cases
     */
    static int getDistance(Position from, Position to) {
        if (!isDiagonal(from, to)) {
            throw new RuntimeException("Mouvement non diagonale");
        }
        double mathematicalDistance = Math.hypot(Move.getAbsDistX(from, to), Move.getAbsDistY(from, to));
        int cellDistance = (int) Math.round(mathematicalDistance / Math.sqrt(2));
        return cellDistance;
    }
}
