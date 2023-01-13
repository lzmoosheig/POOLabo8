package chess.test;

import chess.PlayerColor;
import chess.engine.Controller;
import chess.engine.board.Position;
import chess.engine.piece.Piece;

import java.util.HashMap;

/**
 * Classe Permettant de tester les fonctionnalités de Controller
 *
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class ControllerTest extends Controller {

    /**
     * Constructeur du Controller
     */
    public ControllerTest() {
        super();
    }

    public void initializeTest(HashMap<Position, Piece> hashMap) {
        super.initializeTest(hashMap);
    }

    public boolean checkmateTest(PlayerColor player) {
        return super.checkmateTest(player);
    }

    public boolean collisionExistTest(Position from, Position to) {
        return super.collisionExistTest(from, to);
    }

    public boolean playerIsCheckTest(Position kingPos) {
        return super.playerIsCheckTest(kingPos);
    }

    public boolean canMoveTest(Position from, Position to) {
        return super.canMoveTest(from, to);
    }

    public void castlingTest(Position from, Position to) {
        super.castlingTest(from, to);
    }
}
