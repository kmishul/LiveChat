package com.project.chat_app.server.requests;

import com.project.chat_app.model.UserDetail;
import com.project.chat_app.server.DBConnect;

import java.awt.*;
import java.io.Serializable;
import java.sql.*;
import java.util.Base64;
import java.util.Date;

public class UserSignupRequest implements Serializable {
    private final Connection con;
    private PreparedStatement st;
    private String userid;
    private String name;
    private String pass;
    private Date joiningTime;

    public UserSignupRequest(UserDetail user){
        con = DBConnect.con;
        userid=user.getUserid();
        name =user.getName();
        pass=getEncoded(user.getPass());
        joiningTime=user.getJoiningTime();
    }
    //Method returning true on inserting all details and false if that username already exists
    public boolean adduser(){
        System.out.println("Receiving details");

        try
        {
            if(!checkUsername(userid)){
                st=con.prepareStatement("INSERT INTO users "
                        + "(`userId`, `name`,"
                        + " `pass`, `joiningTime`) VALUES (?,?,?,?)");
                st.setString(1, userid);
                st.setString(2, name);
                st.setString(3, pass);
                //st.setDate(4, new java.sql.Date(joiningTime.getTime()) );
                st.setDate(4,new java.sql.Date(System.currentTimeMillis()));
                st.execute();
                return true;
            }
            else
                return false;
        }
        catch(SQLException e)
        {
            System.out.println("Server Error"+e.getMessage());
            return false;
        }
    }
    private boolean checkUsername(String username){

        boolean username_exist = false;


        try {
            String query = "SELECT * FROM `users` WHERE `userId` = ?";
            st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            System.out.println("checkk\n");

            if(rs.next())
            {
                username_exist = true;

            }

        } catch (HeadlessException | SQLException ex) {
            System.out.println(ex);
        }catch(Exception e){
            System.out.println(e);

        }

        return username_exist;
    }
    private String getEncoded(String valueOf) {

        return Base64.getEncoder().encodeToString(valueOf.getBytes());
    }
}

