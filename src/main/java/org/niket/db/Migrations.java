package org.niket.db;

import com.github.javafaker.Faker;
import org.niket.utils.FileLoader;

import java.sql.Connection;
import java.util.List;

public class Migrations {
    private final Faker faker = new Faker();

    private final List<String> seatsPerRow = List.of("A", "B", "C", "D", "E", "F");

    public Migrations() {
    }

    public void reset() throws Exception {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        String queryDropTableSeats = "DROP TABLE IF EXISTS seats;";
        connection.createStatement().executeUpdate(queryDropTableSeats);
        connection.commit();

        String queryDropTableTrips = "DROP TABLE IF EXISTS trips;";
        connection.createStatement().executeUpdate(queryDropTableTrips);
        connection.commit();

        String queryDropTableUsers = "DROP TABLE IF EXISTS users;";
        connection.createStatement().executeUpdate(queryDropTableUsers);
        connection.commit();
        connection.close();

        performMigrations();
    }

    private void performMigrations() throws Exception {
        createTables();
        insertSeats();
        insertTrips();
        insertUsers();
    }

    private void createTables() throws Exception {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        String queryCreateSeatsTable = FileLoader.loadFromFile("src/main/resources/db.migrations/create_table_seats.sql");
        connection.createStatement().executeUpdate(queryCreateSeatsTable);

        String queryCreateTripsTable = FileLoader.loadFromFile("src/main/resources/db.migrations/create_table_trips.sql");
        connection.createStatement().executeUpdate(queryCreateTripsTable);

        String queryCreateUsersTable = FileLoader.loadFromFile("src/main/resources/db.migrations/create_table_users.sql");
        connection.createStatement().executeUpdate(queryCreateUsersTable);
        connection.commit();
        connection.close();
    }

    private void insertSeats() throws Exception {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        int count = 1;
        for (int row = 1; row <= 20; row++) {
            for (String seat : seatsPerRow) {
                String seatNumber = String.format("%d-%s", row, seat);
                String query = String.format("REPLACE INTO seats (id, name, trip_id) VALUES (%d, '%s', %d);", count, seatNumber, 1);
                connection.createStatement().executeUpdate(query);
                count++;
            }
        }
        connection.commit();
        connection.close();
    }

    private void insertTrips() throws Exception {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        String query = String.format("REPLACE INTO trips (id, name) VALUES (%d, '%s');", 1, "INDIGO-101");
        connection.createStatement().executeUpdate(query);
        connection.commit();
        connection.close();
    }

    private void insertUsers() throws Exception {
        Connection connection = DatabaseConnection.getDatabaseConnection();
        for (int userId = 1; userId <= 120; userId++) {
            String name = faker.name().fullName();
            String query = String.format("REPLACE INTO users (id, name) VALUES (%d, \"%s\");", userId, name);
            connection.createStatement().executeUpdate(query);
        }
        connection.commit();
        connection.close();
    }
}
