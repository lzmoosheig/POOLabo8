package chess.engine.move;
import chess.engine.board.Position;

public class Move {

    /**
     * Permet de définir si le mouvement est une diagonale
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est une diagonale
     */
    public static boolean isDiagonal(Position from, Position to) {
        return Move.getAbsDistX(from, to) == Move.getAbsDistY(from, to);
    }

    /**
     * Permet de définir si le mouvement est orthogonal
     *
     * @param from La position de départ
     * @param to   La position de destination
     * @return true si le mouvement est orthogonal
     */
    public static boolean isStraight(Position from, Position to) {
        return (Move.getAbsDistX(from, to) == 0 || Move.getAbsDistY(from, to) == 0);
    }

    /**
     * Permet de récupérer la distance en valeur absolue sur X
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance entre départ et destination
     */
    public static int getAbsDistX(Position from, Position to){
        return Math.abs(getDistX(from , to));
    }
    /**
     * Permet de récupérer la distance sur X
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance entre départ et destination
     */
    public static int getDistX(Position from, Position to){
       return to.getX() - from.getX();
    }

    /**
     * Permet de récupérer la distance sur Y
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance entre départ et destination
     */
    public static int getDistY(Position from, Position to){
        return to.getY() - from.getY();
    }

    /**
     * Permet de récupérer la distance en valeur absolue sur Y
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance entre départ et destination
     */
    public static int getAbsDistY(Position from, Position to){
        return Math.abs(to.getY() - from.getY());
    }

    /**
     * Permet de récupérer la distance parcourue depuis from vers to
     * @param from La position de départ
     * @param to La position de destination
     * @return La distance parcourue entre from et to
     */
    public static int getDistance (Position from, Position to) {
        if (isStraight(from, to)){
            return getAbsDistX(from, to) + getAbsDistY(from, to);
        }
        if (isDiagonal(from, to)) {
            return getAbsDistX(from, to);
        }
        throw new RuntimeException("Mouvement illégal");
    }

    /**
     * Récupère les positions se trouvant sur le chemin entre from(non compris) et to (non compris)
     * @param from La position de départ
     * @param to La position de destination
     * @return un tableau de positions entre from (non compris) et to (non compris)
     */
    public static Position[] getWay(Position from, Position to){
        if(getDistance(from, to) == 1){
            return null;
        }
        int xCoef = getDistX(from, to) < 0 ? -1 : 1 ;
        int yCoef = getDistY(from, to) < 0 ? -1 : 1 ;
        Position[] way = new Position[getDistance(from, to) - 1];
        if (isDiagonal(from, to)){
            for (int i = 1; i < getAbsDistX(from, to); ++i){
                int x = from.getX() + i * xCoef;
                int y = from.getY() + i * yCoef;
                way[i-1] = new Position(x,y) ;
            }
            return way;
        }
        if (isStraight(from, to)){
            if(getDistX(from, to) == 0){
                for (int i = 1; i < getAbsDistY(from, to) ; ++i){
                    int y = from.getY() + i * yCoef;
                    way[i-1] = new Position(from.getX(),y) ;
                }
            }
            if(getDistY(from, to) == 0){
                for (int i = 1; i < getAbsDistX(from, to) ; ++i){
                    int x = from.getY() + i * xCoef;
                    way[i-1] = new Position(x,from.getY()) ;
                }
            }
            return way;
        }
        throw new RuntimeException("Mouvement illégal");
    }

}
