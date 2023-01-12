package chess.test;

import chess.PlayerColor;
import chess.engine.Controller;
import chess.engine.board.Board;
import chess.engine.board.Position;
import chess.engine.piece.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControllerTest extends Controller {

    /**
     * Constructeur du Controller
     *
     * @param board La board que le Controller doit controller
     */
    public ControllerTest(Board board) {
        super(board);
    }

    public void initializeTest(HashMap<Position, Piece> hashMap)
    {
        super.initializeTest(hashMap);
    }

    public boolean checkmateTest(PlayerColor player)
    {
        return super.checkmateTest(player);
    }

    public boolean collisionExistTest(Position from, Position to)
    {
        return super.collisionExistTest(from,to);
    }

    public boolean playerIsCheckTest(Position kingPos)
    {
        return super.playerIsCheckTest(kingPos);
    }

    public boolean priseEnPassantTest(Position from, Position to)
    {
        return super.priseEnPassantTest(from, to);
    }

    public boolean canMoveTest(Position from, Position to)
    {
        return super.canMoveTest(from,to);
    }

    public boolean isPatTest()
    {
        return super.isPatTest();
    }

}
