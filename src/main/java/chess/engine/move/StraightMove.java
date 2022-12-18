package chess.engine.move;

import chess.engine.board.Position;

/**
 * Interface permettant de distribuer des méthodes static pour les mouvements orthogonaux
 */
public interface StraightMove extends Move {
    /**
     * Permet de définir si le mouvement est orthogonal
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est orthogonal
     */
    static boolean isStraight(Position from, Position to) {
        return (Move.getAbsDistX(from, to) == 0 || Move.getAbsDistY(from, to) == 0);
    }

    /**
     * Permet de récupérer la distance en nombre de cases sur un tracé orthogonal
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return La distance en nombre de cases
     */
    static int getDistance(Position from, Position to) {
        if (!isStraight(from, to)) {
            throw new RuntimeException("Mouvement non orthogonal");
        }
        return Move.getAbsDistX(from, to) + Move.getAbsDistY(from, to);
    }
}
