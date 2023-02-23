package cinnamoncinemas.cinema;

import cinnamoncinemas.Main;
import cinnamoncinemas.user.User;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class Seat {

    private ImageView imageView;

    private User user;

    public static Image VACANT_SEAT = new Image(Main.RESOURCE_STUB + "chair.png");
    public static Image ALLOCATED_SEAT = new Image(Main.RESOURCE_STUB + "chair2.png");

    public Seat() {
    }

    public User getUser() {return user;}

    public void setImageView(ImageView imageView) {
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.allocate(Main.currentUser);
                Main.currentCinema.checkFull();
            }
            else if (event.getButton() == MouseButton.SECONDARY) {this.deallocate();}
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
