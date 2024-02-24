package com.example.midterm.Object;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.midterm.DAO.ShowDAO;
import com.example.midterm.DataHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie implements Serializable {
    private transient Bitmap posterImage;
    private String movieName;
    private String timeDuration; // Store the concatenated time duration string
    private Date releaseDate;
    private String description;


    // Constructor
    public Movie(Bitmap posterImage, String movieName, String duration, Date releaseDate, String description) {
        this.posterImage = posterImage;
        this.movieName = movieName;
        this.timeDuration = duration;
        this.releaseDate = releaseDate;
        this.description = description;
    }
    public Movie(Bitmap posterImage, String movieName, int durationHours, int durationMinutes, Date releaseDate, String description) {
        this.posterImage = posterImage;
        this.movieName = movieName;
        this.setTimeDuration(durationHours, durationMinutes);
        this.releaseDate = releaseDate;
        this.description = description;
    }

    // Getter methods
    public Bitmap getPosterImage() {
        return posterImage;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    // Setter methods (if needed)
    public void setPosterImage(Bitmap posterImage) {
        this.posterImage = posterImage;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    // Method to set and update time duration as a concatenated string
    public void setTimeDuration(int durationHours, int durationMinutes) {
        this.timeDuration = durationHours + "h " + durationMinutes + "m";
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription(){
        return this.description;
    }

}
