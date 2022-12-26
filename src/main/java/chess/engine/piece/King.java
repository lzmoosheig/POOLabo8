package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.*;


/**
 * Class définissant une piece de type King
 */
public class King extends PieceExtend {
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
    public boolean legalMove(Position from, Position to) {
        if (StraightMove.isStraight(from, to)
                && StraightMove.getDistance(from, to) == 1) {
            return true;
        }

        if (DiagonalMove.isDiagonal(from, to)
                && DiagonalMove.getDistance(from, to) == 1) {
            return true;
        }
        return false;
    }
}
