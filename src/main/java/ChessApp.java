import chess.ChessController;
import chess.ChessView;
import chess.views.gui.GUIView;
import chess.engine.Controller;

/**
 * Classe principale du programme
 *
 * @author Anthony David, Alexandre Iorio, Léo Zmoos
 */
public class ChessApp {

    /**
     * Méthode permettant le lancement du programme
     *
     * @param args arguments de main() (pas utilisé dans notre programme)
     */
    public static void main(String[] args) {

        // 1. Création du contrôleur pour gérer le jeu d’échec
        ChessController controller = new Controller(); // Instancier un ChessController

        // 2. Création de la vue
        ChessView view = new GUIView(controller); // mode GUI
        // = new ConsoleView(controller); // mode Console

        // 3. Lancement du programme.
        controller.start(view);
    }
}