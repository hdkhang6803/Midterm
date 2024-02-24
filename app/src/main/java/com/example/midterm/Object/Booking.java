package com.example.midterm.Object;

import com.google.gson.Gson;

public class Booking {
    private String cineName;
    private int hallID;
    private String date;
    private String startTime;
    private int rowIdx;
    private int colIdx;
    private String phoneNum;


    public Booking(){

    }
    public Booking(String cineName, int hallID, String date, String startTime, int rowIdx, int colIdx, String phoneNum) {
        this.cineName = cineName;
        this.hallID = hallID;
        this.date = date;
        this.startTime = startTime;
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
        this.phoneNum = phoneNum;
    }
    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    // Getter methods
    public String getCineName() {
        return cineName;
    }

    public int getHallID() {
        return hallID;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getRowIdx() {
        return rowIdx;
    }

    public int getColIdx() {
        return colIdx;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    // Setter methods
    public void setCineName(String cineName) {
        this.cineName = cineName;
    }

    public void setHallID(int hallID) {
        this.hallID = hallID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setRowIdx(int rowIdx) {
        this.rowIdx = rowIdx;
    }

    public void setColIdx(int colIdx) {
        this.colIdx = colIdx;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
