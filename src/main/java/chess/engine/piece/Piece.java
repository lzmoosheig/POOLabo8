package chess.engine.piece;

import chess.PieceType;
import chess.PlayerColor;

/**
 * Classe permettant de définir une pièce du jeu
 */
public abstract class Piece {
    /**
     * Stock le type de la pièce
     */
    private final PieceType type;

    /**
     * Stock la couleur de la pièce
     */
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
     * Permet de récupérer le type de la pièce instanciée
     *
     * @return Le type de la pièce
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Permet de récupérer la couleur de la pièce instanciée
     *
     * @return La couleur de la pièce.
     */
    public PlayerColor getColor() {
        return this.color;
    }

    /**
     * Compare la couleur de la pièce instanciée avec celle passée en paramètre
     *
     * @param other La pièce à comparer
     * @return true si la couleur est identique
     */
    private boolean equalsColor(Piece other) {
        return color == other.color;
    }

    /**
     * Permet de comprer les attributs de la pièce instanciée avec celle passée en paramètre
     *
     * @param other La pièce à comparer
     * @return true si les attributs sont identiques
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (other instanceof Piece piece) {
            return type == piece.type && equalsColor(piece);
        }
        return false;
    }

    /**
     * Affichage d'une pièce
     *
     * @return Une string contenant les éléments affichables de la pièce.
     */
    @Override
    public String toString() {
        return color.toString() + " " + type.toString();
    }

}
