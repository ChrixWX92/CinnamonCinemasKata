package cinnamoncinemas.cinema;

import cinnamoncinemas.Main;
import cinnamoncinemas.gui.GUI;
import cinnamoncinemas.user.User;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CinemaTest {

    @Test
    @Disabled // Non-functional since GUI integration
    void generateSeats() {
        Cinema cinema = new Cinema();
        assertEquals(countSeats(cinema.getSeats()), 15);
    }

    @Test
    void rowToIndex() {
        int counter = 0;
        for (char row = 'A' ; row <= 'C' ; row++) {
            assertEquals(Cinema.rowToIndex(row), counter);
            counter++;
        }
    }

    private int countSeats(List<List<Seat>> seats) {
        int freeSeats = 0;
        for (List<Seat> row : seats) { freeSeats += row.size(); }
        return freeSeats;
    }

}