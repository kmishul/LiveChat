package com.project.chat_app.server;


import com.project.chat_app.server.requests.MessagingThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static ArrayList<MessagingThread> clients = new ArrayList<>();
    public static ServerSocket serverSocket;
    public static ServerSocket messageServer;
    public static final int port=8086;
    public static void main(String args[]) throws IOException
    {
        new DBConnect();
        serverSocket=new ServerSocket(port);
        messageServer = new ServerSocket(8082);
        while(true)
        {
            Socket user;
            try
            {
                System.out.println("Waiting For Users To Connect: ");
                user=serverSocket.accept();
                System.out.println("A new User Connected: "+user);
                Thread t=new Thread(new ClientHandler(user));
                t.start();

                Socket client = messageServer.accept();
                MessagingThread thread = new MessagingThread(client);
                clients.add(thread);
                thread.start();
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
