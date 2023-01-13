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
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Class permettant de controller une partie d'échec.
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class Controller implements ChessController {
    private ChessView view; // View que Controller doit gérer

    // Tour dans le jeu
    private String message; // Message à afficher
    private Position from; // Position clé-valeur du premier click de l'utilisateur
    private Position to; // Position clé-valeur du second click de l'utilisateur
    private Board board = new Board(); // Échiquier sur lequel on va effectuer la partie
    private Board boardSnapShot = new Board(); // Etat de l'échiquier à un moment donné pour retour à une situation antérieur
    private Piece lastPiece = null;
    private boolean isBlackTurn = false; // Définit si c'est au tour des blancs ou des noirs

    /**
     * Controller() : Constructeur du Controller
     */
    public Controller() {}

    /**
     * playerIsCheck() : Test si un roi est en échec
     * @param positionOfKingToCheck Position du roi qui peut être mise en échec
     * @return vrai si le roi est échec
     */
    private boolean playerIsCheck(Position positionOfKingToCheck){
        return playerIsCheck(board, positionOfKingToCheck);
    }

    /**
     * Utilisé seulement pour les tests
     * @param positionOfKingToCheck Position du roi qui peut être mise en échec
     * @return vrai si le roi est échec
     */
    protected boolean playerIsCheckTest(Position positionOfKingToCheck){
        return playerIsCheck(positionOfKingToCheck);
    }

    /**
     * playerIsCheck() : Test si un roi est en échec
     * @param board Échiquier sur lequel on joue
     * @param positionOfKingToCheck position du roi à contrôler
     * @return vrai si le roi est en échec
     */
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

    /**
     * getKing() : Méthode qui recherche la position du roi d'une couleur donnée sur l'échiquier
     * @param color couleur du roi recherché
     * @return - Entry<Position, Piece> avec la position du roi et pièce de ce dernier.
     *         - null si ne trouve pas le roi
     */
    private Entry<Position, Piece> getKing(PlayerColor color){
        for (Entry<Position, Piece> entry : board.getBoard().entrySet()){
            if (entry.getValue() instanceof King king && king.getColor() == color){
                return entry;
            }
        }
        return null;
    }

    /**
     * isCheck() : Contrôle si un joueur est en échec pour un échiquier donné
     * @param board échiquier sur lequel on joue
     * @param from position de départ du mouvement
     * @param to position d'arrivée du mouvement
     * @return vrai si le jeu est en échec
     */
    private boolean isCheck(Board board, Position from, Position to){

        return (board.getPiece(from).legalMove(from, to) || pawnCanEat(from, to)) && !collisionExist(from, to);
    }

    /**
     * isCheck() : Surcharge de la fonction isCheck
     * @param from position de départ du mouvement
     * @param to position d'arrivée du mouvement
     * @return vrai si le jeu est en échec
     */
    private boolean isCheck(Position from, Position to){
        return isCheck(board, from, to);
    }

    /**
     * checkmate() : Contrôle si un joueur est en échec et mat
     * @param player joueur qu'on vérifie la situation
     * @return vrai si le joueur est en échec et mat
     */
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
            if (!playerIsCheck(board, validPosition)){
                board = new Board(boardSnapShot);
                return false;
            }
            board = new Board(boardSnapShot);
        }
        return true;
    }

    /**
     * checkmateTest() : Utilisé seulement pour les tests
     * @param player: la couleur du joueur
     * @return un booléen
     */
    protected boolean checkmateTest(PlayerColor player)
    {
        return checkmate(player);
    }

    /**
     * start() : Démarre le jeu
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
     * newGame() : Lance une nouvelle partie
     */
    @Override
    public void newGame() {
        if (view == null) throw new RuntimeException("The view can't be null");
        message = "New game, it's up to the white player to play ";
        isBlackTurn = false;
        initialize();
        refreshView();
    }

    /**
     * move() : Methode appelée lors de la demande de déplacement par l'utilisateur
     * @param fromX la coordonnée sur X du premier click
     * @param fromY la coordonnée sur Y du premier click
     * @param toX   la coordonnée sur X du second click
     * @param toY   la coordonnée sur Y du second click
     * @return true si le déplacement a bien été effectué
     */
    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {
        boardSnapShot = new Board(board);

        captureEvent(fromX, fromY, toX, toY);

        if (!to.isValidPosition()){
            message = "Invalid position";
            displayMessage();
            return false;
        }

        if (board.getPiece(from) == null) {
            message = "no pieces are selected";
            displayMessage();
            return false;
        }

        if(canCastle(from, to)){
            if (!castle(from, to)){
                message = "Impossible action, Castling puts the king in check";
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

        if(to.getY() == (isBlackTurn? 0 : 7) && board.getPiece(to).getType() == PieceType.PAWN){
            Promotion();
        }

        if(playerIsCheck(getKing(currentPlayer()).getKey())) {
            message = "Impossible movement - the king is check";
            unDo();
            displayMessage();
            return false;
        }

        if(checkmate(opponentPlayer())) {
            askNewGame();
            return false;
        }

        if(playerIsCheck(getKing(opponentPlayer()).getKey())) {
            message = opponentPlayer().toString() + " player is currently check, move your King! ";
        }
        else {
            message = "it's up to the " + opponentPlayer() + " player to play";
        }


        finishTurn();
        return true;
    }

    /**
     * askNewGame() : Cette méthode est utilisée pour afficher une demande de nouvelle partie
     */
    private void askNewGame(){

        if (view.askUser("Game over", opponentPlayer() + " lose the game\nNew Game ?",
                new ChessView.UserChoice() {
                    @Override
                    public String textValue() {
                        return "Yes";
                    }
                }, new ChessView.UserChoice() {
                    @Override
                    public String textValue() {
                        return "No";
                    }
                }).textValue().equals("Yes")) {
            newGame();
        } else {
            System.exit(0);
        }
    }

    /**
     * priseEnPassant(): Cette méthode est utilisée pour la prise en passant
     * @param from : Position de départ
     * @param to : Position d'arrivée
     * @return un booléen indiquant si l'on la prise en passant a été effectuée
     */
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

    /**
     * finishTurn() : Cette
     */
    private void finishTurn(){
        isBlackTurn = !isBlackTurn;
        lastPiece = board.getPiece(from);
        refreshView();
    }

    /**
     * Promotion() : Cette méthode permet d'effectuer la promotion d'un pion
     */
    private void Promotion() {
        class PieceChoice implements ChessView.UserChoice {
            private final PieceType pieceType;

            PieceChoice(PieceType pieceType){
                this.pieceType = pieceType;
            }

            public PieceType getType() {
                return pieceType;
            }
            @Override
            public String textValue() {
                return pieceType.toString();
            }
        }

        ChessView.UserChoice[] userChoices = new ChessView.UserChoice[]{
                new PieceChoice(PieceType.QUEEN),
                new PieceChoice(PieceType.BISHOP),
                new PieceChoice(PieceType.KNIGHT),
                new PieceChoice(PieceType.ROOK)
        };

        ChessView.UserChoice userChoice = view.askUser("Pawn promotion",
                "Please, choose a piece", userChoices);
        PieceType selectedType = ((PieceChoice) userChoice).getType();
        switch (selectedType){
            case QUEEN -> board.add(to, new Queen(currentPlayer()));
            case BISHOP -> board.add(to, new Bishop(currentPlayer()));
            case KNIGHT -> board.add(to, new Knight(currentPlayer()));
            case ROOK -> board.add(to, new Rook(currentPlayer()));
            default -> throw new RuntimeException("Impossible to promote");
        }
    }

    /**
     * currentPlayer() : Cette méthode permet de connaître la couleur du joueur actuelle
     * @return PlayerColor la couleur du joueur actuel
     */
    private PlayerColor currentPlayer(){
        return isBlackTurn ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    /**
     * opponentPlayer() : Retourne la couleur du joueur adverse
     * @return la couleur du joueur adverse
     */
    private PlayerColor opponentPlayer(){
        return opponentPlayer(currentPlayer());
    }

    /**
     * opponentPlayer() : Cette méthode permet de retourner la couleur adverse en fonction d'une couleur donnée
     * @param player : la couleur
     * @return la couleur opposée
     */
    private PlayerColor opponentPlayer(PlayerColor player){
        return player == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    /**
     * unDo() : Permet d'annuler la dernière action
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
     * isCorrectPlayer() : Définit si la pièce sélectionnée est celle du joueur courant
     * @return true si c'est le cas
     */
    private boolean isCorrectPlayer(Piece selectedPiece) {
        return selectedPiece.getColor()  == (isBlackTurn? PlayerColor.WHITE : PlayerColor.BLACK);
    }

    /**
     * captureEvent() : Capture un événement de déplacement demandé par l'utilisateur et complète les attributs de la classe Controller
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
     * initialize() : Initialisation d'une vue avec les pièces dans leurs positions initiales en les stockant dans une board
     */
    private void initialize() {
        board.initialize();
    }

    /**
     * initializeTest() : Utilisé seulement pour les tests
     * @param hashMap représentation de la map
     */
    protected void initializeTest(HashMap<Position, Piece> hashMap) {
        board = new Board();
       for (Entry<Position, Piece> entry : hashMap.entrySet()){
           board.add(entry.getKey(), entry.getValue());
       }
    }

    /**
     * refreshView() : permet de rafraîchir la vue
     */
    private void refreshView(){
        displayMessage();
        clearView();
        putPieces();
    }

    /**
     * displayMessage() : permet d'afficher un message
     */
    private void displayMessage(){
        displayMessage(message);
    }

    /**
     * displayMessage() : permet d'afficher un message
     * @param message Le message à afficher
     */
    private void displayMessage(String message) {
        view.displayMessage(message);
    }

    /**
     * clearView() : Permet d'effacer les pièces sur les vues
     */
    private void clearView() {
        for (int x = 0; x < Board.SIZE; ++x ){
            for (int y = 0; y < Board.SIZE; ++y){
                view.removePiece(x, y);
            }
        }
    }

    /**
     * canMove() : Définit si la pièce a le droit de bouger
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return vrai si la pièce peut bouger
     */
    private boolean canMove(Position from, Position to){

        if (isCorrectPlayer(board.getPiece(from))){
            message = "It's the other team's turn to play!";
            return false;
        }
        if (pawnCanEat(from, to)) return true;

        if (isLegalMove(from, to)) {
            message = "prohibited movement";
            return false;
        }
        if(isSameColor(to)){
            message = "The destination already has a piece";
            return false;
        }
        if(collisionExist(from, to)){
            message = "There is a collision";
            return false;
        }
        return true;
    }

    /**
     * Utilisé seulement pour les tests
     * @param from position de départ de la pièce
     * @param to position d'arrivée de la pièce
     * @return vrai si la pièce peut se déplacer
     */
    protected boolean canMoveTest(Position from, Position to)
    {
        return canMove(from,to);
    }

    /**
     * Permet de définir si un Pawn peut manger en diagonal
     *
     * @param from la position de départ
     * @param to La position de destination
     * @return vrai si le pion peut manger la pièce
     */
    private boolean pawnCanEat(Position from, Position to) {
        if ((from.getY() - to.getY()) * Move.getCoef(board.getPiece(from).getColor()) > 0) return false;
        if ( board.getPiece(from) instanceof Pawn
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

    /**
     * Contrôle si c'est la même couleur
     * @param to position d'arrivée de la pièce demandée
     * @return vrai si c'est la même couleur
     */
    private boolean isSameColor(Position to){
        Piece piece = board.getPiece(to);
        if (piece == null) {
            return false;
        }
        return board.getPiece(from).getColor() == piece.getColor();
    }

    /**
     * Permet de définir si une pièce est présente sur le chemin et qu'elle crée une collision pour un échiquier donné.
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

    /**
     * Permet de définir si une pièce est présente sur le chemin et qu'elle crée une collision pour l'échiquier en cours.
     * Bloque le pion s'il y a un adversaire devant lui, ignore le cavalier
     * @param from la position de départ
     * @param to La position de destination
     * @return true si il y a une collision
     */
    private boolean collisionExist(Position from, Position to){
        return collisionExist(board, from, to);
    }

    /**
     * Utilisé seulement pour les tests
     * @param from position de départ de la pièce
     * @param to position d'arrivée de la pièce
     * @return vrai s'il existe un risque de collision
     */
    protected boolean collisionExistTest(Position from, Position to)
    {
        return collisionExist(from, to);
    }


    /**
     * putPiece() : Permet de mettre à jour la view avec l'ajout d'une pièce
     * @param entry La clé-valeur comprennent la position ainsi que la pièce à ajouter
     */
    private void putPiece(Entry<Position, Piece> entry) {
        putPiece(entry.getKey(), entry.getValue());
    }

    /**
     * putPiece() : Permet de mettre à jour la view avec l'ajout d'une pièce
     * @param position La position sur laquelle la pièce doit être ajoutée
     * @param piece    La pièce à ajouter
     */
    private void putPiece(Position position, Piece piece) {
        if (piece == null) return;
        view.putPiece(piece.getType(), piece.getColor(), position.getX(), position.getY());
    }

    /**
     * putPieces() : Permet de mettre à jour la view avec les pièces du board
     */
    private void putPieces() {
        for (Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            putPiece(entry.getKey(), entry.getValue());
        }
    }

    /**
     * removePiece() : Permet de mettre à jour la view avec le retrait d'une pièce
     * @param entry La clé-valeur comprennent la position ainsi que la pièce à retirer
     */
    private void removePiece(Entry<Position, Piece> entry) {
        removePiece(entry.getKey());
    }

    /**
     * removePiece() : Permet de mettre à jour la view avec le retrait d'une pièce
     * @param position La position de la pièce à retirer
     */
    private void removePiece(Position position) {
        view.removePiece(position.getX(), position.getY());
    }

    /**
     * canCastle() : Vérifie si les conditions requises au roque sont réunies
     * @param from position de départ du mouvement
     * @param to position d'arrivée demandée par le joueur
     * @return vrai si le roque peut être fait
     */
    public boolean canCastle(Position from, Position to){
        // On doit définit si grand ou petit roque (tour de droite ou de gauche)
        int rookX = (to.getX() < from.getX() ? 0 : 7);
        int rookY = (currentPlayer() == PlayerColor.WHITE? 0 : 7);

        Piece first = board.getPiece(from);
        Position rookPosition = new Position(rookX, rookY); // Trouver la position de la tour
        Piece second = board.getPiece(rookPosition);

        return first instanceof King king && second instanceof Rook rook
                && king.getFirstMove() && rook.getFirstMove()
                && !collisionExist(from, rookPosition) && !playerIsCheck(from);
    }

    /**
     * castle() : Méthode qui fait le roque (voir si petit ou grand et initier le roque avec la méthode castling())
     * @param from position de départ de la pièce
     * @param to position d'arrivée demandée de la pièce
     * @return vrai si le roque peut se faire
     */
    private boolean castle(Position from, Position to)
    {
        // true si petit roque
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

    /**
     * castling() : Méthode qui fait le roque (déplacement des pièces)
     * @param smallCastlingAsked vrai si on demande le petit roque
     */
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

    /**
     * Utilisé seulement pour les tests
     * @param from position de départ
     * @param to position d'arrivée de la pièce
     */
    protected void castlingTest(Position from, Position to)
    {
        castle(from, to);
    }

    /**
     * getActualBoard() : permet de récupérer l'échiquier
     * @return board
     */
    public Board getActualBoard()
    {
        return board;
    }
}
