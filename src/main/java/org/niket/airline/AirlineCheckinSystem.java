package org.niket.airline;

import org.niket.db.Migrations;
import org.niket.db.Repository;
import org.niket.entities.Seat;
import org.niket.entities.User;

import java.util.List;

public class AirlineCheckinSystem {
    private final Migrations migrations;

    private final Repository repository;

    public AirlineCheckinSystem() {
        migrations = new Migrations();
        repository = new Repository();
    }

    public void reset() throws Exception {
        migrations.reset();
    }

    public User getUser(int userId) throws Exception {
        return repository.getUser(userId);
    }

    public Seat getSeat(int seatId) throws Exception {
        return repository.getSeat(seatId);
    }

    public void bookSeat(int userId, int seatId) throws Exception {
        repository.bookSeat(userId, seatId);
    }

    public void printSeats() throws Exception {
        repository.printSeats();
    }

    public List<User> getUsers() throws Exception {
        return repository.getUsers();
    }
}
