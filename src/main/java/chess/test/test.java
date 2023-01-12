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

public class test {

    /*
    * - Mouvement de chacune des pièces
  - Pion (uniquement en avant, première fois de 2 et ensuite de 1)
- Détection de l'échec
- Roque (grand et petit)
  - Egalement si la tour ou le roi on deja bougé avant
- Prise en passant
  - Egalement si le mouvement de 2 etait le dernier
- Promotion
    *
    * */

    @Test
    public void testCheckMate() {
        ControllerTest controller = new ControllerTest();
        //Mise en situation avec Anastasia's Mate
        HashMap<Position, Piece> hashMap = new HashMap<>() {{
            put(new Position(4,6), new Knight(PlayerColor.WHITE));
            put(new Position(6,6), new Pawn(PlayerColor.BLACK));
            put(new Position(7,6), new King(PlayerColor.BLACK));
            put(new Position(7,4), new Rook(PlayerColor.WHITE));
            put(new Position(0,0), new King(PlayerColor.WHITE));
        }};
        controller.initializeTest(hashMap);

        assertTrue(controller.checkmateTest(PlayerColor.BLACK));
        assertFalse(controller.checkmateTest(PlayerColor.WHITE));

        //Mise en situation avec Anderssen's mate
        hashMap = new HashMap<>() {{
            put(new Position(6,6), new Pawn(PlayerColor.WHITE));
            put(new Position(6,7), new King(PlayerColor.BLACK));
            put(new Position(7,7), new Rook(PlayerColor.WHITE));
            put(new Position(5,5), new King(PlayerColor.WHITE));
        }};
        controller.initializeTest(hashMap);

        assertTrue(controller.checkmateTest(PlayerColor.BLACK));
        assertFalse(controller.checkmateTest(PlayerColor.WHITE));
    }



    @Test
    public void testKingInCheck() {
        ControllerTest controller = new ControllerTest(new Board());

        Position kingPos = new Position(4,4);
        Position queenPos = new Position(7,7);

        HashMap<Position, Piece> initMap = new HashMap<>() {{
            put(kingPos, new King(PlayerColor.WHITE));
            put(queenPos, new Queen(PlayerColor.BLACK));
        }};

        controller.initializeTest(initMap);

        // Vérifie que le roi est en échec
        assertTrue(controller.playerIsCheckTest(kingPos));

        // Vérifie que la reine ne peut pas mettre le roi en échec
        //assertFalse(controller.canPutInCheck(board, 4, 4));
    }


    @Test
    public void testPawnEnPassant() {

        // TODO: Vérifier comment on pourrait déplacer une pièce simplement (CE TEST N'EST PAS ENCORE FINI !)
        ControllerTest controller = new ControllerTest(new Board());

        Piece pawn1 = new Pawn(PlayerColor.BLACK);
        Piece pawn2 = new Pawn(PlayerColor.WHITE);

        Position pawn1Pos = new Position(3, 4);
        Position pawn2Pos = new Position(4, 5);

        HashMap<Position, Piece> initMap = new HashMap<>() {{
            put(pawn1Pos, new King(PlayerColor.WHITE));
            put(pawn2Pos, new Queen(PlayerColor.BLACK));
        }};

        controller.initializeTest(initMap);

        // Vérifie que le pion ne peut pas prendre en passant s'il n'a pas avancé de deux cases
        assertFalse(controller.priseEnPassantTest(pawn1Pos, new Position(3, 3)));

        // Fait avancer le pion de deux cases
        controller.move(3, 4, 3, 6);

        // Vérifie que le pion peut prendre en passant
        assertTrue(controller.priseEnPassantTest(pawn1Pos, new Position(4, 5)));

        // Vérifie que la prise en passant ne peut pas être effectuée si le pion a déjà bougé
        assertFalse(controller.priseEnPassantTest(pawn1Pos, new Position(4, 5)));
        controller.move(3, 6, 4, 5);
        assertFalse(controller.priseEnPassantTest(pawn1Pos, new Position(4, 5)));
    }

    @Test
    public void testStaleMate() {

        ControllerTest controller = new ControllerTest(new Board());

        Piece king = new King(PlayerColor.BLACK);
        Piece rook = new Rook(PlayerColor.WHITE);

        Position kingPos = new Position(4, 4);
        Position rookPos = new Position(4, 0);

        HashMap<Position, Piece> initMap = new HashMap<>() {{
            put(kingPos, king);
            put(rookPos, rook);
        }};

        controller.initializeTest(initMap);

        // Vérifie que le roi n'est pas en échec
        assertFalse(controller.playerIsCheckTest(kingPos));

        // Vérifie que le roi ne peut pas bouger
        assertFalse(controller.canMoveTest(new Position(4,4), new Position(3,4)));
        assertFalse(controller.canMoveTest(new Position(4,4), new Position(4,3)));

        // Vérifie que le pat est détecté
        assertTrue(controller.isPatTest());
    }





    //

    //

    //

    //





}
