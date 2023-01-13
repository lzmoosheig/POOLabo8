package chess.engine.piece;

import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.Move;

/**
 * Class définissant une piece de type Knight
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class Knight extends Piece implements ChessView.UserChoice {
    final private double DISTANCE_MAX = Math.sqrt(1 + 2 * 2); //distance exacte que Knight doit respecter lors d'un mouvement

    /**
     * Constructeur de Knight
     *
     * @param color La couleur de la pièce
     */
    public Knight(PlayerColor color) {
        super(color, PieceType.KNIGHT);
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
        return DISTANCE_MAX == Math.hypot(Move.getAbsDistX(from,to), Move.getAbsDistY(from,to));
    }

    /**
     * Réimplémentation de la fonction
     *
     * @return String avec le type de la classe parente
     */
    @Override
    public String textValue() {
        return super.getType().toString();
    }
}
