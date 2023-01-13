package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;

/**
 * Class d'extension de Piece
 *
 * @author Anthony David, Alexadnre Iorio, Léo Zmoos
 */
public abstract class PieceExtend extends Piece {
    protected boolean firstMove = true; // si c'est le premier mouvement de la pièce

    /**
     * Réimplémentation du constructeur de Piece
     *
     * @param color La couleur de la pièce
     * @param type  Le type de la pièce
     */
    public PieceExtend(PlayerColor color, PieceType type) {
        super(color, type);
    }

    /**
     * Getter sur le premier mouvement
     *
     * @return vrai si c'est le premier mouvement
     */
    public boolean getFirstMove() {
        return firstMove;
    }

    /**
     * Setter permettant de passer le premier mouvement à false lors de celui-ci.
     */
    public void setFirstMove() {
        this.firstMove = false;
    }
}
