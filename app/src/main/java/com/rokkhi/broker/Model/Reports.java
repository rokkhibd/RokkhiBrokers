package com.rokkhi.broker.Model;

public class Reports {
    String docID;
    String buildID;
    String userID;
    String details;
    String title;
    String phoneNumber;

    public Reports() {
    }

    public Reports(String docID, String buildID, String userID, String details, String title, String phoneNumber) {
        this.docID = docID;
        this.buildID = buildID;
        this.userID = userID;
        this.details = details;
        this.title = title;
        this.phoneNumber = phoneNumber;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
