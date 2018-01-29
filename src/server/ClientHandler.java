package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.socket = socket;
            name = "undefined";
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                //Авторизация
            while(true){
                String msg = in.readUTF();
                if(msg.startsWith("/auth")){
                    String[] elements  = msg.split(" ");
                    String nick = server.getAuthService().getNickByLoginPass(elements[1], elements[2]);
                    System.out.println(nick);
                    if(nick != null){ // если пользователь указал правильные логин/пароль
                        if(!server.isNickBusy(nick)){
                            sendMessage("/authok " + nick);
                            this.name = nick;
                            server.broadcast(this.name + " зашел в чат");
                            break;
                        }else sendMessage("Учетная запись уже используется");
                    }else sendMessage("Не верные логин/пароль");
                }else sendMessage("Для начала надо авторизоваться!");
          } //пока не прервется цикл авторизации, не начнется цикл приема сообщений
                while (true) {
                    String msg = in.readUTF();
                    System.out.println("client: " + msg);
                }
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
