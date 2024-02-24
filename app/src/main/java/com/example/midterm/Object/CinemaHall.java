package com.example.midterm.Object;

import android.util.Log;
import android.util.Pair;

import com.example.midterm.DAO.SeatDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CinemaHall implements Serializable {

    private String cinemaName;
    private int hallId;
    private int totalSeats;
    private int  rowCount;
    private int colCount;
    private List<Pair> occupiedTimePeriods;


    public String getCinemaName(){
        return this.cinemaName;
    }

    public CinemaHall(String cinemaName, int hallId, int rows, int columns) {
        this.cinemaName = cinemaName;
        this.hallId = hallId;
        this.totalSeats = rows*columns;
        this.rowCount = rows;
        this.colCount = columns;
        this.occupiedTimePeriods = new ArrayList<>();
    }

    public int getColCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public boolean addShows(Show m_show) {
        if (isTimeOccupied(m_show.getDateOfShow(), m_show.getTimePeriod())) {
            return false; // Show cannot be added, time is already occupied
        }

        occupiedTimePeriods.add(Pair.create(m_show.getDateOfShow(), m_show.getTimePeriod()));
        m_show.setPlayAt(this);
        Log.e("SHOW to Cine","Done");
        return true; // Show added successfully
    }

    private boolean isTimeOccupied(Date date,TimePeriod timePeriod) {
        if (occupiedTimePeriods.isEmpty())
            return false;
        for (Pair occupiedPeriod : occupiedTimePeriods) {

            if (occupiedPeriod.first.equals(date) && timePeriod.overlaps((TimePeriod) occupiedPeriod.second)) {
                return true; // Time is occupied
            }
        }
        return false; // Time is not occupied
    }

    public List<Pair> getOccupiedTimePeriods() {
        return this.occupiedTimePeriods;
    }

    public boolean isBusy() {
        int totalOccupiedMinutes = 0;

        for (Pair occupiedPeriod : occupiedTimePeriods) {
            TimePeriod timePeriod = (TimePeriod) occupiedPeriod.second;
            totalOccupiedMinutes += timePeriod.getDurationInMinutes();
        }

        // Calculate the total occupied hours
        int totalOccupiedHours = totalOccupiedMinutes / 60;

        // Check if the total occupied hours are more than 23 hours
        return totalOccupiedHours > 23;
    }

    public int getHallId() {
        return hallId;
    }

}
