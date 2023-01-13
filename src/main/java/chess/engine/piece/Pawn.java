package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.Move;

/**
 * Class définissant une piece de type Pawn
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class Pawn extends PieceExtend {
    /**
     * Constructeur de Pawn
     *
     * @param color La couleur de la pièce
     */

//    private int firstMoveTurn;

//    public void setFirstMoveTurn(int firstMoveTurn) {
//        if (!getFirstMove()) return;
//        this.firstMoveTurn = firstMoveTurn;
//    }

//    public int getFirstMoveTurn() {
//        return firstMoveTurn;
//    }

    public Pawn(PlayerColor color) {
        super(color, PieceType.PAWN);
    }

    /**
     * Permet de s'assurer d'un mouvement vertical
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est vertical
     */
    public boolean moveAhead(Position from, Position to) {
        int coef = Move.getCoef(getColor());
        return (to.getY() - from.getY()) * coef > 0 && to.getX()-from.getX() == 0;
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
        return Move.isStraight(from, to)
                && moveAhead(from, to)
                && Move.getDistance(from, to) <= (firstMove ? 2 : 1);
    }
}
