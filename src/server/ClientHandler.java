package server;

import java.io.*;
import java.net.Socket;
import static server.database.ConnectSQLiteServer.login;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(this.socket.getInputStream());
            out= new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                //Авторизация
                while (true) {
                    String message = in.readUTF();
                    if (message.startsWith("/auth")) {
                        String[] elements = message.split(" ");
                        if (login(elements[1], elements[2]).equals(true)) {
                            sendMessage("/authok ");
                        } else {
                            sendMessage("/authfailed ");
                        }
                    }
                    System.out.println("message: " + message);
                    System.out.println("ip: " + socket.getInetAddress());
                }
            }catch (EOFException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                server.unSubscribeClient(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
