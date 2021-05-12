package com.project.chat_app.model;

import java.io.Serializable;
import java.util.Date;

public class UserDetail implements Serializable {

    private String userid;
    private String name;
    private String pass;
    private Date joiningTime;

    public UserDetail() {}

    public UserDetail(String userid, String name, String pass, Date joiningTime) {
        this.userid = userid;
        this.name = name;
        this.pass = pass;
        this.joiningTime = joiningTime;
    }

    public UserDetail(String userid, String pass) {
        this.userid = userid;
        this.pass = pass;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Date getJoiningTime() {
        return joiningTime;
    }

    public void setJoiningTime(Date joiningTime) {
        this.joiningTime = joiningTime;
    }
}
