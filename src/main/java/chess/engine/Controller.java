package chess.engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import chess.engine.move.Move;
import chess.views.gui.GUIView;
import chess.engine.board.*;
import chess.engine.piece.*;


import java.util.Map.Entry;

/**
 * Class permettant de controller une partie d'échec.
 */
public class Controller implements ChessController {

    /**
     * Stock la view que le Controlleur doit controller
     */
    private ChessView view;
    /**
     * Lors d'un événement, stock la position clé-valeur du premier click de l'utilisateur
     */
    private Entry<Position, Piece> from;
    /**
     * Lors d'un événement, stock la position clé-valeur du second click de l'utilisateur
     */
    private Entry<Position, Piece> to;

    /**
     * Stock la board que le Controlleur doit controller
     */
    private final Board board;

    /**
     * Définit si c'est le tour de blanc
     */
    private boolean isBlackTurn = false;

    /**
     * Constructeur du Controller
     *
     * @param board La board que le Controller doit controller
     */
    public Controller(Board board) {
        if (board == null) {
            throw new RuntimeException("board can't be null");
        }
        this.board = board;
    }

    /**
     * Démmare le jeu
     *
     * @param view La vue sur laquelle le jeu doit démarrer
     * @throws RuntimeException si la vue est null
     */
    @Override
    public void start(ChessView view) {
        if (view == null) throw new RuntimeException("The view can't be null");
        isBlackTurn = false;
        this.view = view;
        initialize(board);
        view.startView();
    }

    /**
     * Lance une nouvelle partie
     */
    @Override
    public void newGame() {
            start(new GUIView(this));
    }

    /**
     * Methode appelée lors de la demande de déplacement par l'utilisateur
     *
     * @param fromX la coordonnée sur X du premier click
     * @param fromY la coordonnée sur Y du premier click
     * @param toX   la coordonnée sur X du second click
     * @param toY   la coordonnée sur Y du second click
     * @return true si le déplacement à bien été effectué
     */
    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        captureEvent(fromX, fromY, toX, toY);
        if (from.getValue() == null) {
            view.displayMessage("Aucune pièce seléctionnée");
            return false;
        }

        if (!executeMove()) {
            return false;
        }
        board.move(from, to);
        return true;
    }

    private boolean isCorrectPlayer() {
        return from.getValue().getColor()  == (isBlackTurn? PlayerColor.WHITE : PlayerColor.BLACK);
    }

    /**
     * Capture un événement de déplacement demandé par l'utilisateur et complète les attributs de la classe Controller
     *
     * @param fromX la coordonnée sur X du premier click
     * @param fromY la coordonnée sur Y du premier click
     * @param toX   la coordonnée sur X du second click
     * @param toY   la coordonnée sur Y du second click
     */
    private void captureEvent(int fromX, int fromY, int toX, int toY) {
        from = board.getEntry(new Position(fromX, fromY));
        to = board.getEntry(new Position(toX, toY));
    }

    /**
     * Initialisation d'une vue avec les pièces dans leurs positions initiales en les stockant dans une board
     *
     * @param board La board dans laquelle les pièces vont être stockées
     */
    private void initialize(Board board) {
        board.initialize();
        putPieces();
    }

    /**
     * Réalise le mouvement et effectue les actions nécessaires pour mettre à jour la board et la view.
     */
    private boolean executeMove() {
        if (!canMove()){
            return false;
        }
        to.setValue(from.getValue());
        removePiece(from);
        putPiece(to);
        isBlackTurn = !isBlackTurn;
        return true;
    }

    private boolean canMove(){

        if (pawnCanEat()) return true;

        if (isCorrectPlayer()){
            view.displayMessage("C'est à l'équipe adverse de jouer !");
            return false;
        }
        if (isLegalMove()) {
            view.displayMessage("Mouvement interdit");
            return false;
        }
        if(isSameColor()){
            view.displayMessage("La destination possède déjà une pièce");
            return false;
        }

        if(from.getValue().getType() != PieceType.KNIGHT && collisionExist()){
            view.displayMessage("Il y a une collision");
            return false;
        }
        return true;
    }

    private boolean pawnCanEat(){

        if ( from.getValue() instanceof Pawn pawn
                && to.getValue() != null
                && Move.isDiagonal(from.getKey(), to.getKey())
                && Move.getDistance(from.getKey(), to.getKey()) == 1 ){

            return  pawn.moveAhead(from.getKey(), to.getKey())
                    && to.getValue().getColor() != from.getValue().getColor();
        }
        return false;

    }

    /**
     * Permet de définir si le mouvement est legal respectivement à la règle de déplacement de la pièce séléctionnée
     * @return True si le mouvement est légal
     */
    private boolean isLegalMove() {
        return !from.getValue().legalMove(from.getKey(), to.getKey());
    }

    /**
     * Permet de définir si la destination possède est occupée par le même joueur
     * @return True si le la destination est de la même couleur
     */
    private boolean isSameColor() {
        if (to.getValue() == null) {
            return false;
        }
        return from.getValue().getColor() == to.getValue().getColor();
    }

    /**
     * Permet de définir si une pièce est présente sur le chemin et qu'elle crée une collision
     * @return true si il y a une collision
     */
    private boolean collisionExist() {
        Position[] way = Move.getWay(from.getKey(), to.getKey());
        if (way == null) {
            return false;
        }
        for (Position p : way){
            if (board.getEntry(p).getValue() != null){
                return true;
            }
        }
        return false;
    }

    /**
     * Permet de mettre à jour la view avec l'ajout d'une pièce
     *
     * @param entry La clé-valeur comprennent la position ainsi que la pièce à ajouter
     */
    private void putPiece(Entry<Position, Piece> entry) {
        putPiece(entry.getKey(), entry.getValue());
    }

    /**
     * Permet de mettre à jour la view avec l'ajout d'une pièce
     *
     * @param position La position sur laquelle la pièce doit être ajoutée
     * @param piece    La pièce à ajouter
     */
    private void putPiece(Position position, Piece piece) {
        view.putPiece(piece.getType(), piece.getColor(), position.getX(), position.getY());
    }

    /**
     * Permet de mettre à jour la view avec les pièces du board
     */
    private void putPieces() {
        for (Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            putPiece(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Permet de mettre à jour la view avec le retrait d'une pièce
     *
     * @param entry La clé-valeur comprennent la position ainsi que la pièce à retirer
     */
    private void removePiece(Entry<Position, Piece> entry) {
        removePiece(entry.getKey());
    }

    /**
     * Permet de mettre à jour la view avec le retrait d'une pièce
     *
     * @param position La position de la pièce à retirer
     */
    private void removePiece(Position position) {
        view.removePiece(position.getX(), position.getY());
    }


}
