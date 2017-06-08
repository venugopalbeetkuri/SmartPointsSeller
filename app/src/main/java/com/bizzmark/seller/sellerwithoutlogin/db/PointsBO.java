package com.bizzmark.seller.sellerwithoutlogin.db;

/**
 * Created by Tharun on 23-02-2017.
 */

public class PointsBO {

    private
    String deviceid;
    String type;
    String billAmount;
    String storeName;
    String points;
    String disCountAmount;
    String time;

    public PointsBO(){

    }

    public PointsBO(String type,
                    String billAmount,
                    String storeName,
                    String points,
                    String deviceID,
                    String disCountAmount,
                    String time){

        this.type = type;
        this.billAmount =billAmount;
        this.storeName = storeName;
        this.points = points;
        this.deviceid = deviceID;
        this.disCountAmount = disCountAmount;
        this.time = time;
    }


    public String getType() {
        return type;
    }

    public String setType(String type) {
        this.type = type;
        return type;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDeviceId() {
        return deviceid;
    }

    public void setDeviceId(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDisCountAmount() {
        return disCountAmount;
    }

    public void setDisCountAmount(String disCountAmount) {
        this.disCountAmount = disCountAmount;
    }

    public String getTime(){return time;}

    public void setTime(String time) { this.time = time;}
}
