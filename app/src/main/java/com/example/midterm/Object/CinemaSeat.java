package com.example.midterm.Object;

public class CinemaSeat {

    public enum Status {
        RESERVED,
        AVAILABLE,
        SELECTED
    }

    private int price;
    private Status status;

    private int rowIdx;
    private int colIdx;

    public CinemaSeat(int price, Status status, int rowIdx, int colIdx) {
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
        this.price = price;
        this.status = status;
    }

    public int getColIdx() {
        return colIdx;
    }

    public int getRowIdx() {
        return rowIdx;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
