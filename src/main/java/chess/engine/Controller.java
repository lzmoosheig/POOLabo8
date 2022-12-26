package chess.engine;

import chess.ChessController;
import chess.ChessView;
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
        //TODO Faire une methode IsMyTurn
        if (from.getValue().getColor().ordinal() == (isBlackTurn? 0 : 1)){
            view.displayMessage("C'est à l'équipe adverse de jouer !");
            return false;
        }
        // TODO Jusque ici
        isBlackTurn = !isBlackTurn;
        if (from.getValue() == null) {
            view.displayMessage("Aucune Pièce seléctionnée");
            return false;
        }

        if (!executeMove()) {
            return false;
        }
        board.move(from, to);
        return true;
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
        //TODO Manger une piece ici
        //if (to.getValue() != null){/*Manger une pièce*/}
        if (!from.getValue().legalMove(board ,from.getKey(), to.getKey())){
            view.displayMessage("Mouvement interdit");
            return false;
        }
        to.setValue(from.getValue());
        removePiece(from);
        putPiece(to);
        return true;
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
