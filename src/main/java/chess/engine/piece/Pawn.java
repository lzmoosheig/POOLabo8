package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.StraightMove;

/**
 * Class définissant une piece de type Pawn
 */
public class Pawn extends PieceExtend {
    /**
     * Constructeur de Pawn
     *
     * @param color La couleur de la pièce
     */
    public Pawn(PlayerColor color) {
        super(color, PieceType.PAWN);
    }

    /**
     * Permet de s'assurer d'un mouvement vertical
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est vertical
     */
    private boolean moveAhead(Position from, Position to) {
        int coef = getColor() == PlayerColor.BLACK ? -1 : 1;
        return ((to.getY() - from.getY()) * coef > 0);
    }

    /**
     * Permet de définir si le mouvement est legal
     *
     * @param from La clé-valeur de départ
     * @param to   La clé-valeur de destination
     * @return true si le mouvement est legal
     */
    @Override
    public boolean legalMove(Position from, Position to) {
        //TODO Faire le contrôle de la diagonale si il y a un pion
        //TODO Faire le contrôle que le pion puisse aller seulement en avant

        if (!StraightMove.isStraight(from, to)
                || !moveAhead(from, to)
                || StraightMove.getDistance(from, to) > (firstMove ? 2 : 1)) {
            return false;
        }
        firstMove = false;
        return true;
    }
}
