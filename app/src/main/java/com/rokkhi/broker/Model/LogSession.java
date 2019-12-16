package com.rokkhi.broker.Model;

import java.util.Date;

public class LogSession {

    private String id="none";  //userid
    private String user_id="none";
    private String token="none";
    private String which_app="none";
    private Date when=new Date();


    public LogSession(){
    }

    public LogSession(String id, String user_id, String token, String which_app, Date when) {
        this.id = id;
        this.user_id = user_id;
        this.token = token;
        this.which_app = which_app;
        this.when = when;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWhich_app() {
        return which_app;
    }

    public void setWhich_app(String which_app) {
        this.which_app = which_app;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}