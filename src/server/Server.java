package server;

import common.authorization.AuthService;
import common.authorization.BaseAuthService;
import server.database.ConnectSQLiteServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static server.database.ConnectSQLiteServer.connectDB;
import static server.database.ConnectSQLiteServer.disconnectDB;


public class Server implements Const{

    private List<ClientHandler> clients;

    public Server() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        clients = Collections.synchronizedList(new ArrayList<ClientHandler>());
        try {
            serverSocket = new ServerSocket(PORT);
            connectDB();
            System.out.println("Server start, wait clients");

            while (true) {
                socket = serverSocket.accept();
                synchronized (clients){
                    clients.add(new ClientHandler(socket, this));
                }
                System.out.println("Client connected");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                serverSocket.close();
                disconnectDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void unSubscribeClient(ClientHandler c){
        clients.remove(c);
    }

}
