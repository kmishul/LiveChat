package com.project.chat_app.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public static Connection con;
    public DBConnect()
    {
        try
        {
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_application", "root", "mishul@123");
         }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
