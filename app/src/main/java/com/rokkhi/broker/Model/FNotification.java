package com.rokkhi.broker.Model;

import java.util.Date;

public class FNotification {

    private String n_type="none";
    private String n_sender="none";
    private String n_body="none";
    private String n_pic="none";
    private String n_id="none";
    private Date n_time=new Date();
    private String n_tittle="none";

    public FNotification() {
    }

    public FNotification(String n_type, String n_sender, String n_body, String n_pic, String n_id, Date n_time,String n_tittle) {
        this.n_type = n_type;
        this.n_sender = n_sender;
        this.n_body = n_body;
        this.n_pic = n_pic;
        this.n_id = n_id;
        this.n_time = n_time;
        this.n_tittle=n_tittle;
    }


    public String getN_tittle() {
        return n_tittle;
    }

    public void setN_tittle(String n_tittle) {
        this.n_tittle = n_tittle;
    }

    public String getN_type() {
        return n_type;
    }

    public void setN_type(String n_type) {
        this.n_type = n_type;
    }

    public String getN_sender() {
        return n_sender;
    }

    public void setN_sender(String n_sender) {
        this.n_sender = n_sender;
    }

    public String getN_body() {
        return n_body;
    }

    public void setN_body(String n_body) {
        this.n_body = n_body;
    }

    public String getN_pic() {
        return n_pic;
    }

    public void setN_pic(String n_pic) {
        this.n_pic = n_pic;
    }

    public String getN_id() {
        return n_id;
    }

    public void setN_id(String n_id) {
        this.n_id = n_id;
    }

    public Date getN_time() {
        return n_time;
    }

    public void setN_time(Date n_time) {
        this.n_time = n_time;
    }
}
