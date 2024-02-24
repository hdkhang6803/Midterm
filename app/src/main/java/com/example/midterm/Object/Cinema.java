package com.example.midterm.Object;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Cinema {

    private String cinemaName;
    private int totalHalls;
    private List<Integer> hallsID;
    private List<CinemaHall> halls;

    public Cinema(String cinemaName, List<Integer> hallsID) {
        this.cinemaName = cinemaName;
        this.hallsID = hallsID;
        this.totalHalls = hallsID.size();
        this.halls = new ArrayList<>();
        initializeHalls();
    }

    public Cinema(String cinemaName) {
        this.cinemaName = cinemaName;
        this.hallsID = new ArrayList<>();
        this.totalHalls = hallsID.size();
        this.halls = new ArrayList<>();
    }
    private void initializeHalls() {
        for (int i = 0; i < totalHalls; i++) {
            addHall(hallsID.get(i).intValue());
        }
    }

    public void addHall(int hallID){
        Log.e("addHall", String.valueOf(hallID));
        hallsID.add(Integer.valueOf(hallID));
        CinemaHall cinemaHall = new CinemaHall(this.cinemaName, Integer.valueOf(hallID),10,10);
        halls.add(cinemaHall);
    }
    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public int getTotalHalls() {
        return totalHalls;
    }

    public void setTotalHalls(int totalHalls) {
        this.totalHalls = totalHalls;
        this.halls = new ArrayList<>();
        initializeHalls();
    }

    public List<CinemaHall> getHalls() {
        return halls;
    }
    public CinemaHall getHallByID(int id){
        for (int i = 0 ; i < totalHalls; i++){
            if (Integer.valueOf(hallsID.get(i)) == id){
                return halls.get(i);
            }
        }
        return null;
    }

    public void setHalls(List<CinemaHall> halls) {
        this.halls = halls;
    }

    public boolean addShow(Show m_show) {
        for (CinemaHall hall : halls) {
            Log.e("AddSHOW HALLS", hall.getCinemaName() + String.valueOf(hall.getHallId()));
            if(hall.addShows(m_show))
                return true; // Show added successfully
        }

        return false; // No hall with available time for the show
    }

}
