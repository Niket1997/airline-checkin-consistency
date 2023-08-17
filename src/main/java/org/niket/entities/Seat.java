package org.niket.entities;

public record Seat(
        int id,
        String name,
        int tripId,
        int userId
) {
}
