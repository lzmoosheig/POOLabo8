package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.DiagonalMove;
import chess.engine.move.StraightMove;

/**
 * Class définissant une piece de type Queen
 */
public class Queen extends Piece {
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
    public boolean legalMove(Position from, Position to) {
        if (!DiagonalMove.isDiagonal(from, to)
                && !StraightMove.isStraight(from, to)) {
            return false;
        }
        return true;
    }
}
