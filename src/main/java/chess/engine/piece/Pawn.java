package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.DiagonalMove;
import chess.engine.move.Move;
import chess.engine.move.StraightMove;

import java.util.Map;

/**
 * Class définissant une piece de type Pawn
 */
public class Pawn extends PieceExtend implements StraightMove, DiagonalMove {
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
        int coef = getColor().ordinal() == 1 ? -1 : 1;
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
    public boolean legalMove(Map.Entry<Position, Piece> from, Map.Entry<Position, Piece> to) {
        //TODO Faire le contrôle de la diagonale si il y a un pion
        //TODO Faire le contrôle que le pion puisse aller seulement en avant

        if (!StraightMove.isStraight(from.getKey(), to.getKey())
                || !moveAhead(from.getKey(), to.getKey())
                || StraightMove.getDistance(from.getKey(), to.getKey()) > (firstMove ? 2 : 1)
                || Move.moveToSameColor(from, to)) {
            return false;
        }
        firstMove = false;
        return true;
    }
}
