package cinnamoncinemas.gui;

import cinnamoncinemas.Main;
import cinnamoncinemas.theatre.Seat;
import cinnamoncinemas.theatre.Theatre;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GUI {

    public static final int SEAT_ICON_SIZE = 100;
    public static final int PADDING_SIZE = (int) (((1024 / 2) - (SEAT_ICON_SIZE * 1.5)) / 2);
    Theatre theatre;

    public GUI(Theatre theatre) {

        this.theatre = theatre;
        this.init(new Stage());

    }

    public void init(Stage stage) {

//        stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("menuStyling.css")).toExternalForm());
        stage.setTitle("Cinnamon Cinemas Booking System");
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(false);
        Button randomButton = new Button("Randomly Allocate 1-3 Seats");
        Button setButton = new Button("Allocate Set Amount of Seats:");
        Slider slider = new Slider(1, 3, 0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setStyle("-fx-tick-label-font-size: 12px");
        VBox sliderVBox =  new VBox(setButton, slider);
        sliderVBox.setSpacing(14);
        Button specificButton = new Button("Allocate Specific Seat:");
        TextField rowInput = new TextField();
        TextField numberInput = new TextField();
        rowInput.setPromptText("Row");
        numberInput.setPromptText("Number");
        VBox specifyVBox =  new VBox(specificButton, rowInput, numberInput);
        specifyVBox.setAlignment(Pos.CENTER);

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

        // Parent pane
        BorderPane borderPane = new BorderPane();

        BackgroundImage myBI= new BackgroundImage(new Image("file:src/main/resources/cinema.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        borderPane.setBackground(new Background(myBI));

        // Top padding
        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(PADDING_SIZE/4F, 0, PADDING_SIZE/4F, 0));
        borderPane.setTop(topHBox);
        Label welcome = new Label("Welcome, " + Main.currentUser.name() + "!\nPlease choose which seats you would like to book:");
        welcome.setAlignment(Pos.CENTER);
        welcome.setTextAlignment(TextAlignment.CENTER);
        welcome.setFont(Font.font("Garamond", FontWeight.BOLD, 35));
        welcome.setTextFill(Color.color(1, 1, 1));
        topHBox.setAlignment(Pos.CENTER);
        topHBox.getChildren().add(welcome);

        // Bottom padding
        HBox bottomHBox = new HBox();
        bottomHBox.setPadding(new Insets(PADDING_SIZE, 0, 0, 0));
        borderPane.setBottom(bottomHBox);

        // Left padding
        VBox leftVBox = new VBox();
        leftVBox.setPadding(new Insets(0, 0, 0, PADDING_SIZE*.666));
        borderPane.setLeft(leftVBox);

        // Button pane
        VBox buttons = new VBox(randomButton, sliderVBox, specifyVBox);
        buttons.setPadding(new Insets(0, PADDING_SIZE/4F, 0, 0));
        buttons.setSpacing(40);
        buttons.setAlignment(Pos.CENTER);
        borderPane.setRight(buttons);

        // Seats pane
        FlowPane seatsPane = addSeatsPane();
        borderPane.setCenter(seatsPane);
        randomButton.setOnAction(event -> {
            for (int i = 0 ; i < ThreadLocalRandom.current().nextInt(1, 3 + 1) ; i++) {
                this.theatre.allocateSeat();
                System.out.println(i);
            }
        });

        setButton.setOnAction(event -> {
            for (int i = 0 ; i < slider.getValue() ; i++) this.theatre.allocateSeat();
        });

        specificButton.setOnAction(event -> {
            if (rowInput.getText().length() > 0 && numberInput.getText().length() > 0){
                theatre.seats.get(
                    (theatre.seats.size() - 1) - Theatre.rowToIndex(rowInput.getText().toCharArray()[0])).get(
                            Integer.parseInt(numberInput.getText())-1)
                                .allocate(Main.currentUser);
                theatre.checkFull();
            }
        });
        Scene primaryScene = new Scene(borderPane, 1024, 768);
        stage.setScene(primaryScene);
        stage.show();
    }

    private FlowPane addSeatsPane() {

        FlowPane flow = new FlowPane();
        flow.setMaxHeight(SEAT_ICON_SIZE*3);
        flow.setMaxWidth(SEAT_ICON_SIZE*5);
        flow.setMinHeight(SEAT_ICON_SIZE*3);
        flow.setMinWidth(SEAT_ICON_SIZE*5);

        for (List<Seat> row : theatre.seats) {
            for (Seat seat : row) {
                ImageView imageView = new ImageView(new Image("file:src/main/resources/chair.png"));
                imageView.setFitHeight(SEAT_ICON_SIZE);
                imageView.setFitWidth(SEAT_ICON_SIZE);
                seat.setImageView(imageView);
                flow.getChildren().add(imageView);
            }
        }

        return flow;

    }

//    public static void showExitDialog(final Stage primaryStage) {
//        Button btn = new Button();
//        btn.setText("Open Dialog");
//        btn.setOnAction(event -> {
//            final Stage dialog = new Stage();
//            dialog.initModality(Modality.APPLICATION_MODAL);
//            dialog.initOwner(primaryStage);
//            VBox dialogVbox = new VBox(20);
//            dialogVbox.getChildren().add(new Text("This is a Dialog"));
//            Scene dialogScene = new Scene(dialogVbox, 300, 200);
//            dialog.setScene(dialogScene);
//            dialog.show();
//        });
//    }


}

