package com.project.chat_app.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class Client extends JFrame implements ActionListener {

    String userid;
    Req_Res res;
    String username;
    PrintWriter pw;
    BufferedReader br;
    JTextArea chatmsg;
    JTextField chatip;
    JButton send, exit;
    Socket chatusers;

    public Client(Req_Res res, String userid,String uname, String servername,String chat_history) throws Exception {
        super(uname);
        this.username = uname;
        this.res=res;
        this.userid=userid;
        chatusers = new Socket(servername, 8082);
        br = new BufferedReader(new InputStreamReader(chatusers.getInputStream()));
        pw = new PrintWriter(chatusers.getOutputStream(), true);
        pw.println(userid);
        pw.println(uname);
        buildInterface();
        chatmsg.append(chat_history + "\n");
        new MessagesThread().start();

    }

    public void buildInterface() throws SQLException {

        send = new JButton("Send");
        exit = new JButton("Exit");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(50);
        chatmsg.setEditable(false);
        chatip = new JTextField(50);
        JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp, "Center");
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(chatip);

        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.LIGHT_GRAY);
        bp.setName("Instant Messenger");
        add(bp, "North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500, 300);
        setVisible(true);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == exit) {
            pw.println("end");
            System.exit(0);
        } else {
            pw.println(chatip.getText());
            chatip.setText(null);
        }
    }

    class MessagesThread extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while (true) {
                    line = br.readLine();
                    chatmsg.append(line + "\n");
                }
            } catch (Exception ex) {
            }
        }
    }
}