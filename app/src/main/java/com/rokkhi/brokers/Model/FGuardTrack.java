package com.rokkhi.brokers.Model;

import java.util.Date;

public class FGuardTrack {
    private String user_id="none";
    private Date timeStart=new Date();
    private Date timeEnd=new Date();
    private String build_id="none";
    private String doc_id="none";
    private String guard_id="none";
    private String latitude="none";
    private String longitude="none";
    private String status="none";


    public FGuardTrack() {

    }


    public FGuardTrack(String user_id, Date timeStart, Date timeEnd, String build_id, String doc_id, String guard_id, String latitude, String longitude,String status) {
        this.user_id = user_id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.build_id = build_id;
        this.doc_id = doc_id;
        this.guard_id = guard_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status=status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
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

    public String getGuard_id() {
        return guard_id;
    }

    public void setGuard_id(String guard_id) {
        this.guard_id = guard_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
