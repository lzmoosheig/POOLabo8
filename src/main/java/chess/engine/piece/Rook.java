package chess.engine.piece;

import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.Move;

/**
 * Class définissant une piece de type Rook
 */
public class Rook extends PieceExtend implements ChessView.UserChoice {
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
    public boolean legalMove(Position from, Position to) {
        return Move.isStraight(from, to);
    }

    @Override
    public String textValue() {
        return super.getType().toString();
    }
}
