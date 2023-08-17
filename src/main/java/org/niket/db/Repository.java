package org.niket.db;

import org.niket.entities.Seat;
import org.niket.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Repository {
    private final Connection connection;

    public Repository(Connection connection) {
        this.connection = connection;
    }

    public User getUser(int userId) throws SQLException {
        connection.createStatement().executeUpdate("BEGIN");
        String query = String.format("SELECT * FROM users where id = %d;", userId);
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        connection.commit();
        User user = null;
        if (resultSet.next()) {
            user = new User(
                    userId,
                    resultSet.getString("name").trim()
            );
        }
        return user;
    }

    public Seat getSeat(int seatId) throws SQLException {
        connection.createStatement().executeUpdate("BEGIN");
        String query = String.format("SELECT * FROM seats where id = %d;", seatId);
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        connection.commit();
        Seat seat = null;
        if (resultSet.next()) {
            seat = new Seat(
                    seatId,
                    resultSet.getString("name").trim(),
                    resultSet.getInt("trip_id"),
                    resultSet.getInt("user_id")
            );
        }
        return seat;
    }

    public void bookSeat(int userId, int seatId) throws SQLException {
        connection.createStatement().executeUpdate("BEGIN");
        String query = String.format("UPDATE seats SET user_id = %d WHERE id = %d;", userId, seatId);
        connection.createStatement().executeUpdate(query);
        connection.commit();
    }

    public void printSeats() throws SQLException {
        connection.createStatement().executeUpdate("BEGIN");
        String query = "SELECT * FROM seats;";
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        connection.commit();

        String[][] seats = new String[6][20];

        int row = 0;
        int col = 0;
        while (resultSet.next()) {
            if (resultSet.getInt("user_id") == 0) {
                seats[row][col] = "*";
            } else {
                seats[row][col] = "X";
            }
            row++;
            if (row % 6 == 0) {
                col++;
                row = 0;
            }
        }
        System.out.println();
        for (row = 0; row < 6; row++) {
            System.out.println(String.join(" ", seats[row]));
            if (row == 2) System.out.println();
        }
        System.out.println();
    }
}
