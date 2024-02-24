package com.example.midterm.Object;

import android.util.Log;
import android.util.Pair;

import com.example.midterm.DAO.SeatDAO;
import com.example.midterm.DataHelper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Show {

    public enum Status {
        SELECTED,
        AVAILABLE,
        FULL
    }

    private Status status;
    private String movieName;
    private Date dateOfShow;
    private TimePeriod timePeriod;
    private CinemaHall playAt;
    private CinemaSeat seats[][];

    // Constructor that takes start and end times
    public Show(String movieName, Date dateOfShow, int startHour, int startMinute, int endHour, int endMinute) {
        this.dateOfShow = dateOfShow;
        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);
        this.timePeriod = new TimePeriod(startTime, endTime);
        this.movieName = movieName;
        this.status = Status.AVAILABLE;
    }
    void initSeats(){
        CinemaHall hall = getPlayAt();
        int r = hall.getRowCount();
        int c = hall.getColCount();
        seats = new CinemaSeat[r][c];
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = new CinemaSeat(90000, CinemaSeat.Status.AVAILABLE, i, j);
            }
        }
    }

    // Other constructors and methods...

    public Date getDateOfShow() {
        return dateOfShow;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }



    public void setDateOfShow(Date dateOfShow) {
        this.dateOfShow = dateOfShow;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public CinemaHall getPlayAt() {
        return playAt;
    }

    public boolean addToCinema(Cinema cinema) {
        boolean flag = cinema.addShow(this);
        if(flag)
            initSeats();
        else {
            Log.e("ADD TO CINEMA"," can not add to cinema");
        }
        return flag;
    }
    public void setPlayAt(CinemaHall hall){
        this.playAt = hall;
    }

    public String getShowStartTime() {
        // Assuming you have timePeriod and dateOfShow available
        LocalTime localTime = timePeriod.getStartTime();

        // Convert Date to Instant and then to LocalDateTime
        Instant instant = dateOfShow.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Combine LocalDateTime with LocalTime
        LocalDateTime combinedDateTime = LocalDateTime.of(localDateTime.toLocalDate(), localTime);

        // Format the combined LocalDateTime using DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(combinedDateTime);
    }

    public interface SeatCallback {
        void onSeatsRetrieved(CinemaSeat[][] seats);
    }
    public CinemaSeat[][] getSeats() {
        if(seats == null ){
            Log.e("GETSEATS", "Empty seats");
            return null;
        }

        return seats;
    }

    public CinemaSeat[][] getSeats(SeatCallback callback) {
        if(seats == null ){
            Log.e("GETSEATS", "Empty seats");
            return null;
        }
        SeatDAO.getCinemaReservedSeat(
                playAt.getCinemaName(),
                playAt.getHallId(),
                DataHelper.convertDateToString(getDateOfShow())
                , getShowStartTime(), new SeatDAO.SeatCallback() {
                    @Override
                    public void onSeatsRetrieved(List<Pair> reserved) {
                        Log.e("RETRIEVED","After retrieved");
                        System.out.println(reserved);
                        for(Pair pair : reserved){
                            CinemaSeat cur_seat = seats[Integer.parseInt(pair.first.toString())][Integer.parseInt(pair.second.toString())];
                            cur_seat.setStatus(CinemaSeat.Status.RESERVED);
                        }
                        callback.onSeatsRetrieved(seats);

                    }
                });
        return seats;
    }
}
