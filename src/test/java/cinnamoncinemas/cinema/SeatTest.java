package cinnamoncinemas.cinema;

import cinnamoncinemas.user.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    @Disabled // Non-functional since GUI integration
    void allocationTest() {
        Cinema cinema = new Cinema();
        int initialFreeSeats = countFreeSeats(cinema.getSeats());

        // Choose random seat to allocate
        Seat seat = cinema.getSeats().get(new Random().nextInt(cinema.getSeats().size())).get(new Random().nextInt(cinema.getSeats().size()));
        seat.allocate(new User("Test"));

        assertEquals(countFreeSeats(cinema.getSeats()), initialFreeSeats - 1);
    }

    private int countFreeSeats(List<List<Seat>> seats) {
        int freeSeats = 0;
        for (List<Seat> row : seats) {
            freeSeats += row.stream().filter(e -> e.getUser() != null).toList().size();
        }
        return freeSeats;
    }

}