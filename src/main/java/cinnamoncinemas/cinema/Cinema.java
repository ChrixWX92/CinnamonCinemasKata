package cinnamoncinemas.cinema;

import cinnamoncinemas.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cinema {

    private final List<List<Seat>> seats;

    public Cinema() {
        this.seats = generateSeats();
    }

    private List<List<Seat>> generateSeats() {
        List<List<Seat>> seats = new ArrayList<>();
        for (char row = 'A'; row <= 'C'; row++) {
            List<Seat> rowList = new ArrayList<>();
            for (int number = 0; number < 5; number++) {
                rowList.add(new Seat(row, number));
            }
            seats.add(rowToIndex(row), rowList);
        }
        Collections.reverse(seats);
        return seats;
    }

    public void allocateSeat() {
        for (char row = 'C'; row >= 'A'; row--) {
            for (int number = 0; number < 5; number++) {
                System.out.println(row + "" + number);
                Seat seat = seats.get(rowToIndex(row)).get(number);
                if (seat.getUser() == null) {
                    seat.allocate(Main.currentUser);
                    checkFull();
                    return;
                }
            }
        }
    }

    public void checkFull() {
        for (List<Seat> row : this.seats) {
            for (Seat seat : row) {
                if (seat.getUser() == null) {
                    return;
                }
            }
        }
        Main.exitApplication();
    }

    public static int rowToIndex(char row){return Character.toUpperCase(row) - 65;}


    public List<List<Seat>> getSeats() {return seats;}

}
