package cinnamoncinemas;

import cinnamoncinemas.gui.GUI;
import cinnamoncinemas.theatre.Theatre;
import cinnamoncinemas.user.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    private static Stage stage;
    public static User currentUser;
    public static Theatre currentTheatre;

    private static final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {

        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "All seats have been allocated. Exit?");

        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);

        exitButton.setText("Yes");

        closeConfirmation.setHeaderText("Confirm Exit");

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.setX((screenBounds.getWidth() - 1024) / 2);
        closeConfirmation.setY((screenBounds.getHeight() - 768) / 2); //TODO: Get these figures set

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.orElse(null))) {
            event.consume();
            Platform.exit();
            System.exit(0);
        }
    };

    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        stage.setOnCloseRequest(confirmCloseEventHandler);
        currentUser = new User(System.getProperty("user.name"));
        currentTheatre = new Theatre();
        new GUI(currentTheatre);
    }

    public static void exitApplication() {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
