package org.niket;

import org.niket.airline.AirlineCheckinSystem;
import org.niket.db.DatabaseConnection;
import org.niket.entities.Seat;
import org.niket.entities.User;

public class Main {
    public static void main(String[] args) {
        try {
            AirlineCheckinSystem airlineCheckinSystem = new AirlineCheckinSystem(DatabaseConnection.getConnection());
            airlineCheckinSystem.reset();
            User user = airlineCheckinSystem.getUser(1);
            Seat seat = airlineCheckinSystem.getSeat(2);
            airlineCheckinSystem.bookSeat(2, 5);
            airlineCheckinSystem.bookSeat(3, 10);
            airlineCheckinSystem.bookSeat(50, 99);
            airlineCheckinSystem.printSeats();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}