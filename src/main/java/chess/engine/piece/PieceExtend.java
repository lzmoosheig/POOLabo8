package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;

/**
 * Class d'extension de Piece
 */
public abstract class PieceExtend extends Piece {
    /**
     * Permet de savoir si la pièce à déjà fait son premier mouvement
     */
    boolean firstMove = true;

    /**
     * Constructeur d'une pièce
     *
     * @param color La couleur de la pièce
     * @param type  Le type de la pièce
     */
    public PieceExtend(PlayerColor color, PieceType type) {
        super(color, type);
    }
}
