package com.rokkhi.broker.Model;

import java.util.Date;

public class PaymentHistory {

    private String id="none";
    private String f_uid="none";
    private String payment_type ="none";
    private String build_id="none";
    private int amount=0;
    private String payment_status = "none";
    private Date month=new Date();
    private Date created_at=new Date();
    private Date updated_at=new Date();

    public PaymentHistory() {
    }


    public PaymentHistory(String id, String f_uid, String payment_type, String build_id, int amount, String payment_status, Date month, Date created_at, Date updated_at) {
        this.id = id;
        this.f_uid = f_uid;
        this.payment_type = payment_type;
        this.build_id = build_id;
        this.amount = amount;
        this.payment_status = payment_status;
        this.month = month;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_uid() {
        return f_uid;
    }

    public void setF_uid(String f_uid) {
        this.f_uid = f_uid;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
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
}



