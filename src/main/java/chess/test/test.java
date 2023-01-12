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

    // Used for all test
    ControllerTest controller = new ControllerTest(new Board());

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
        ControllerTest controller = new ControllerTest(new Board());

        HashMap<Position, Piece> Anastasia = new HashMap<>() {{
            put(new Position(4,6), new Knight(PlayerColor.WHITE));
            put(new Position(6,6), new Pawn(PlayerColor.BLACK));
            put(new Position(7,6), new King(PlayerColor.BLACK));
            put(new Position(7,4), new Rook(PlayerColor.WHITE));
            put(new Position(0,0), new King(PlayerColor.WHITE));
        }};

        controller.initializeTest(Anastasia);

        assertTrue(controller.checkmateTest(PlayerColor.BLACK));
        assertFalse(controller.checkmateTest(PlayerColor.WHITE));
    }





    //

    //

    //

    //





}
