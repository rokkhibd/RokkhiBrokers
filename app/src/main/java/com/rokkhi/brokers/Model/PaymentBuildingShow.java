package com.rokkhi.brokers.Model;

public class PaymentBuildingShow {
    private String b_name="none";
    private int amount=0;


    public PaymentBuildingShow() {
    }

    public PaymentBuildingShow(String b_name, int amount) {
        this.b_name = b_name;
        this.amount = amount;
    }


    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
