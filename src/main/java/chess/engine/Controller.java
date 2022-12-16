package chess.engine;

import chess.ChessController;
import chess.ChessView;
import chess.views.gui.GUIView;
import chess.engine.board.Board;
import chess.engine.board.Position;
import chess.engine.piece.*;

import java.util.Map;


public class Controller implements ChessController {

    private final static int NB_PIECES = 16;
    private ChessView view;
    private final Board board;

    public Controller(Board board){
        if(board == null) {
            throw new RuntimeException("board can't be null");
        }
        this.board = board;
    }

    /**
     * Start the game
     * @param view the view to use to start a game
     * @throws RuntimeException if the view is null
     */
    @Override
    public void start(ChessView view) {
        if (view == null) throw new RuntimeException("The view can't be null");
        this.view = view;
        initialize(view, board);
        view.startView();
    }

    /**
     * Initialize a view and put the pieces at their initial position
     * @param view the view to initialize
     * @param board the board to initialize
     */
    private void initialize(ChessView view, Board board) {
        board.initialize();
        putPieces(view, board);
    }

    private void putPieces(ChessView view, Board board){
        for (Map.Entry<Position, Piece> entry : board.getPieces().entrySet()){
            putPiece(view, entry.getValue(), entry.getKey());
        }
    }

    private void putPiece(ChessView view, Piece piece, Position position){
        view.putPiece(piece.getType(), piece.getColor(), position.getX(),position.getY());
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public void newGame() {
        start(new GUIView(this));
    }
}
