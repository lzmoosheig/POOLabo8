package chess.engine.move;

import chess.engine.board.Position;


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
}
