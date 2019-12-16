package com.rokkhi.broker.Model;


import java.util.Date;


public class FBuildingContacts {

    private String name="none";
    private String designation="none";
    private String phone_no="none";
    private String doc_id="none"; //mobileno+bcode
    private Date created_at=new Date();
    private Date updated_at=new Date();
    private String b_code="none";

    public FBuildingContacts() {

    }

    public FBuildingContacts(String name, String designation, String phone_no, String doc_id, Date created_at, Date updated_at, String b_code) {
        this.name = name;
        this.designation = designation;
        this.phone_no = phone_no;
        this.doc_id = doc_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.b_code = b_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
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
