package server;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static server.database.ConnectSQLiteServer.getFiles;
import static server.database.ConnectSQLiteServer.login;
import static server.database.ConnectSQLiteServer.registration;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ObjectInputStream inputObj;
    private ObjectOutputStream outputObj;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.socket = socket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
            inputObj = new ObjectInputStream(in);
            outputObj = new ObjectOutputStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                //Авторизация
                while (true) {
                    Object msg = new Object();
                    while (true) {
                        msg = inputObj.readObject();
                        if (msg instanceof String) {
                            String ques = msg.toString();
                            System.out.println(ques);
                            if (ques.startsWith("/auth")) {
                                String[] elements = ques.split(" ");
                                if (login(elements[1], elements[2]).equals("true")) {
                                    System.out.println((login(elements[1], elements[2])));
                                    sendMessage("/authok ");
                                    break;
                                } else {
                                    sendMessage("/authfailed ");
                                }
                            } else if (ques.startsWith("/reg")) {
                                String[] regElements = ques.split(" ");
//                                if(regElements[1] == null || regElements[2] == null){
//                                    sendMessage("/regfailed");
//                                }
                                createFolder(regElements[3]);
                                System.out.println(Arrays.toString(regElements));
                                registration(regElements[1], regElements[2], regElements[3], regElements[4]);
                                sendMessage("/regok");
                                break;

                            } else if (ques.startsWith("/getfiles")) {
                                ArrayList<File> files = getFiles(1);
                                String[] fileNme = new String[files.size()];
                                for (int i = 0; i < fileNme.length; i++) {
                                    fileNme[i] = files.get(i).getFileName();
                                }
                                outputObj.writeObject(fileNme);
                            }
                            System.out.println("message: " + ques);
                            System.out.println("ip: " + socket.getInetAddress());
                        }
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException e) {
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

    public void sendMessage(String msg) {
        try {
            outputObj.writeObject(msg);
            outputObj.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFolder(String s) {
        java.io.File file = new java.io.File(s);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }
}
