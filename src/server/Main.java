package server;

import server.database.ConnectSQLiteServer;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new Server();
    }
}
