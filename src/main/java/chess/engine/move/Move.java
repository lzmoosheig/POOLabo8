package chess.engine.move;

import chess.engine.board.Position;
import chess.engine.piece.Piece;
import chess.engine.board.Board;

import java.util.Map.Entry;

public interface Move {
    /**
     * Permet de récupérer la distance en valeur absolue sur X
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance entre départ et destination
     */
    static int getAbsDistX(Position from, Position to){
        return Math.abs(to.getX() - from.getX());
    }
    /**
     * Permet de récupérer la distance en valeur absolue sur Y
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance entre départ et destination
     */
    static int getAbsDistY(Position from, Position to){
        return Math.abs(to.getY() - from.getY());
    }
    /**
     * Fonction determinant l'Hypoténuse entre deux positions
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance la diagonale entre départ et destination
     */
    static double pythagore(Position from, Position to){
        int distX = Move.getAbsDistX(from,to);
        int distY = Move.getAbsDistY(from,to);
        return Math.sqrt(distX*distX + distY*distY);
    }

    /**
     * Permet de définir si la destination est libre
     * @param to La clé-valeur de destination
     * @return true si la destination est libre
     */
    static boolean positionIsEmpty(Board board, Position to){
        return board.getBoard().get(to) == null;
    }

    /**
     * Permet de définir si la destination possède est occupée par le même joueur
     * @param from La clé-valeur de départ
     * @param to La clé-valeur de destination
     * @return true si le joueur present sur la destination est de la même couleur
     */
    static boolean moveToSameColor(Board board, Position from, Position to){
        if (positionIsEmpty(board, to)) {
            return false;
        }
        return board.getBoard().get(from).getColor() == board.getBoard().get(from).getColor();
    }

    /**
     * Permet de définir si le mouvement est legal
     * @param from La clé-valeur de départ
     * @param to La clé-valeur de destination
     * @return true si le mouvement est legal
     */
    //TODO Verifier si il y a une pièce sur le tracé (sauf pour knight qui peut passer par dessus)
    boolean legalMove(Board board, Position from, Position to );

}
