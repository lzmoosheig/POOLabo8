package chess.test;

// Version Junit: 4.13.1

import chess.PlayerColor;
import chess.engine.board.*;
import chess.engine.piece.*;

import org.junit.Test;

import java.util.HashMap;
import static org.junit.Assert.*;

public class MoveTest {

    ControllerTest controller = new ControllerTest();

    /**
     * Ce test permet de vérifier les différents mouvements du pion et s'assurer que les règles des échecs soient respectées
     */
    @Test
    public void testPawnMovement() {
        // Test pour un pion blanc
        Piece pawn = new Pawn(PlayerColor.WHITE);

        // Cas 1 : Vérifie que le pion peut avancer d'une case
        Position from = new Position(3,4);
        Position to = new Position(3,5);
        assertTrue(pawn.legalMove(from, to));

        // Cas 2 : Vérifie que le pion ne peut pas reculer
        to = new Position(3,3);
        assertFalse(pawn.legalMove(from, to));

        // Cas 3 : Vérifie que le pion ne peut pas avancer de plus de deux cases (valable pour les deux couleurs)
        to = new Position(6,7);
        assertFalse(pawn.legalMove(from, to));

        // Test pour un pion noir
        Piece pawnB = new Pawn(PlayerColor.BLACK);

        // Cas 1 : Vérifie que le pion peut avancer d'une case
        from = new Position(3,4);
        to = new Position(3,3);
        assertTrue(pawnB.legalMove(from, to));

        // Cas 2 : Vérifie que le pion ne peut pas reculer
        to = new Position(3,5);
        assertFalse(pawnB.legalMove(from, to));
    }

    /**
     * Ce test permet de vérifier les différents mouvements du roi et s'assurer que les règles des échecs soient respectées
     */
    @Test
    public void testKingMovement(){
        King king = new King(PlayerColor.BLACK);
        Position from = new Position(4,4);

        // Vérifie que le roi peut se déplacer d'une case vers l'avant, arrière, gauche, droite ou en diagonale
        assertTrue(king.legalMove(from, new Position(3,3)));
        assertTrue(king.legalMove(from, new Position(3, 4)));
        assertTrue(king.legalMove(from, new Position(3, 5)));
        assertTrue(king.legalMove(from, new Position(4, 3)));
        assertTrue(king.legalMove(from, new Position(4, 5)));
        assertTrue(king.legalMove(from, new Position(5, 3)));
        assertTrue(king.legalMove(from, new Position(5, 4)));
        assertTrue(king.legalMove(from, new Position(5, 5)));

        // Vérifie que le roi ne peut pas se déplacer de plus d'une case
        assertFalse(king.legalMove(from,new Position(2, 2)));
    }

    /**
     * Ce test permet de vérifier les différents mouvements de la reine et s'assurer que les règles des échecs soient respectées
     */
    @Test
    public void testQueenMovement() {
        Piece queen = new Queen(PlayerColor.BLACK);
        Position from = new Position(3,3);

        HashMap<Position, Piece> map = new HashMap<>();
        map.put(new Position(3,2), new Pawn(PlayerColor.WHITE));
        map.put(from,queen);
        controller.initializeTest(map);


        // Vérifie que la reine peut se déplacer en diagonale
        assertTrue(queen.legalMove(from, new Position(5,5)));
        assertTrue(queen.legalMove(from, new Position(1, 1)));
        assertTrue(queen.legalMove(from, new Position(1, 5)));
        assertTrue(queen.legalMove(from, new Position(5, 1)));

        // Vérifie que la reine peut se déplacer verticalement ou horizontalement
        assertTrue(queen.legalMove(from, new Position(3, 8)));
        assertTrue(queen.legalMove(from, new Position(8, 3)));

        // Vérifie que la reine ne peut pas sauter par-dessus d'autres pièces
        assertTrue(controller.collisionExistTest(from, new Position(3, 0)));
    }

    /**
     * Ce test permet de vérifier les différents mouvements du fou et s'assurer que les règles des échecs soient respectées
     */
    @Test
    public void testBishopMovement() {
        Piece bishop = new Bishop(PlayerColor.BLACK);
        Position from = new Position(4,4);

        HashMap<Position, Piece> map = new HashMap<>();
        map.put(new Position(4,3), new Pawn(PlayerColor.WHITE));
        map.put(from,bishop);
        controller.initializeTest(map);


        // Vérifie que le fou peut se déplacer en diagonale
        assertTrue(bishop.legalMove(from, new Position(2, 2)));
        assertTrue(bishop.legalMove(from, new Position(6, 6)));
        assertTrue(bishop.legalMove(from, new Position(2, 6)));
        assertTrue(bishop.legalMove(from, new Position(6, 2)));

        // Vérifie que le fou ne peut pas se déplacer horizontalement ou verticalement
        assertFalse(bishop.legalMove(from, new Position(4, 6)));
        assertFalse(bishop.legalMove(from, new Position(6, 4)));

        // Vérifie que le fou ne peut pas sauter par-dessus d'autres pièces
        assertTrue(controller.collisionExistTest(from, new Position(4, 0)));
    }

    /**
     * Ce test permet de vérifier les différents mouvements du cavalier et s'assurer que les règles des échecs soient respectées
     */
    @Test
    public void testKnightMovement() {
        Piece knight = new Knight(PlayerColor.BLACK);
        Position from = new Position(3,3);
        // Vérifie que le cavalier peut se déplacer en L
        assertTrue(knight.legalMove(from, new Position(2, 1)));
        assertTrue(knight.legalMove(from, new Position(4, 1)));
        assertTrue(knight.legalMove(from, new Position(4, 5)));
        assertTrue(knight.legalMove(from, new Position(2, 5)));
        assertTrue(knight.legalMove(from, new Position(1, 2)));
        assertTrue(knight.legalMove(from, new Position(5, 2)));
        assertTrue(knight.legalMove(from, new Position(1, 4)));
        assertTrue(knight.legalMove(from, new Position(5, 4)));

        from = new Position(4,4);
        // Vérifie que le cavalier ne peut pas se déplacer horizontalement ou verticalement
        assertFalse(knight.legalMove(from, new Position(4, 6)));
        assertFalse(knight.legalMove(from, new Position(6, 4)));

        // Vérifie que le cavalier ne peut pas se déplacer en diagonale
        assertFalse(knight.legalMove(from, new Position(6, 6)));
    }

    /**
     * Ce test permet de vérifier les différents mouvements de la tour et s'assurer que les règles des échecs soient respectées
     */
    @Test
    public void testRookMovement() {
        Piece rook = new Rook(PlayerColor.BLACK);
        Position from = new Position(4,4);

        HashMap<Position, Piece> map = new HashMap<>();
        map.put(new Position(4,3), new Pawn(PlayerColor.WHITE));
        map.put(from,rook);
        controller.initializeTest(map);

        // Vérifie que la tour peut se déplacer horizontalement ou verticalement
        assertTrue(rook.legalMove(from, new Position(4, 6)));
        assertTrue(rook.legalMove(from, new Position(6, 4)));
        assertTrue(rook.legalMove(from, new Position(4, 2)));
        assertTrue(rook.legalMove(from, new Position(2, 4)));

        // Vérifie que la tour ne peut pas se déplacer en diagonale
        assertFalse(rook.legalMove(from, new Position(6, 6)));
        assertFalse(rook.legalMove(from, new Position(2, 2)));

        // Vérifie que la tour ne peut pas sauter par-dessus d'autres pièces
        assertTrue(controller.collisionExistTest(from, new Position(4, 0)));
    }
}
