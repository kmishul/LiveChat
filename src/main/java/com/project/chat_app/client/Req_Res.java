package com.project.chat_app.client;

import com.project.chat_app.model.UserDetail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Req_Res implements Serializable {
    private final String ip;
    private final int port;
    private final Socket socket;
    private final ObjectOutputStream OOS1;
    private final ObjectInputStream OIS1;
   // private final String user;
    private String s;

    //To get ObjectOutputStream Object
    public ObjectOutputStream getObjectOutputStream() {
        return OOS1;
    }

    //To get ObjectInputStream Object
    public ObjectInputStream getObjectInputStream() {
        return OIS1;
    }

    //To get Socket
    public Socket getSocket() {
        return socket;
    }

    //Constructor --call upon object creation
    public Req_Res() throws IOException {
        this.ip = "localhost";
        this.port = 8086;
        this.socket = new Socket(ip, port);
        this.OOS1 = new ObjectOutputStream(socket.getOutputStream());
        this.OIS1 = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected!");
}

    public String sendUserSignup(UserDetail user) throws IOException, ClassNotFoundException {
        OOS1.writeObject("User SignUp");
        OOS1.writeObject(user);
        OOS1.flush();
        s = (String) OIS1.readObject();
        System.out.println(s);
        return s;
    }

    public String sendUserLogin(UserDetail user) throws IOException, ClassNotFoundException {
        OOS1.writeObject("User Login");
        OOS1.writeObject(user);
        OOS1.flush();
        s = (String) OIS1.readObject();
        return s;
    }

    public void logout() throws IOException{
        OOS1.writeObject("logout");
        socket.close();
        System.out.println("Disconnected");
    }

    public String openChat(String userid, String friend) throws IOException, ClassNotFoundException {
        OOS1.writeObject("openchat");
        OOS1.writeObject(userid);
        OOS1.writeObject(friend);
       s=(String) OIS1.readObject();
       return s;
    }
}
