package cinnamoncinemas;

import cinnamoncinemas.gui.GUI;
import cinnamoncinemas.cinema.Cinema;
import cinnamoncinemas.user.User;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private static Stage stage;
    public static User currentUser;
    public static Cinema currentCinema;

    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        stage.setOnCloseRequest(GUI.confirmCloseEventHandler);
        currentUser = new User(System.getProperty("user.name"));
        currentCinema = new Cinema();
        new GUI(currentCinema);
    }

    public static void exitApplication() {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
