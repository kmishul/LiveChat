package com.project.chat_app.server;

import com.project.chat_app.model.UserDetail;
import com.project.chat_app.server.requests.MessagingThread;
import com.project.chat_app.server.requests.UserLoginRequest;
import com.project.chat_app.server.requests.UserSignupRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ClientHandler implements Runnable, Serializable {
    private final Socket client;
    private ObjectOutputStream OOS;
    private ObjectInputStream OIS;
    private UserDetail mainId;

    ClientHandler(Socket client) throws IOException {
        this.client = client;
        OOS = new ObjectOutputStream(client.getOutputStream());
        OIS = new ObjectInputStream(client.getInputStream());
    }

    private UserDetail getUserMainID()  //To get userId Of user
    {
        return this.mainId;
    }

    private void setUserMainID(UserDetail u) //To set userID of user
    {
        this.mainId = u;
    }


    @Override
    public void run() {
        String request="";
        try {
            while ((client.isClosed() == false)) {
                request = (String) OIS.readObject();

                if (request.equals("User SignUp")) {
                    UserDetail user = (UserDetail) OIS.readObject();
                    UserSignupRequest userr = new UserSignupRequest(user);
                    if (userr.adduser()) {
                        OOS.writeObject("valid");
                        OOS.flush();
                        System.out.println("valid check\n");
                    } else {
                        OOS.writeObject("Error: may be username already exist,try another one");
                        OOS.flush();
                    }

                }
                if (request.equals("User Login")) {
                    System.out.println("reached client handler for login");
                    UserDetail userl = (UserDetail) OIS.readObject();

                    UserLoginRequest userlr = new UserLoginRequest(userl);
                    if (userlr.checklogininfo()) {
                        OOS.writeObject("validlogindetails");
                        OOS.flush();
                        this.setUserMainID(userl);
                        System.out.println("Valid User Login");
                    } else {
                        OOS.writeObject("Wrong credentials");
                        OOS.flush();
                    }
                }

                if(request.equals("openchat"))
                {
                    String user=(String) OIS.readObject();
                    String userTo=(String) OIS.readObject();
                    MessagingThread m=new MessagingThread();
                    String chats=m.getChats(user,userTo);
                    OOS.writeObject(chats);
                    OOS.flush();
                }

                if (request.equals("logout")) {
                    client.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}