package org.niket.approach2;

import org.niket.airline.AirlineCheckinSystem;
import org.niket.db.DatabaseConnection;
import org.niket.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static Connection connection = null;

    public static void main(String[] args) {
        try {
            connection = DatabaseConnection.getConnection();
            AirlineCheckinSystem airlineCheckinSystem = new AirlineCheckinSystem(connection);
            airlineCheckinSystem.reset();

            List<User> users = airlineCheckinSystem.getUsers();
            System.out.printf("simulating seat selection for %d users.%n%n", users.size());

            ExecutorService threadPool = Executors.newFixedThreadPool(users.size());

            for (User user : users) {
                threadPool.submit(() -> {
                    try {
                        book(user);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                });
            }
            System.out.println("waiting on threads to complete.");
            awaitTerminationAfterShutdown(threadPool);
            airlineCheckinSystem.printSeats();
        } catch (Exception e) {
            System.out.println("Exception thrown: " + e.getMessage());
        }
    }

    private static void book(User user) throws SQLException {
        Random random = new Random();
        int randomSeatNumber = 1 + random.nextInt(120);

        connection.createStatement().executeUpdate("BEGIN");
        System.out.println("ahoy");

        String getSeatQuery = String.format("SELECT * FROM seats WHERE id = \"%s\";", randomSeatNumber);
        ResultSet resultSet = connection.createStatement().executeQuery(getSeatQuery);
        if (!resultSet.next()) throw new RuntimeException("seat not found");
        System.out.println("transaction got the seat.");

        String updateSeatQuery = String.format("UPDATE seats SET user_id = \"%s\" WHERE id = \"%s\";", user.id(), randomSeatNumber);
        connection.createStatement().executeUpdate(updateSeatQuery);
        connection.commit();
        System.out.printf("%s booked the seat %s.%n", user.name(), resultSet.getString("name").trim());
    }

    private static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
