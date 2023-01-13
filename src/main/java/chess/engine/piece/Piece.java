package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Position;

/**
 * Classe permettant de définir une pièce du jeu
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public abstract class Piece {
    private final PieceType type;
    private final PlayerColor color;

    /**
     * Constructeur d'une pièce
     *
     * @param color La couleur de la pièce
     * @param type  Le type de la pièce
     */
    public Piece(PlayerColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    /**
     * Getter sur le type de la pièce
     *
     * @return Le type de la pièce
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Getter sur la couleur de la pièce
     *
     * @return La couleur de la pièce.
     */
    public PlayerColor getColor() {
        return this.color;
    }

    /**
     * Affichage d'une pièce
     *
     * @return Un String contenant les éléments affichables de la pièce.
     */
    @Override
    public String toString() {
        return color.toString() + " " + type.toString();
    }

    /**
     * Permet de savoir si un déplacement de la pièce est autorisé
     *
     * @param from position de départ de la pièce
     * @param to   position d'arrivée de la pièce
     * @return vrai si le déplacement est légal
     */
    public abstract boolean legalMove(Position from, Position to);
}
