package com.rokkhi.brokers.Model;


import java.util.Date;


public class FWorkerBuilding {

    private String build_id="none"; //docid
    private String doc_id="none"; //docid
    private String f_uid="none";
    private String status_id="none";
    private Date created_at=new Date();
    private Date updated_at=new Date();
    private String b_code="none";


    public FWorkerBuilding() {
    }

    public FWorkerBuilding(String build_id, String doc_id, String f_uid, String status, Date created_at, Date updated_at, String b_code) {
        this.build_id = build_id;
        this.doc_id = doc_id;
        this.f_uid = f_uid;
        this.status_id = status_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.b_code = b_code;
    }



    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getF_uid() {
        return f_uid;
    }

    public void setF_uid(String f_uid) {
        this.f_uid = f_uid;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
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

    public String getB_code() {
        return b_code;
    }

    public void setB_code(String b_code) {
        this.b_code = b_code;
    }
}
