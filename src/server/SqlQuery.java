package server;

public interface SqlQuery {
    String SELECT_USER = "SELECT * from users where email=? and password=?";
    String REGISTRATION = "INSERT INTO users(email, name, surname, password, folder) VALUES (?, ?, ?, ?, ?)";
    String SELECT_FILES = "SELECT file_name, file_path FROM files WHERE user_id = ?";
}
