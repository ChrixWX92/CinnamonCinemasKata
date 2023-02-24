package cinnamoncinemas.gui;

import cinnamoncinemas.Main;
import cinnamoncinemas.cinema.Seat;
import cinnamoncinemas.cinema.Cinema;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static cinnamoncinemas.Main.RESOURCE_STUB;

public class GUI {

    public static final int WINDOW_X_SIZE = 1024;
    public static final int WINDOW_Y_SIZE = 768;
    public static final int SEAT_ICON_SIZE = 100;
    public static final int PADDING_SIZE = (int) (((1024 / 2) - (SEAT_ICON_SIZE * 1.5)) / 2);

    Stage stage;
    Cinema cinema;

    BorderPane borderPane;
    Slider slider;
    TextField rowInput;
    TextField numberInput;
    Button randomButton;
    Button setButton;
    Button specificButton;

    public static final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {

        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "All seats have been allocated. Exit?");
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Yes");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.orElse(null))) {
            event.consume();
            Platform.exit();
            System.exit(0);
        }

    };

    public GUI(Cinema cinema) {

        this.cinema = cinema;
        this.stage = new Stage();
        this.stage.getIcons().add(new Image(RESOURCE_STUB + "cinnamon.png"));
        this.init();

    }

    public void init() {

        alterStage();

        addSlider();
        configureTextFields();
        configureButtons();
        initBorderPane();
        addPadding();
        addButtonPane();
        addSeatsPane();

        Scene primaryScene = new Scene(borderPane, WINDOW_X_SIZE, WINDOW_Y_SIZE);
        stage.setScene(primaryScene);
        stage.show();

    }

    private void alterStage() {
        //        stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("menuStyling.css")).toExternalForm());
        stage.setTitle("Cinnamon Cinemas Booking System");
        stage.setWidth(WINDOW_X_SIZE);
        stage.setHeight(WINDOW_Y_SIZE);
        stage.setResizable(false);
    }

    private void addSlider() {
        slider = new Slider(1, 3, 0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setStyle("-fx-tick-label-font-size: 12px");
    }

    private void configureTextFields() {
        rowInput = new TextField();
        numberInput = new TextField();
        rowInput.setPromptText("Row");
        numberInput.setPromptText("Number");

        rowInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("([abcABC])") || newValue.length() > 1) {
                ((StringProperty)observable).setValue(oldValue);
            }
        });
        numberInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("([1-5])") || newValue.length() > 1) {
                ((StringProperty)observable).setValue(oldValue);
            }
        });
    }

    private void configureButtons() {

        randomButton = new Button("Randomly Allocate 1-3 Seats");
        setButton = new Button("Allocate Set Amount of Seats:");
        specificButton = new Button("(De)Allocate Specific Seat:");

        randomButton.setOnAction(event -> {
            for (int i = 0 ; i < ThreadLocalRandom.current().nextInt(1, 3 + 1) ; i++) this.cinema.allocateSeat();
        });

        setButton.setOnAction(event -> {
            for (int i = 0 ; i < slider.getValue() ; i++) this.cinema.allocateSeat();
        });

        specificButton.setOnAction(event -> {
            if (rowInput.getText().length() > 0 && numberInput.getText().length() > 0){
                Seat seat = cinema.getSeats().get(
                        (cinema.getSeats().size() - 1) - Cinema.rowToIndex(rowInput.getText().toCharArray()[0])).get(
                        Integer.parseInt(numberInput.getText())-1);
                if (seat.getUser() == null) seat.allocate(Main.currentUser); else seat.deallocate();
                cinema.checkFull();
            }
        });
    }

    private void initBorderPane() {
        borderPane = new BorderPane();
        borderPane = new BorderPane();

        BackgroundImage backgroundImage= new BackgroundImage(new Image(RESOURCE_STUB + "cinema.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        borderPane.setBackground(new Background(backgroundImage));
    }

    private void addPadding() {
        // Top padding
        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(PADDING_SIZE/4F, 0, PADDING_SIZE/4F, 0));
        borderPane.setTop(topHBox);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.getChildren().add(addWelcomeLabel());

        // Bottom padding
        HBox bottomHBox = new HBox();
        bottomHBox.setPadding(new Insets(PADDING_SIZE, 0, 0, 0));
        borderPane.setBottom(bottomHBox);

        // Left padding
        VBox leftVBox = new VBox();
        leftVBox.setPadding(new Insets(0, 0, 0, PADDING_SIZE*.666));
        borderPane.setLeft(leftVBox);
    }

    private Label addWelcomeLabel() {
        Label welcome = new Label("Welcome, " + Main.currentUser.name() + "!\nPlease choose which seats you would like to book:");
        welcome.setAlignment(Pos.CENTER);
        welcome.setTextAlignment(TextAlignment.CENTER);
        welcome.setFont(Font.font("Garamond", FontWeight.BOLD, 35));
        welcome.setTextFill(Color.color(1, 1, 1));
        return welcome;
    }

    private void addButtonPane() {

        VBox sliderVBox = new VBox(setButton, slider);
        sliderVBox.setSpacing(14);

        VBox specifyVBox = new VBox(specificButton, rowInput, numberInput);
        specifyVBox.setAlignment(Pos.CENTER);

        VBox buttons = new VBox(randomButton, sliderVBox, specifyVBox);
        buttons.setPadding(new Insets(0, PADDING_SIZE/4F, 0, 0));
        buttons.setSpacing(40);
        buttons.setAlignment(Pos.CENTER);
        borderPane.setRight(buttons);

    }

    private void addSeatsPane() {

        FlowPane flow = new FlowPane();
        flow.setMaxHeight(SEAT_ICON_SIZE*3);
        flow.setMaxWidth(SEAT_ICON_SIZE*5);
        flow.setMinHeight(SEAT_ICON_SIZE*3);
        flow.setMinWidth(SEAT_ICON_SIZE*5);

        for (List<Seat> row : cinema.getSeats()) {
            for (Seat seat : row) {
                ImageView imageView = new ImageView(new Image(RESOURCE_STUB + "chair.png"));
                imageView.setFitHeight(SEAT_ICON_SIZE);
                imageView.setFitWidth(SEAT_ICON_SIZE);
                seat.setImageView(imageView);
                flow.getChildren().add(imageView);
            }
        }

        borderPane.setCenter(flow);

    }

}

