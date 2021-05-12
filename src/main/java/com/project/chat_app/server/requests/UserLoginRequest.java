package com.project.chat_app.server.requests;

import com.project.chat_app.model.UserDetail;
import com.project.chat_app.server.DBConnect;

import java.awt.*;
import java.io.Serializable;
import java.sql.*;
import java.util.Base64;

public class UserLoginRequest implements Serializable {
    private final Connection con;
    private PreparedStatement st;
    private String userid, password;
    public UserLoginRequest(UserDetail userl){
        con = DBConnect.con;
        userid=userl.getUserid();
        password=userl.getPass();
    }
    //Method returning true if password entered by user is correct else returns false
    public boolean checklogininfo() {

        try{
            System.out.println("reached checkloginifo");
            System.out.println(userid +password);
            String query="Select pass From users where userId='"+(userid)+"';";
            st = con.prepareStatement(query);


            ResultSet rs=st.executeQuery(query);
            rs.next();
            String actual_password=getDecoded(rs.getString("pass"));
            rs.close();
            if (actual_password.equals(password)){
                return true;
            }
            else{
                return false;
            }

        }catch (HeadlessException | SQLException ex) {
            System.out.println(ex);
            return false;

        }catch(Exception e){
            System.out.println(e);
            return false;

        }

    }
    private static String getDecoded(String hashed){
        return new String(Base64.getMimeDecoder().decode(hashed));
    }
}

