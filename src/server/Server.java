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

public class Server {
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    public AuthService getAuthService(){
        return authService;
    }
    //private List<ClientHandler> synClients = Collections.synchronizedList(clients);

    public Server() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        ConnectSQLiteServer connectSQLiteServer = null;
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(PORT);
            authService = new BaseAuthService();
            authService.start();
            connectSQLiteServer = new ConnectSQLiteServer();
            //connectDB();
            System.out.println("Server start, wait clients");

            while (true) {
                socket = serverSocket.accept();
                clients.add(new ClientHandler(socket, this));
                System.out.println("Client connected");
            }
        } catch (IOException e) {
//            socket.close();
//            connectSQLiteServer.disconnectDB();
            e.printStackTrace();
        }
    }

    public void unSubscribeClient(ClientHandler c){
        clients.remove(c);
    }

    public boolean isNickBusy(String nick){
        System.out.println(nick);
        for(ClientHandler c: clients){
            if(c.getName().equals(nick)) return true;
        }
        return false;
    }
    public void broadcast(String msg){
        for(ClientHandler c: clients){
            c.sendMessage(msg);
        }
    }
    public void broadcastUsersList(){
        StringBuffer sb = new StringBuffer("/userslist");
        for(ClientHandler c: clients){
            sb.append(" " + c.getName());
        }
        for(ClientHandler c: clients){
            c.sendMessage(sb.toString());
        }
    }
}
