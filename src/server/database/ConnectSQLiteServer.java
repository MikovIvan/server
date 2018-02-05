package server.database;

import server.Const;

import java.sql.*;

public class ConnectSQLiteServer implements Const {
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
    }

    public static void disconnectDB() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String login(String email, String password){
        String result="false";
        try {
            PreparedStatement ps = connection.prepareStatement(SELECT_USER);
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                result = "true";
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
