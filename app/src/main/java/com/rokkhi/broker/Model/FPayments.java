package com.rokkhi.broker.Model;

import java.util.Date;

public class FPayments {

    private int total_earning=0;
    private int due_earning=0;
    private int bonus_earning=0;
    private int due_bonus_earning=0;

    private int total_buildings=0;//onetime
    private int active_buildings=0;//monthly
    private int due_buildings=0;
    private int total_meeting=0;
    private int due_meeting=0;
    private int total_referral=0;
    private int due_referral=0;

    private String user_id="none";
    private String ref_id="none";
    private String fw_phone="none";
    private String bkash_no="none";
    private String nogod_no="none";
    private Date created_at=new Date();
    private Date updated_at=new Date();
    private Date working_from=new Date();


    public FPayments() {

    }

    public FPayments(String user_id, String ref_id, String fw_phone, int total_earning, int due_earning,int bonus_earning,int due_bonus_earning, int total_buildings, int active_buildings, int due_buildings, String bkash_no, String nogod_no, Date created_at, Date updated_at, Date working_from, int total_meeting, int due_meeting, int total_referral, int due_referral) {
        this.user_id = user_id;
        this.ref_id = ref_id;
        this.fw_phone = fw_phone;
        this.total_earning = total_earning;
        this.due_earning = due_earning;
        this.bonus_earning=bonus_earning;
        this.due_bonus_earning=due_bonus_earning;
        this.total_buildings = total_buildings;
        this.active_buildings = active_buildings;
        this.due_buildings = due_buildings;
        this.bkash_no = bkash_no;
        this.nogod_no = nogod_no;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.working_from = working_from;
        this.total_meeting = total_meeting;
        this.due_meeting = due_meeting;
        this.total_referral = total_referral;
        this.due_referral = due_referral;
    }


    public int getBonus_earning() {
        return bonus_earning;
    }

    public void setBonus_earning(int bonus_earning) {
        this.bonus_earning = bonus_earning;
    }

    public int getDue_bonus_earning() {
        return due_bonus_earning;
    }

    public void setDue_bonus_earning(int due_bonus_earning) {
        this.due_bonus_earning = due_bonus_earning;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getFw_phone() {
        return fw_phone;
    }

    public void setFw_phone(String fw_phone) {
        this.fw_phone = fw_phone;
    }

    public int getTotal_earning() {
        return total_earning;
    }

    public void setTotal_earning(int total_earning) {
        this.total_earning = total_earning;
    }

    public int getDue_earning() {
        return due_earning;
    }

    public void setDue_earning(int due_earning) {
        this.due_earning = due_earning;
    }

    public int getTotal_buildings() {
        return total_buildings;
    }

    public void setTotal_buildings(int total_buildings) {
        this.total_buildings = total_buildings;
    }

    public int getActive_buildings() {
        return active_buildings;
    }

    public void setActive_buildings(int active_buildings) {
        this.active_buildings = active_buildings;
    }

    public int getDue_buildings() {
        return due_buildings;
    }

    public void setDue_buildings(int due_buildings) {
        this.due_buildings = due_buildings;
    }

    public String getBkash_no() {
        return bkash_no;
    }

    public void setBkash_no(String bkash_no) {
        this.bkash_no = bkash_no;
    }

    public String getNogod_no() {
        return nogod_no;
    }

    public void setNogod_no(String nogod_no) {
        this.nogod_no = nogod_no;
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

    public Date getWorking_from() {
        return working_from;
    }

    public void setWorking_from(Date working_from) {
        this.working_from = working_from;
    }

    public int getTotal_meeting() {
        return total_meeting;
    }

    public void setTotal_meeting(int total_meeting) {
        this.total_meeting = total_meeting;
    }

    public int getDue_meeting() {
        return due_meeting;
    }

    public void setDue_meeting(int due_meeting) {
        this.due_meeting = due_meeting;
    }

    public int getTotal_referral() {
        return total_referral;
    }

    public void setTotal_referral(int total_referral) {
        this.total_referral = total_referral;
    }

    public int getDue_referral() {
        return due_referral;
    }

    public void setDue_referral(int due_referral) {
        this.due_referral = due_referral;
    }
}



