package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.DiagonalMove;

/**
 * Class définissant une piece de type Bishop
 */
public class Bishop extends Piece {
    /**
     * Constructeur de Bishop
     *
     * @param color La couleur de la pièce
     */
    public Bishop(PlayerColor color) {
        super(color, PieceType.BISHOP);
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
        if (!DiagonalMove.isDiagonal(from, to)) {
            return false;
        }
        return true;
    }
}
