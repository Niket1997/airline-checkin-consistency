package org.niket.approach0;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.niket.airline.AirlineCheckinSystem;
import org.niket.db.DatabaseConnection;
import org.niket.entities.Seat;
import org.niket.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {
        try {
            AirlineCheckinSystem airlineCheckinSystem = new AirlineCheckinSystem();
            airlineCheckinSystem.reset();

            Scanner myInput = new Scanner(System.in);

            System.out.print("Please enter the user id: ");
            int userId = myInput.nextInt();
            User user = airlineCheckinSystem.getUser(userId);
            System.out.println();

            System.out.printf("Hello %s, Please enter the seat you want to book: ", user.name());
            int seatId = myInput.nextInt();
            Seat seat = airlineCheckinSystem.getSeat(seatId);
            System.out.println();

            book(seat, user);
            logger.info(String.format("%s booked the seat %s", user.name(), seat.name()));
            airlineCheckinSystem.printSeats();

        } catch (Exception e) {
            logger.info("Exception thrown: " + e.getMessage());
        }
    }

    private static void book(Seat seat, User user) throws Exception {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        connection.createStatement().executeUpdate("BEGIN");

        String getSeatQuery = String.format("SELECT * FROM seats WHERE id = \"%s\";", seat.id());
        ResultSet resultSet = connection.createStatement().executeQuery(getSeatQuery);
        logger.info("transaction got the seat");

        String updateSeatQuery = String.format("UPDATE seats SET user_id = \"%s\" WHERE id = \"%s\";", user.id(), seat.id());
        connection.createStatement().executeUpdate(updateSeatQuery);
        connection.commit();
    }
}
