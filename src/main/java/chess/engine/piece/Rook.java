package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.Move;
import chess.engine.move.StraightMove;

import java.util.Map;

/**
 * Class définissant une piece de type Rook
 */
public class Rook extends PieceExtend implements StraightMove {
    /**
     * Constructeur de Rook
     *
     * @param color La couleur de la pièce
     */
    public Rook(PlayerColor color) {
        super(color, PieceType.ROOK);
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
        if (Move.moveToSameColor(from, to) || !StraightMove.isStraight(from.getKey(), to.getKey())) {
            return false;
        }
        return true;
    }
}
