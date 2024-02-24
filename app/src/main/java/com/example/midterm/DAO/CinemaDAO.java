package com.example.midterm.DAO;

import android.content.Context;
import android.util.Log;

import com.example.midterm.DataHelper;
import com.example.midterm.Object.Cinema;
import com.example.midterm.Object.CinemaHall;
import com.example.midterm.Object.Show;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinemaDAO {

    private static List<Cinema> cinemas;
    private static final String CSV_FILE_NAME = "cinemas.csv";
    private enum ColumnIdx{
        CINEMA_NAME,
        HALL_ID,
    }
    public static List<Cinema> getAllCinemas(Context context) {
        Log.e("GETCINEMAS", "IN");
        if (cinemas == null) {
            cinemas = new ArrayList<>();

            try {
                InputStream inputStream = context.getAssets().open(CSV_FILE_NAME);
                if (inputStream == null || DataHelper.isCsvFileEmpty(context, CSV_FILE_NAME)) {
                    Log.e("GETCINEMAS", "EMPTY FILE");
                    return cinemas;
                }
                InputStreamReader reader = new InputStreamReader(inputStream);

                CSVReader csvReader = new CSVReader(reader);
                List<String[]> data = csvReader.readAll();

                Map<String, Cinema> cinemaMap = new HashMap<>();

                for (String[] row : data) {
                    if (row.length == 2) {
                        String cinemaName = row[ColumnIdx.CINEMA_NAME.ordinal()];
                        int hallID = Integer.parseInt(row[ColumnIdx.HALL_ID.ordinal()]);

                        // Check if the cinema already exists in the map
                        if (cinemaMap.containsKey(cinemaName)) {
                            cinemaMap.get(cinemaName).addHall(hallID);
                        } else {
                            // Create a new cinema and add it to the map
                            Cinema cinema = new Cinema(cinemaName);
                            cinema.addHall(hallID);
                            cinemaMap.put(cinemaName, cinema);
                        }
                    }
                }

                // Add all cinemas from the map to the list
                cinemas.addAll(cinemaMap.values());

                csvReader.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("get SHOWS", "FAIL");
            }
        }
        for(Cinema cinema:cinemas){
            System.out.println(cinema.getCinemaName());
            for(CinemaHall hall : cinema.getHalls()){
                Log.e("HALLS", hall.getCinemaName() + String.valueOf(hall.getHallId()));
            }
        }
        return cinemas;
    }

    public static Cinema getCinemaByName(Context context, String cineName) {
        if (cinemas == null) {
            getAllCinemas(context);
        }
        Cinema new_cine = null;
        for(Cinema cine : cinemas){
            if (cine.getCinemaName().equals(cineName)){
                new_cine = cine;
            }
        }
        return new_cine;
    }

    public static CinemaHall getHallByCineNameAndID(Context context, String cineName, int hallID){
        if (cinemas == null)
            getAllCinemas(context);
        Cinema cine = getCinemaByName(context,cineName);
        for(CinemaHall hall:cine.getHalls()){
            if (hall.getHallId() == hallID)
                return hall;
        }
        return null;
    }
}
