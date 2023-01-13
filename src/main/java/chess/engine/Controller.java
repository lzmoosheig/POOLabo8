package chess.engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import chess.engine.board.Board;
import chess.engine.board.Position;
import chess.engine.move.Move;
import chess.engine.piece.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Class permettant de controller une partie d'échec.
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
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

    private String message;

    /**
     * Permet de compter le nombre de tours sans mouvement de pion
     */
    private int cptPawn;
    private Position from;
    /**
     * Lors d'un événement, stock la position clé-valeur du second click de l'utilisateur
     */
    private Position to;

    /**
     * Stock la board que le Controlleur doit controller
     */
    private Board board = new Board();

    /**
     * Stock un snapshot de la board afin de rétablir un état antérieur
     */
    private Board boardSnapShot = new Board();

    private Piece lastPiece = null;

    /**
     * Définit si c'est le tour de blanc
     */
    private boolean isBlackTurn = false;

    /**
     * Constructeur du Controller
     *
     */
    public Controller() {}

    private boolean playerIsCheck(Position positionOfKingToCheck){
        return playerIsCheck(board, positionOfKingToCheck);
    }

    protected boolean playerIsCheckTest(Position positionOfKingToCheck) {
        return playerIsCheck(positionOfKingToCheck);
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

    private boolean isCheck(Board board, Position from, Position to){

        return (board.getPiece(from).legalMove(from, to) || pawnCanEat(from, to)) && !collisionExist(from, to);
    }

    private boolean isCheck(Position from, Position to){
        return isCheck(board, from, to);
    }

    protected boolean isCheckTest(Position from, Position to)
    {
        return isCheck(from,to);
    }

    private boolean checkmate(PlayerColor player){
        Position kingPosition = getKing(player).getKey();

        //sequences de directions
        int[] sequencesX = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] sequencesY = {-1, 0, 1, -1, 1, -1, 0, 1};

        ArrayList<Position> validPositions = new ArrayList<>();
        boardSnapShot = new Board(board);

        for (int i = 0; i < 8; i++) {
            int futurX = kingPosition.getX() + sequencesX[i];
            int futurY = kingPosition.getY() + sequencesY[i];
            Position futurPosition = new Position(futurX, futurY);
            Piece pieceOnFuturPosition = board.getPiece(futurPosition);

            if (futurPosition.isValidPosition() &&
                    (pieceOnFuturPosition == null || pieceOnFuturPosition.getColor() != player)){
                board.move(kingPosition, futurPosition);
                validPositions.add(futurPosition);
            }
            board = new Board(boardSnapShot);
        }

        if (validPositions.size() == 0) {
            return false;
        }

        board = new Board(boardSnapShot);

        for (Position validPosition : validPositions){
            board.move(kingPosition, validPosition);
//            for (Entry<Position, Piece> entry : simulationBoard.getBoard().entrySet()){
//                Piece attacant = entry.getValue();
//                if (!attacant.legalMove(entry.getKey(), validPosition)
//                        || collisionExist(entry.getKey(), validPosition)){
//                    return false;
//                }
//            }
            if (!playerIsCheck(board, validPosition)){
                board = new Board(boardSnapShot);
                return false;
            }
            board = new Board(boardSnapShot);
        }
        return true;
    }

    protected boolean checkmateTest(PlayerColor player){

        return checkmate(player);
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
        refreshView();
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

        //if (isPat())

        if (!to.isValidPosition()){
            message = "Position invalide";
            displayMessage();
            return false;
        }

        if (from == null) {
            message = "Aucune pièce seléctionnée";
            displayMessage();
            return false;
        }

        if(canCastle(from, to)){
            if (!castle(from, to)){
                message = "Action impossible, Le roque se met le roi en échec";
                displayMessage();
                return false;
            }
            finishTurn();
            return true;
        }

        if (priseEnPassant(from, to)) {
            finishTurn();
            return true;
        }

        if (!canMove(from, to)){
            displayMessage();
            return false;
        }


        if(board.getPiece(from) instanceof PieceExtend pieceExtend){
            pieceExtend.setFirstMove();
        }

        board.move(from, to);

        if(to.getY() == (isBlackTurn? 0 : 7) && board.getPiece(from).getType() == PieceType.PAWN){
            Promotion();
        }

        if(playerIsCheck(getKing(currentPlayer()).getKey())) {
            message = "Mouvement impossible - Echec";
            unDo();
            displayMessage();
            return false;
        }

        if(checkmate(opponentPlayer())) {
            message = "Player "+ opponentPlayer() + " lose";
            finishTurn();
            return true;
        }

        if(playerIsCheck(getKing(opponentPlayer()).getKey())) {
            message = opponentPlayer().toString() + " player is currently check!";
        }


        finishTurn();
        return true;
    }

    private boolean isPat() {
        Position positions[] = new Position[board.SIZE * board.SIZE];
        for (int i = 0; i < board.SIZE; ++i) {
            for (int j = 0; j < board.SIZE; ++j) {
                int index = i * board.SIZE + j;
                positions[index] = new Position(i, j);
            }
        }

        for (Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            Piece piece = entry.getValue();
            Position initialPosition = entry.getKey();
            for (Position position : positions) {
                Entry<Position, Piece> toEntry = new AbstractMap.SimpleEntry<>(position, board.getPiece(position));
                if (canMove(entry.getKey(), toEntry.getKey())) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean isPatTest()
    {
        return isPat();
    }

    private boolean priseEnPassant(Position from, Position to) {
        if (currentPlayer() != board.getPiece(from).getColor()) return false;
        int coef = Move.getCoef(currentPlayer());
        int epX = to.getX();
        int epY = to.getY() + coef;
        Position epPosition = new Position(epX, epY);
        if (board.getPiece(epPosition) instanceof Pawn epPawn
                && lastPiece == epPawn
                && epPawn.getColor() == opponentPlayer()){
            board.remove(epPosition);
            board.move(from, to);
            return true;
        }
        return false;
    }

    protected boolean priseEnPassantTest(Position from, Position to)
    {
        return priseEnPassant(from, to);
    }

    private void finishTurn(){
        turn++;
        isBlackTurn = !isBlackTurn;
        lastPiece = board.getPiece(from);
        refreshView();
    }

    private void Promotion() {

        ChessView.UserChoice[] userChoices = new ChessView.UserChoice[]{
                new Queen(currentPlayer()),
                new Bishop(currentPlayer()),
                new Rook(currentPlayer()),
                new Knight(currentPlayer())};

        ChessView.UserChoice userChoice = view.askUser("Pawn promotion",
                "Please, choose a piece", userChoices);

        if (userChoice != null) {
            board.add(to, (Piece) userChoice);
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
    private boolean isCorrectPlayer(Piece selectedPiece) {
        return selectedPiece.getColor()  == (isBlackTurn? PlayerColor.WHITE : PlayerColor.BLACK);
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
        from = new Position(fromX, fromY);
        to = new Position(toX, toY);
    }

    /**
     * Initialisation d'une vue avec les pièces dans leurs positions initiales en les stockant dans une board
     *
     */
    private void initialize() {
        board.initialize();
    }

    protected void initializeTest(HashMap<Position, Piece> hashMap) {
        board = new Board();
       for (Entry<Position, Piece> entry : hashMap.entrySet()){
           board.add(entry.getKey(), entry.getValue());
       }
    }
    private void refreshView(){
        displayMessage();
        clearView();
        putPieces();
    }

    private void displayMessage(){
        displayMessage(message);
    }
    private void displayMessage(String message) {
        view.displayMessage(message);
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
    private boolean canMove(Position from, Position to){

        if (isCorrectPlayer(board.getPiece(from))){
            message = "C'est à l'équipe adverse de jouer !";
            return false;
        }
        if (pawnCanEat(from, to)) return true;

        if (isLegalMove(from, to)) {
            message = "Mouvement interdit";
            return false;
        }
        if(isSameColor(to)){
            message = "La destination possède déjà une pièce";
            return false;
        }

        if(collisionExist(from, to)){
            message = "Il y a une collision";
            return false;
        }
        return true;
    }

    protected boolean canMoveTest(Position from, Position to)
    {
        return canMove(from,to);
    }






    /**
     * Permet de définir si un Pawn peut manger en diagonal
     *
     * @param from la position de départ
     * @param to La position de destination
     * @return
     */

    private boolean pawnCanEat(Position from, Position to) {
        if ((from.getY() - to.getY()) * Move.getCoef(board.getPiece(from).getColor()) > 0) return false;
        if ( board.getPiece(from) instanceof Pawn pawn
                && board.getPiece(to) != null
                && Move.isDiagonal(from, to)
                && Move.getDistance(from, to) == 1 ){

            return  board.getPiece(to).getColor() != board.getPiece(from).getColor();
        }
        return false;
    }

    /**
     * Permet de définir si le mouvement est legal respectivement à la règle de déplacement de la pièce sélectionnée
     * @return True si le mouvement est légal
     */
    private boolean isLegalMove(Position from, Position destination) {
        return !board.getPiece(from).legalMove(from, destination);
    }

    private boolean isSameColor(Position to){
        Piece piece = board.getPiece(to);
        if (piece == null) {
            return false;
        }
        return board.getPiece(from).getColor() == piece.getColor();
    }
    /**
     * Permet de définir si la destination possède est occupée par le même joueur
     * @return True si le la destination est de la même couleur
     */
    private boolean isSameColor() {
       return isSameColor(to);
    }

    /**
     * Permet de définir si une pièce est présente sur le chemin et qu'elle crée une collision.
     * Bloque le pion s'il y a un adversaire devant lui, ignore le cavalier
     * @param from la position de départ
     * @param to La position de destination
     * @param board La board sur laquelle il faut tester la collision
     * @return true si il y a une collision
     */
    private boolean collisionExist(Board board, Position from, Position to) {
        if (board.getPiece(from).getType() == PieceType.KNIGHT) return false;
        if (board.getPiece(from) instanceof Pawn pawn
                && pawn.moveAhead(from, to)
                && board.getPiece(to) != null) return true;

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

    private boolean collisionExist(Position from, Position to){
        return collisionExist(board, from, to);
    }

    protected boolean collisionExistTest(Position from, Position to)
    {
        return collisionExist(from, to);
    }

    /**
     * Permet de définir si une pièce est présente sur le chemin et qu'elle crée une collision
     * @return true si il y a une collision
     */
    private boolean collisionExist(){
        return collisionExist(from, to);
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
    //TODO Attention au roque pas kingside mettre la position juste selon les regles
    //TODO attention au roque quand c'est pas ton tour
    //TODO checker les echec sur toutes les case du roque pas seuleument sur le roi
    public boolean canCastle(Position from, Position to){
        // On doit définit si grand ou petit roque (tour de droite ou de gauche)
        int rookX = (to.getX() < from.getX() ? 0 : 7);
        int rookY = (currentPlayer() == PlayerColor.WHITE? 0 : 7);

        Piece first = board.getPiece(from);
        Position rookPosition = new Position(rookX, rookY); // Trouver la position de la tour
        Piece second = board.getPiece(rookPosition);

        if (first instanceof King king && second instanceof Rook rook
            && king.getFirstMove() && rook.getFirstMove()
            && !collisionExist(from, rookPosition) && !playerIsCheck(from)) return true;
        return false;
    }

    private boolean castle(Position from, Position to)
    {
        // true si petit
        boolean smallCastlingAsked = to.getX() == 6;

        Position futurPosition = new Position(smallCastlingAsked ? 6 : 2 , currentPlayer() == PlayerColor.WHITE ? 0 : 7);
        Board simulationBoard = new Board(board);
        simulationBoard.move(from, futurPosition);

        if (playerIsCheck(simulationBoard, futurPosition)){
            return false;
        }
        castling(smallCastlingAsked);
        return true;

    }

    private void castling(boolean smallCastlingAsked){
        int y = currentPlayer() == PlayerColor.WHITE ? 0 : 7;
        int kingX = smallCastlingAsked ? 6 : 2;
        int rookX = smallCastlingAsked ? 5 : 3;
        int rookXFrom = smallCastlingAsked ? 7 : 0;

        Position kingFrom = new Position(4, y);
        Position kingTo = new Position(kingX, y);
        Position rookFrom = new Position(rookXFrom, y);
        Position rookTo = new Position(rookX, y);
        board.add(kingTo, board.getPiece(kingFrom));
        board.add(rookTo, board.getPiece(rookFrom));
        board.remove(kingFrom);
        board.remove(rookFrom);
    }

    public int getTurn()
    {
        return turn;
    }
}
