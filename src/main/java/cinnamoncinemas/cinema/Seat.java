package cinnamoncinemas.cinema;

import cinnamoncinemas.Main;
import cinnamoncinemas.user.User;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class Seat {

    private final int row;
    private final int number;

    private ImageView imageView;

    private User user;

    public static Image VACANT_SEAT = new Image("file:src/main/resources/chair.png");
    public static Image ALLOCATED_SEAT = new Image("file:src/main/resources/chair2.png");

    public Seat(int row, int number) {
        this.row = row;
        this.number = number;
    }

    public User getUser() {return user;}

    public void setImageView(ImageView imageView) {
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.allocate(Main.currentUser);
                Main.currentCinema.checkFull();
            }
            else if (event.getButton() == MouseButton.SECONDARY) {this.deallocate();}
            System.out.println(this.row + "" + this.number);
        });
        this.imageView = imageView;
    }

    public void allocate(User user) {
        this.user = user;
        this.imageView.setImage(ALLOCATED_SEAT);
    }

    private void deallocate() {
        this.user = null;
        this.imageView.setImage(VACANT_SEAT);
    }

}
