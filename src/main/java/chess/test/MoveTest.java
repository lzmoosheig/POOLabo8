package chess.test;

// Version Junit: 4.13.1

import chess.ChessController;
import chess.PlayerColor;
import chess.engine.Controller;
import chess.engine.board.*;
import chess.engine.piece.*;
import chess.engine.move.*;
import chess.engine.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MoveTest {

    ControllerTest controller = new ControllerTest(new Board());

    @Test
    public void testPawnMovement() {

        HashMap<Position, Piece> map = new HashMap<>();
        map.put(new Position(0,0), new King(PlayerColor.WHITE));
        controller.initializeTest(map);

        Piece pawn = new Pawn(PlayerColor.BLACK);
        // Cas 1
        Position from = new Position(3,4);
        Position to = new Position(3,5);

        // Cas 1 : Vérifie que le pion peut avancer d'une case
        assertTrue(controller.isLegalMoveTest(from, to));

        // Cas 2 : Vérifie que le pion ne peut pas reculer
        to = new Position(3,3);
        assertFalse(controller.isLegalMoveTest(from, to));

        // Cas 3 : Vérifie que le pion ne peut pas avancer de plus de deux cases
        to = new Position(6,7);
        assertFalse(controller.isLegalMoveTest(from, to));
    }

    /**
     * Test to verify the King moves properly
     */
    @Test
    public void testKingMovement(){
        King king = new King(PlayerColor.BLACK);
        // Vérifie que le roi peut se déplacer d'une case vers l'avant, arrière, gauche, droite ou en diagonale
        Position from = new Position(4,4);

        // Tout les mouvements valides
        assertTrue(controller.isLegalMoveTest(from, new Position(3,3)));
        assertTrue(controller.isLegalMoveTest(from, new Position(3, 4)));
        assertTrue(controller.isLegalMoveTest(from, new Position(3, 5)));
        assertTrue(controller.isLegalMoveTest(from, new Position(4, 3)));
        assertTrue(controller.isLegalMoveTest(from, new Position(4, 5)));
        assertTrue(controller.isLegalMoveTest(from, new Position(5, 3)));
        assertTrue(controller.isLegalMoveTest(from, new Position(5, 4)));
        assertTrue(controller.isLegalMoveTest(from, new Position(5, 5)));

        // Vérifie que le roi ne peut pas se déplacer de plus d'une case
        assertFalse(controller.isLegalMoveTest(from,new Position(2, 2)));
    }

    @Test
    public void testQueenMovement() {
        Piece queen = new Queen(PlayerColor.BLACK);

        Position from = new Position(3,3);

        // Vérifie que la reine peut se déplacer en diagonale
        assertTrue(controller.isLegalMoveTest(from, new Position(5,5)));
        assertTrue(controller.isLegalMoveTest(from, new Position(1, 1)));
        assertTrue(controller.isLegalMoveTest(from, new Position(1, 5)));
        assertTrue(controller.isLegalMoveTest(from, new Position(5, 1)));

        // Vérifie que la reine peut se déplacer verticalement ou horizontalement
        assertTrue(controller.isLegalMoveTest(from, new Position(3, 8)));
        assertTrue(controller.isLegalMoveTest(from, new Position(8, 3)));

        // Vérifie que la reine ne peut pas sauter par-dessus d'autres pièces
        assertFalse(controller.isLegalMoveTest(from, new Position(8, 8)));
    }

    @Test
    public void testBishopMovement() {
        Piece bishop = new Bishop(PlayerColor.BLACK);
        Position from = new Position(4,4);
        // Vérifie que le fou peut se déplacer en diagonale
        assertTrue(controller.isLegalMoveTest(from, new Position(2, 2)));
        assertTrue(controller.isLegalMoveTest(from, new Position(6, 6)));
        assertTrue(controller.isLegalMoveTest(from, new Position(2, 6)));
        assertTrue(controller.isLegalMoveTest(from, new Position(6, 2)));

        // Vérifie que le fou ne peut pas se déplacer horizontalement ou verticalement
        assertFalse(controller.isLegalMoveTest(from, new Position(4, 6)));
        assertFalse(controller.isLegalMoveTest(from, new Position(6, 4)));

        // Vérifie que le fou ne peut pas sauter par-dessus d'autres pièces
        assertFalse(controller.isLegalMoveTest(from, new Position(8, 8)));
    }



}
