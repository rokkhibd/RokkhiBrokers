package com.rokkhi.brokers.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class FBuildings implements Serializable {

    private String build_id="none";
    private String b_address="none";
    private String b_code="none";
    private String b_houseno="none";
    private String b_roadno="none";
    private String b_district="none";
    private String b_area="none";
    private String flatformat="none";
    private int flatperfloor=0;
    private Date followupdate=new Date();
    private String housename="none";
    private int totalfloor=0;
    private Date created_at=new Date();
    private Date updated_at=new Date();
    private String status="none";
    private String active;
    private ArrayList<String> b_imageUrl;
    private ArrayList<String> b_array;
    private Double latitude=0.0;
    private Double longitude=0.0;



    public FBuildings() {
    }


    public FBuildings(String build_id, String b_address, String b_code, String b_houseno, String b_roadno, String b_district, String b_area, String flatformat, int flatperfloor, Date followupdate, String housename, int totalfloor, Date created_at, Date updated_at, String status, String active, ArrayList<String> b_imageUrl, ArrayList<String> b_array, Double latitude, Double longitude) {
        this.build_id = build_id;
        this.b_address = b_address;
        this.b_code = b_code;
        this.b_houseno = b_houseno;
        this.b_roadno = b_roadno;
        this.b_district = b_district;
        this.b_area = b_area;
        this.flatformat = flatformat;
        this.flatperfloor = flatperfloor;
        this.followupdate = followupdate;
        this.housename = housename;
        this.totalfloor = totalfloor;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.active = active;
        this.b_imageUrl = b_imageUrl;
        this.b_array = b_array;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getB_address() {
        return b_address;
    }

    public void setB_address(String b_address) {
        this.b_address = b_address;
    }

    public String getB_code() {
        return b_code;
    }

    public void setB_code(String b_code) {
        this.b_code = b_code;
    }

    public String getB_houseno() {
        return b_houseno;
    }

    public void setB_houseno(String b_houseno) {
        this.b_houseno = b_houseno;
    }

    public String getB_roadno() {
        return b_roadno;
    }

    public void setB_roadno(String b_roadno) {
        this.b_roadno = b_roadno;
    }

    public String getB_district() {
        return b_district;
    }

    public void setB_district(String b_district) {
        this.b_district = b_district;
    }

    public String getB_area() {
        return b_area;
    }

    public void setB_area(String b_area) {
        this.b_area = b_area;
    }

    public String getFlatformat() {
        return flatformat;
    }

    public void setFlatformat(String flatformat) {
        this.flatformat = flatformat;
    }

    public int getFlatperfloor() {
        return flatperfloor;
    }

    public void setFlatperfloor(int flatperfloor) {
        this.flatperfloor = flatperfloor;
    }

    public Date getFollowupdate() {
        return followupdate;
    }

    public void setFollowupdate(Date followupdate) {
        this.followupdate = followupdate;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public int getTotalfloor() {
        return totalfloor;
    }

    public void setTotalfloor(int totalfloor) {
        this.totalfloor = totalfloor;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public ArrayList<String> getB_imageUrl() {
        return b_imageUrl;
    }

    public void setB_imageUrl(ArrayList<String> b_imageUrl) {
        this.b_imageUrl = b_imageUrl;
    }

    public ArrayList<String> getB_array() {
        return b_array;
    }

    public void setB_array(ArrayList<String> b_array) {
        this.b_array = b_array;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
