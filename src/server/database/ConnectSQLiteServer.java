package server.database;

import server.Const;
import server.File;
import server.SqlQuery;

import java.sql.*;
import java.util.ArrayList;

public class ConnectSQLiteServer implements Const, SqlQuery {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void registration(String name, String surName , String email, String password){
        try{
            PreparedStatement ps = connection.prepareStatement(REGISTRATION);
            ps.setString(2,name);
            ps.setString(3,surName);
            ps.setString(1,email);
            ps.setString(4,password);
            ps.setString(5,"C:\\Android\\server\\storage\\" + email);
            ps.executeUpdate();
            System.out.println("add user");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<File> getFiles(int userId) {
        ArrayList<File> files = null;
        try {
            files = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(SELECT_FILES);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                files.add(new File(userId, rs.getString("file_name"), rs.getString("file_path")));
            }
            for (File file : files) {
                System.out.println(file.getFileName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

}
