package server.database;

import java.sql.*;

public class ConnectSQLiteServer {
    private static Connection connection;
    private static Statement statement;

    public ConnectSQLiteServer() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:dropbox.db");
            System.out.println("Db connected");
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to setup JDBC Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error");
        }
    }

    public static void connectDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:dropbox.db");
        System.out.println("Db connected");
        statement = connection.createStatement();
    }

    public static void disconnectDB() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void getData() throws SQLException{
//        ResultSet resultSet = statement.executeQuery("SELECT name FROM users WHERE id > 0");
//        while(resultSet.next()) System.out.println(resultSet.getInt(1) + " " + resultSet.getString("name"));
//    }
}
