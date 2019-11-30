package com.rokkhi.brokers.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FWorkers {

    private String user_id="none";
    private String fw_nid="none";
    private String fw_phone="none";
    private String fw_university="none";
    private String fw_address="none";
    private Date created_at=new Date();
    private Date updated_at=new Date();
    private boolean trained=false;
    private List<String> u_array= new ArrayList<>();
    private List<String> atoken= new ArrayList<>();
    private List<String> itoken= new ArrayList<>();


    public FWorkers() {

    }

    public FWorkers(String user_id, String fw_nid, String fw_phone, String fw_university, String fw_address, Date created_at, Date updated_at, boolean trained, List<String> u_array, List<String> atoken, List<String> itoken) {
        this.user_id = user_id;
        this.fw_nid = fw_nid;
        this.fw_phone = fw_phone;
        this.fw_university = fw_university;
        this.fw_address = fw_address;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.trained = trained;
        this.u_array = u_array;
        this.atoken = atoken;
        this.itoken = itoken;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFw_nid() {
        return fw_nid;
    }

    public void setFw_nid(String fw_nid) {
        this.fw_nid = fw_nid;
    }

    public String getFw_phone() {
        return fw_phone;
    }

    public void setFw_phone(String fw_phone) {
        this.fw_phone = fw_phone;
    }

    public String getFw_university() {
        return fw_university;
    }

    public void setFw_university(String fw_university) {
        this.fw_university = fw_university;
    }

    public String getFw_address() {
        return fw_address;
    }

    public void setFw_address(String fw_address) {
        this.fw_address = fw_address;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isTrained() {
        return trained;
    }

    public void setTrained(boolean trained) {
        this.trained = trained;
    }

    public List<String> getU_array() {
        return u_array;
    }

    public void setU_array(List<String> u_array) {
        this.u_array = u_array;
    }

    public List<String> getAtoken() {
        return atoken;
    }

    public void setAtoken(List<String> atoken) {
        this.atoken = atoken;
    }

    public List<String> getItoken() {
        return itoken;
    }

    public void setItoken(List<String> itoken) {
        this.itoken = itoken;
    }
}



