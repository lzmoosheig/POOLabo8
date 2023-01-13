package chess.engine.piece;

import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;
import chess.engine.move.Move;

/**
 * Class définissant une piece de type Queen
 *
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
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
        return Move.isDiagonal(from, to) || Move.isStraight(from, to);
    }

}
