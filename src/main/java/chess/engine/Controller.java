package chess.engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Board;
import chess.engine.board.Position;
import chess.engine.move.Move;
import chess.engine.piece.*;

import java.util.ArrayList;
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

    private int turn;
    private Entry<Position, Piece> from;
    /**
     * Lors d'un événement, stock la position clé-valeur du second click de l'utilisateur
     */
    private Entry<Position, Piece> to;

    /**
     * Stock la board que le Controlleur doit controller
     */
    private Board board;

    /**
     * Stock un snapshot de la board afin de rétablir un état antérieur
     */
    private Board boardSnapShot = new Board();

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

    private boolean playerIsCheck(Position positionOfKingToCheck){
        return playerIsCheck(board, positionOfKingToCheck);
    }

    private boolean playerIsCheck(Board board, Position positionOfKingToCheck){
        Piece kingToCheck = board.getPiece(positionOfKingToCheck);
        for(Entry<Position, Piece> entry : board.getBoard().entrySet()){
            // on ne compare par ses propres pions
            if (!(kingToCheck instanceof King)
                    || entry.getValue().getColor() != opponentPlayer(kingToCheck.getColor())) {
                continue;
            }
            if (isCheck(entry.getKey(), positionOfKingToCheck)){
                return true;
            }
        }
        return false;
    }

    private Entry<Position, Piece> getKing(PlayerColor color){
        for (Entry<Position, Piece> entry : board.getBoard().entrySet()){
            if (entry.getValue() instanceof King king && king.getColor() == color){
                return entry;
            }
        }
        return null;
    }

    private boolean isCheck(Position from, Position to){
        return board.getPiece(from).legalMove(from, to) && !collisionExist(from, to);
    }

    private boolean checkmate(PlayerColor player){
        Position kingPosition = getKing(player).getKey();

        //sequences de directions
        int[] sequencesX = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] sequencesY = {-1, 0, 1, -1, 1, -1, 0, 1};

        ArrayList<Position> validPositions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int futurX = kingPosition.getX() + sequencesX[i];
            int futurY = kingPosition.getY() + sequencesY[i];
            Position futurPosition = new Position(futurX, futurY);
            Piece pieceOnFuturPosition = board.getPiece(futurPosition);

            if (futurPosition.isValidPosition() &&
                    (pieceOnFuturPosition == null || pieceOnFuturPosition.getColor() != player)){
                Board simulationBoard = new Board(board);
                simulationBoard.move(kingPosition, futurPosition);
                validPositions.add(futurPosition);
            }
        }

        if (validPositions.size() == 0) {
            return false;
        }

        for (Position validPosition : validPositions){
            Board simulationBoard = new Board(board);
            simulationBoard.move(kingPosition, validPosition);
            for (Entry<Position, Piece> entry : simulationBoard.getBoard().entrySet()){
                if (entry.getValue().legalMove(entry.getKey(), validPosition)
                        && !collisionExist(entry.getKey(), validPosition)){
                    return false;
                }
            }
            if (!playerIsCheck(simulationBoard, validPosition)){
                return false;
            }
        }
        return true;
    }

    /**
     * Démmare le jeu
     *
     * @param view La vue sur laquelle le jeu doit démarrer
     * @throws RuntimeException si la vue est null
     */
    @Override
    public void start(ChessView view) {
        this.view = view;
        newGame();
        view.startView();
    }

    /**
     * Lance une nouvelle partie
     */
    @Override
    public void newGame() {
        if (view == null) throw new RuntimeException("The view can't be null");
        isBlackTurn = false;
        initialize();
    }

    /**
     * Methode appelée lors de la demande de déplacement par l'utilisateur
     *
     * @param fromX la coordonnée sur X du premier click
     * @param fromY la coordonnée sur Y du premier click
     * @param toX   la coordonnée sur X du second click
     * @param toY   la coordonnée sur Y du second click
     * @return true si le déplacement a bien été effectué
     *
     */
    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {
        boardSnapShot = new Board(board);
        captureEvent(fromX, fromY, toX, toY);

        if (!to.getKey().isValidPosition()){
            view.displayMessage("Position invalide");
            return false;
        }

        if (from.getValue() == null) {
            view.displayMessage("Aucune pièce seléctionnée");
            return false;
        }

        if(canCastle(from.getKey(), to.getKey())){
            if (!castle(from.getKey(), to.getKey())){
                view.displayMessage("Action impossible, Le roque de met le roi en échec");
                return false;
            }
            finishTurn();
            return true;
        }

        if (!canMove()){
            return false;
        }



        if(from.getValue() instanceof PieceExtend pieceExtend){
            pieceExtend.setFirstMove();
        }

        to.setValue(from.getValue());

        board.move(from.getKey(), to.getKey());

        if(to.getKey().getY() == (isBlackTurn? 0 : 7) && from.getValue().getType() == PieceType.PAWN){
            Promotion();
        }

        if(playerIsCheck(getKing(currentPlayer()).getKey())) {
            view.displayMessage("Mouvement impossible - Echec");
            unDo();
            return false;
        }

        if(checkmate(opponentPlayer())) {
            view.displayMessage("Player "+ opponentPlayer() + " lose");
        }

        finishTurn();
        return true;
    }

    private void finishTurn(){
        turn++;
        isBlackTurn = !isBlackTurn;
        refreshView();
    }

    private void Promotion() {

        ChessView.UserChoice[] userChoices = new ChessView.UserChoice[]{
                new Queen(currentPlayer()),
                new Bishop(currentPlayer()),
                new Rook(currentPlayer()),
                new Knight(currentPlayer())};

        ChessView.UserChoice userChoice = view.askUser("Promotion du Pion",
                "Veuillez-choisir une pièce", userChoices);

        if (userChoice != null) {
            board.add(to.getKey(), (Piece) userChoice);
        }
    }

    private PlayerColor currentPlayer(){
        return isBlackTurn ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    private PlayerColor opponentPlayer(){
        return opponentPlayer(currentPlayer());
    }
    private PlayerColor opponentPlayer(PlayerColor player){
        return player == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    /**
     * Permet d'annuler la dernière action
     */
    private void unDo(){
        for (Entry<Position, Piece> entry : board.getBoard().entrySet()){
            removePiece(entry);
        }
        for (Entry<Position, Piece> entry : boardSnapShot.getBoard().entrySet()){
            putPiece(entry);
        }
        board = boardSnapShot;
    }

    /**
     * Définit si la pièce sélectionnée est celle du joueur courant
     * @return true si c'est le cas
     */
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
     */
    private void initialize() {
        board.initialize();
        putPieces();
    }
    private void refreshView(){
        clearView();
        putPieces();
    }

    private void clearView() {
        for (int x = 0; x < board.SIZE; ++x ){
            for (int y = 0; y < board.SIZE; ++y){
                view.removePiece(x, y);
            }
        }
    }

    /**
     * Définit si la pièce a le droit de bouger
     * @return true si elle peut
     */
    private boolean canMove(){

        if (isCorrectPlayer()){
            view.displayMessage("C'est à l'équipe adverse de jouer !");
            return false;
        }
        if (pawnCanEat()) return true;

        if (isLegalMove()) {
            view.displayMessage("Mouvement interdit");
            return false;
        }
        if(isSameColor()){
            view.displayMessage("La destination possède déjà une pièce");
            return false;
        }

        if(collisionExist()){
            view.displayMessage("Il y a une collision");
            return false;
        }
        return true;
    }

    /**
     * Permet de définir si un Pawn peut manger en diagonal
     * @return true si il peut
     */
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
     * Permet de définir si le mouvement est legal respectivement à la règle de déplacement de la pièce sélectionnée
     * @return True si le mouvement est légal
     */
    private boolean isLegalMove() {
        return !from.getValue().legalMove(from.getKey(), to.getKey());
    }


    private boolean isSameColor(Position to){
        Piece piece = board.getPiece(to);
        if (piece == null) {
            return false;
        }
        return from.getValue().getColor() == piece.getColor();
    }
    /**
     * Permet de définir si la destination possède est occupée par le même joueur
     * @return True si le la destination est de la même couleur
     */
    private boolean isSameColor() {
       return isSameColor(to.getKey());
    }

    /**
     * Permet de définir si une pièce est présente sur le chemin et qu'elle crée une collision
     * @from la position de départ
     * @to La position de destination
     * @return true si il y a une collision
     */
    private boolean collisionExist(Position from, Position to) {
        if (board.getPiece(from).getType() == PieceType.KNIGHT) return false;
        Position[] way = Move.getWay(from, to);
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
     * Permet de définir si une pièce est présente sur le chemin et qu'elle crée une collision
     * @return true si il y a une collision
     */
    private boolean collisionExist(){
        return collisionExist(from.getKey(), to.getKey());
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
        if (piece == null) return;
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

    public boolean canCastle(Position from, Position to){
        Piece first = board.getPiece(from);
        Piece second = board.getPiece(to);
        if (first instanceof King king && second instanceof Rook rook
            && king.getFirstMove() && rook.getFirstMove()
            && !collisionExist(from, to) && !playerIsCheck(from)) return true;
        return false;
    }

    private boolean castle(Position from, Position to)
    {
        boolean kingSideCasteling = to.getX() == 7;

        Position futurPosition = new Position(kingSideCasteling ? 6 : 1 , currentPlayer() == PlayerColor.WHITE ? 0 : 7);
        Board simulationBoard = new Board(board);
        simulationBoard.move(from, futurPosition);

        if (playerIsCheck(simulationBoard, futurPosition)){
            return false;
        }
        castling(kingSideCasteling);
        return true;

    }

    private void castling(boolean kingSideCasteling){
        int y = currentPlayer() == PlayerColor.WHITE ? 0 : 7;
        int kingX = kingSideCasteling ? 6 : 1;
        int rookX = kingSideCasteling ? 5 : 2;
        int rookXFrom = kingSideCasteling ? 7 : 0;

        Position kingFrom = new Position(4, y);
        Position kingTo = new Position(kingX, y);
        Position rookFrom = new Position(rookXFrom, y);
        Position rookTo = new Position(rookX, y);
        board.add(kingTo, board.getPiece(kingFrom));
        board.add(rookTo, board.getPiece(rookFrom));
        board.remove(kingFrom);
        board.remove(rookFrom);
    }
}
