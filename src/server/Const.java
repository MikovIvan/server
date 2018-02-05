package server;

public interface Const {
    int PORT = 8888;
    String SERVER_IP = "192.168.100.4";
    String SELECT_USER = "SELECT * from users where email=? and password=?";
}
