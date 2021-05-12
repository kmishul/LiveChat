package com.project.chat_app.server.requests;

import com.project.chat_app.server.DBConnect;
import com.project.chat_app.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


    public class MessagingThread extends Thread {

        private final Connection con=DBConnect.con;
        String user = "";
        String userto="";
        BufferedReader input;
        PrintWriter output;
        public static ArrayList<MessagingThread> clients;


        public MessagingThread() {
        }

        public MessagingThread(Socket client) throws Exception {

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);

            user = input.readLine();
            userto = input.readLine();
            clients= Server.clients;

        }

        public static void sendToAll(String user, String message) {

            for (MessagingThread c : clients) {
                if (!c.getUser().equals(user)) {
                    c.sendMessage(user, message);
                }else{
                    c.sendToMe(message);
                }
            }
        }

        public static void sendToUser(String user, String userTo,String message) {

            for (MessagingThread c : clients) {
                if (c.getUser().equals(userTo)) {
                    c.sendMessage(user, message);
                }else if (c.getUser().equals(user)){
                    c.sendToMe(message);
                }
            }
        }

        public void sendMessage(String chatUser, String msg) {
            output.println(chatUser + ": " + msg);
        }

        public void sendToMe(String msg){
            output.println("You: " + msg);
        }

        public String getUser() {
            return user;
        }

        public void saveInDB(String user, String userTo,String msg) throws SQLException {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO chat_backup VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, userTo);
            preparedStatement.setString(3, msg);
            preparedStatement.setDate(4,new java.sql.Date(System.currentTimeMillis()));
            int rows_affected = preparedStatement.executeUpdate();

            if(rows_affected > 0){
                System.out.println("succesfully inserted the msg in DB");
            }else{
                System.out.println("unable to insert the msg in DB");
            }

        }


        public String getChats(String user,String userTo) throws SQLException {
            String res="";
            if(userTo.equals("group"))
            {
                String q1 = "SELECT * FROM chat_backup WHERE rid=?";
                PreparedStatement stmt = con.prepareStatement(q1);
                stmt.setString(1, userTo);
                ResultSet rs = stmt.executeQuery();
                System.out.println("check execute group\n");
                while (rs.next()) {
                    String sid=rs.getString("sid");
                    String msg=rs.getString("msg");
                    if(sid.equals(user))
                        res=res+"You: " + msg;
                    else
                        res=res+ sid + ": " + msg;
                    res=res+"\n";

                }

            }
            else {
                String q1 = "SELECT * FROM chat_backup WHERE sid=? AND rid=?";
                PreparedStatement stmt = con.prepareStatement(q1);
                stmt.setString(1, user);
                stmt.setString(2, userTo);
                ResultSet rs = stmt.executeQuery();
                System.out.println("check execute individual\n");
                while (rs.next()) {
                    String sid=rs.getString("sid");
                    String msg=rs.getString("msg");
                    if(sid.equals(user))
                        res=res+"You: " + msg;
                    else
                        res=res+ sid + ": " + msg;
                    res=res+"\n";
                    }

            }
                return res;
        }


        @Override
        public void run() {
            String line;
            try {
                while (true) {
                    line = input.readLine();
                    if (line.equals("end")) {
                        clients.remove(this);
                        break;
                    }else {
                        if(userto.equals("group"))
                        sendToAll(user, line);
                        else sendToUser(user,userto,line);
                        saveInDB(user, userto,line);
                    }
                }
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

