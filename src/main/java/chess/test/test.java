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
        ControllerTest controller = new ControllerTest(new Board());

        HashMap<Position, Piece> map = new HashMap<>();
        Piece Knight = new Knight(PlayerColor.WHITE);
        Piece KingB = new King(PlayerColor.BLACK);
        Piece KingW = new King(PlayerColor.WHITE);
        Piece Rook = new Rook(PlayerColor.WHITE);

        map.put(new Position(5,5), Knight);
        map.put(new Position(7,7), KingB);
        map.put(new Position(0,0), KingW);
        map.put(new Position(7,6), Rook);

        controller.initializeTest(map);

        assertTrue(controller.checkmateTest(PlayerColor.BLACK));
    }



    //

    //

    //

    //





}
