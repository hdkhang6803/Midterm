package com.example.midterm.DAO;

import android.content.Context;
import android.util.Log;

import com.example.midterm.DataHelper;
import com.example.midterm.Object.Cinema;
import com.example.midterm.Object.Show;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowDAO {
    private static List<Show> m_shows;
    private static final String CSV_FILE_NAME = "shows.csv"; // Change to your CSV file name

    private enum ColumnIdx{
        MOVIE_NAME,
        CINEMA_NAME,
        DATE_SHOW,
        START_HOUR,
        START_MINUTE,
        END_HOUR,
        END_MINUTE
    }

    public static void insertShow(String movie_name, String cine_name,
                                  String date,
                                  int start_hour, int start_min,
                                  int end_hour, int end_min) {
        try {
            String content = String.format("%s;%s;%s;%d;%d;%d;%d",
                    movie_name, cine_name, date, start_hour, start_min, end_hour, end_min);

            File file = new File(CSV_FILE_NAME);

            // If the file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Data written to CSV successfully.");
        } catch (IOException e) {
            Log.e("insert show", "failed");
            e.printStackTrace();
        }
    }
    private static String[] showToCsvRow(Show show, String movie_name) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(show.getDateOfShow());

        String startTimeString = show.getShowStartTime();

        return new String[]{
                movie_name,
                show.getPlayAt().getCinemaName(),
                dateString,
                String.valueOf(show.getTimePeriod().getStartTime().getHour()),
                String.valueOf(show.getTimePeriod().getStartTime().getMinute()),
                String.valueOf(show.getTimePeriod().getEndTime().getHour()),
                String.valueOf(show.getTimePeriod().getEndTime().getMinute())
        };
    }

    public static List<Show> getAllShows(Context context) {
        Log.e("GETSHOWS", "IN ALL");

        m_shows = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open(CSV_FILE_NAME);
            if (inputStream == null || isCsvFileEmpty(context)) {
                Log.e("GETSHOWS", "EMPTY FILE");
                return m_shows;
            }
            InputStreamReader reader = new InputStreamReader(inputStream);

            CSVReader csvReader = new CSVReader(reader);
            List<String[]> data = csvReader.readAll();

            for(String[] row : data){
                System.out.println(row.length);
                if (row.length == 7) {
                    String movieName = row[ColumnIdx.MOVIE_NAME.ordinal()];
                    String cinemaName = row[ColumnIdx.CINEMA_NAME.ordinal()];
                    Date dateOfShow = DataHelper.parseDateFromString(row[ColumnIdx.DATE_SHOW.ordinal()]);
                    int startHour = Integer.parseInt(row[ColumnIdx.START_HOUR.ordinal()]);
                    int startMinute = Integer.parseInt(row[ColumnIdx.START_MINUTE.ordinal()]);
                    int endHour = Integer.parseInt(row[ColumnIdx.END_HOUR.ordinal()]);
                    int endMinute = Integer.parseInt(row[ColumnIdx.END_MINUTE.ordinal()]);
                    Log.e("GETSHOWS", movieName);

                    Show show = new Show(movieName,dateOfShow, startHour, startMinute, endHour, endMinute);
                    boolean flag = show.addToCinema(CinemaDAO.getCinemaByName(context,cinemaName));
                    if (flag)
                        m_shows.add(show);
                }
            }

            csvReader.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("get SHOWS", "FAIL");
        }
        return m_shows;
    }
    public static List<Show> getShowsByMovieName(Context context, String m_movieName) {
        Log.e("GETSHOWS", "IN " + m_movieName);
        List<Show> shows = new ArrayList<>();
        if(m_shows == null)
            getAllShows(context);

       for(Show show:m_shows){
           if (show.getMovieName().equals(m_movieName)){
               shows.add(show);
           }
       }
       return shows;
    }


    public static List<Show> getShowsByMovieNameAndDate(Context context, String m_movieName, String date) {
        Log.e("GETSHOWS", "IN " + m_movieName);
        List<Show> shows = new ArrayList<>();
        if(m_shows == null)
            getAllShows(context);

        Date m_date = DataHelper.parseDateFromString(date);
        for(Show show:m_shows){
            if (show.getMovieName().equals(m_movieName) && show.getDateOfShow().equals(m_date)){
                shows.add(show);
            }
        }
        return shows;
    }

    public static List<Show> getShowsByCinemaMovieNameDateStartTime(Context context, String cinema, String m_movieName, String date, String startStime) {
        Log.e("GETSHOWS", "IN " + m_movieName);
        List<Show> shows = new ArrayList<>();
        if(m_shows == null)
            getAllShows(context);

        Date m_date = DataHelper.parseDateFromString(date);
        for(Show show:m_shows){
            if (show.getMovieName().equals(m_movieName)
                    && show.getDateOfShow().equals(m_date)
                    && startStime.equals(show.getShowStartTime())
                    && show.getPlayAt().getCinemaName().equals(cinema)){
                shows.add(show);
            }
        }
        return shows;
    }

    public static boolean isCsvFileEmpty(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(CSV_FILE_NAME);
            if (inputStream == null) {
                return true;
            }
            InputStreamReader reader = new InputStreamReader(inputStream);

            CSVReader csvReader = new CSVReader(reader);
            List<String[]> data = csvReader.readAll();
            return data.isEmpty();
        }catch (IOException e){
            e.printStackTrace();
            return true;
        }catch (CsvException e){
            e.printStackTrace();
            return true;
        }
    }

}
