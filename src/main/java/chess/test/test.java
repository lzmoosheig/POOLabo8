package chess.test;

// Version Junit: 4.13.1

import chess.PlayerColor;
import chess.engine.board.*;
import chess.engine.piece.*;

import org.junit.Test;

import java.util.HashMap;


import static org.junit.Assert.*;

/**
 * Fonction de test des fonctionnalités de l'application
 *
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class test {

    /**
     * Cette fonction permet de tester l'échec et mat
     */
    @Test
    public void testCheckMate() {
        ControllerTest controller = new ControllerTest();
        //Mise en situation avec Anastasia's Mate
        HashMap<Position, Piece> hashMap = new HashMap<>() {{
            put(new Position(4, 6), new Knight(PlayerColor.WHITE));
            put(new Position(6, 6), new Pawn(PlayerColor.BLACK));
            put(new Position(7, 6), new King(PlayerColor.BLACK));
            put(new Position(7, 4), new Rook(PlayerColor.WHITE));
            put(new Position(0, 0), new King(PlayerColor.WHITE));
        }};
        controller.initializeTest(hashMap);

        assertTrue(controller.checkmateTest(PlayerColor.BLACK));
        assertFalse(controller.checkmateTest(PlayerColor.WHITE));

        //Mise en situation avec Anderssen's mate
        hashMap = new HashMap<>() {{
            put(new Position(6, 6), new Pawn(PlayerColor.WHITE));
            put(new Position(6, 7), new King(PlayerColor.BLACK));
            put(new Position(7, 7), new Rook(PlayerColor.WHITE));
            put(new Position(5, 5), new King(PlayerColor.WHITE));
        }};
        controller.initializeTest(hashMap);

        assertTrue(controller.checkmateTest(PlayerColor.BLACK));
        assertFalse(controller.checkmateTest(PlayerColor.WHITE));
    }

    /**
     * Cette fonction permet de tester si le roi est en échec
     */
    @Test
    public void testKingInCheck() {
        ControllerTest controller = new ControllerTest();

        Position kingPos = new Position(4, 4);
        Position queenPos = new Position(7, 7);

        HashMap<Position, Piece> initMap = new HashMap<>() {{
            put(kingPos, new King(PlayerColor.WHITE));
            put(queenPos, new Queen(PlayerColor.BLACK));
        }};

        controller.initializeTest(initMap);

        // Vérifie que le roi est en échec
        assertTrue(controller.playerIsCheckTest(kingPos));
    }

    /**
     * Cette fonction permet de tester le grand roque
     */
    @Test
    public void testBigCastling() {

        ControllerTest controller = new ControllerTest();

        Piece king = new King(PlayerColor.WHITE);
        Piece rook = new Rook(PlayerColor.WHITE);
        Piece pawn = new Pawn(PlayerColor.BLACK);

        Position kingPos = new Position(4, 0);
        Position rookPos = new Position(0, 0);
        Position pawnPos = new Position(7, 6);

        HashMap<Position, Piece> initMap = new HashMap<>() {{
            put(kingPos, king);
            put(rookPos, rook);
            put(pawnPos, pawn);
        }};

        controller.initializeTest(initMap);

        // Vérifie que le roi n'a pas encore bougé
        assertTrue(((King) king).getFirstMove());

        // Vérifie que la tour n'a pas encore bougé
        assertTrue(((Rook) rook).getFirstMove());

        // Vérifie que le grand roque est possible
        assertTrue(controller.canCastle(kingPos, rookPos));

        // Effectue le grand roque
        controller.castlingTest(kingPos, new Position(2, 0));

        // Vérifie que le roi s'est bien déplacé sur la case 2,0
        assertTrue(controller.getActualBoard().getBoard().get(new Position(2, 0)) instanceof King);

        // Vérifie que la tour s'est bien déplacée sur la case 3,0
        assertTrue(controller.getActualBoard().getBoard().get(new Position(3, 0)) instanceof Rook);
    }

    /**
     * Cette fonction permet de tester le petit roque
     */
    @Test
    public void testSmallCastling() {

        ControllerTest controller = new ControllerTest();

        Piece king = new King(PlayerColor.WHITE);
        Piece rook = new Rook(PlayerColor.WHITE);
        Piece pawn = new Pawn(PlayerColor.BLACK);

        Position kingPos = new Position(4, 0);
        Position rookPos = new Position(7, 0);
        Position pawnPos = new Position(7, 6);

        HashMap<Position, Piece> initMap = new HashMap<>() {{
            put(kingPos, king);
            put(rookPos, rook);
            put(pawnPos, pawn);
        }};

        controller.initializeTest(initMap);

        // Vérifie que le roi n'a pas encore bougé
        assertTrue(((King) king).getFirstMove());

        // Vérifie que la tour n'a pas encore bougé
        assertTrue(((Rook) rook).getFirstMove());

        // Vérifie que le grand roque est possible
        assertTrue(controller.canCastle(kingPos, rookPos));

        // Effectue le grand roque
        controller.castlingTest(kingPos, new Position(6, 0));

        // Vérifie que le roi s'est bien déplacé sur la case 6,0
        assertTrue(controller.getActualBoard().getBoard().get(new Position(6, 0)) instanceof King);

        // Vérifie que la tour s'est bien déplacée sur la case 5,0
        assertTrue(controller.getActualBoard().getBoard().get(new Position(5, 0)) instanceof Rook);
    }
}


