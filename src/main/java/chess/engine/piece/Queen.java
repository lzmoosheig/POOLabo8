package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.DiagonalMove;
import chess.engine.move.Move;
import chess.engine.move.StraightMove;

import java.util.Map;

/**
 * Class définissant une piece de type Queen
 */
public class Queen extends Piece implements DiagonalMove, StraightMove {
    /**
     * Constructeur de Queen
     *
     * @param color La couleur de la pièce
     */
    public Queen(PlayerColor color) {
        super(color, PieceType.QUEEN);
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
        if (!DiagonalMove.isDiagonal(from.getKey(), to.getKey())
                && !StraightMove.isStraight(from.getKey(), to.getKey())
                || Move.moveToSameColor(from, to)) {
            return false;
        }
        return true;
    }
}
