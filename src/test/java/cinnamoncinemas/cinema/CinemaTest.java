package cinnamoncinemas.cinema;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CinemaTest {

    @Test
    @Disabled
    public // Non-functional since GUI integration
    void generateSeats() {
        Cinema cinema = new Cinema();
        assertEquals(countSeats(cinema.getSeats()), 15);
    }

    @Test
    public void rowToIndex() {
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