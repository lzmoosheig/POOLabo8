package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.*;

import java.util.Map;

/**
 * Class définissant une piece de type King
 */
public class King extends PieceExtend implements StraightMove, DiagonalMove {
    /**
     * Constructeur de King
     *
     * @param color La couleur de la pièce
     */
    public King(PlayerColor color) {
        super(color, PieceType.KING);
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
        if (Move.moveToSameColor(from, to)) {
            return false;
        }
        if (StraightMove.isStraight(from.getKey(), to.getKey())
                && StraightMove.getDistance(from.getKey(), to.getKey()) == 1) {
            return true;
        }

        if (DiagonalMove.isDiagonal(from.getKey(), to.getKey())
                && DiagonalMove.getDistance(from.getKey(), to.getKey()) == 1) {
            return true;
        }
        return false;
    }
}
