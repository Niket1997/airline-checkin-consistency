package org.niket;

import org.niket.db.DatabaseConnection;
import org.niket.db.Migrations;

public class Main {
    public static void main(String[] args) {
        try {
            Migrations migrations = new Migrations(DatabaseConnection.getConnection());
            migrations.performMigrations();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}