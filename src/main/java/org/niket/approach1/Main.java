package org.niket.approach1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.niket.airline.AirlineCheckinSystem;
import org.niket.db.DatabaseConnection;
import org.niket.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Random;
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

            book(user);
            airlineCheckinSystem.printSeats();

        } catch (Exception e) {
            logger.info("Exception thrown: " + e.getMessage());
        }
    }

    private static void book(User user) throws Exception {
        Connection connection = DatabaseConnection.getDatabaseConnection();

        Random random = new Random();
        int randomSeatNumber = 1 + random.nextInt(120);

        String getSeatQuery = String.format("SELECT * FROM seats WHERE id = \"%s\";", randomSeatNumber);
        ResultSet resultSet = connection.createStatement().executeQuery(getSeatQuery);
        if (!resultSet.next()) throw new RuntimeException("seat not found");
        logger.info("transaction got the seat.");

        String updateSeatQuery = String.format("UPDATE seats SET user_id = \"%s\" WHERE id = \"%s\";", user.id(), randomSeatNumber);
        connection.createStatement().executeUpdate(updateSeatQuery);
        connection.commit();
        logger.info(String.format("%s booked the seat %s.", user.name(), resultSet.getString("name").trim()));
    }
}
