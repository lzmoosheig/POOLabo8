package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.Move;
import chess.engine.move.StraightMove;
import chess.engine.board.Board;

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
    public boolean legalMove(Board board, Position from, Position to) {
        if (Move.moveToSameColor(board, from, to) || !StraightMove.isStraight(from, to)) {
            return false;
        }
        return true;
    }
}
