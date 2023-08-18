package org.niket.approach4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.niket.airline.AirlineCheckinSystem;
import org.niket.db.HikariConnectionPool;
import org.niket.entities.Seat;
import org.niket.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {
        try {
            AirlineCheckinSystem airlineCheckinSystem = new AirlineCheckinSystem();
            airlineCheckinSystem.reset();

            List<User> users = airlineCheckinSystem.getUsers();
            logger.info(String.format("simulating seat selection for %d users.%n%n", users.size()));

            ExecutorService threadPool = Executors.newFixedThreadPool(users.size());
            CountDownLatch latch = new CountDownLatch(users.size());

            long begin = System.currentTimeMillis();
            for (User user : users) {
                threadPool.submit(() -> {
                    try {
                        book(user);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }
            logger.info("waiting on threads to complete.");
            latch.await();
            threadPool.shutdown();
            long end = System.currentTimeMillis();
            System.out.println();
            logger.info(String.format("The seats were booked in %d milliseconds.", end - begin));
            airlineCheckinSystem.printSeats();
        } catch (Exception e) {
            logger.info("Exception thrown: " + e.getMessage());
        }
    }

    private static void book(User user) throws Exception {
        Connection connection = HikariConnectionPool.getConnection();
        connection.setAutoCommit(false);
        String getSeatQuery = "SELECT * FROM seats WHERE user_id IS NULL AND trip_id = 1 ORDER BY id LIMIT 1 FOR UPDATE";
        PreparedStatement getSeatStatement = connection.prepareStatement(getSeatQuery);
        ResultSet resultSet = getSeatStatement.executeQuery();
        if (!resultSet.next()) throw new RuntimeException("seat not found");
        logger.info("transaction got the seat.");
        Seat seat = new Seat(
                resultSet.getInt("id"),
                resultSet.getString("name").trim(),
                resultSet.getInt("trip_id"),
                resultSet.getInt("user_id")
        );

        String updateSeatQuery = String.format("UPDATE seats SET user_id = \"%s\" WHERE id = \"%s\"", user.id(), seat.id());
        connection.prepareStatement(updateSeatQuery).executeUpdate();
        connection.commit();
        logger.info(String.format("%s booked the seat %s.", user.name(), resultSet.getString("name").trim()));
        connection.close();
    }
}
