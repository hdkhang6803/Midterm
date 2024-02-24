package com.example.midterm.DAO;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;

import com.example.midterm.DataHelper;
import com.example.midterm.Object.Movie;
import com.example.midterm.R;
import com.example.midterm.Object.Show;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MovieDAO {

    private static List<Movie> m_movie;

    public static List<Movie> getTop4RecentMovies(Context context, List<Movie> movieList) {
        if(m_movie == null)
            getAllMoviesInfo(context);

        // Sort the movieList based on release date in descending order
        Collections.sort(m_movie, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                return movie2.getReleaseDate().compareTo(movie1.getReleaseDate());
            }
        });

        // Get the top 4 movies or all movies if the list size is less than 4
        int topMoviesCount = Math.min(movieList.size(), 4);
        return movieList.subList(0, topMoviesCount);
    }
    public static List<Movie> getAllMoviesInfo(Context context) {
        if (m_movie == null) {
            Resources resources = context.getResources();


            String[] movieNames = resources.getStringArray(R.array.movieNames);
            String[] movieDurations = resources.getStringArray(R.array.movieDurations);
            String[] movieReleaseDates = resources.getStringArray(R.array.movieReleaseDates);
            TypedArray imgs = resources.obtainTypedArray(R.array.moviePosters);
            String[] movieDescriptions = resources.getStringArray(R.array.movieDescriptions);
            m_movie = new ArrayList<>();

            Random random = new Random();

            for (int i = 0; i < movieNames.length; i++) {
                int posterDrawableId = imgs.getResourceId(i, 0);
                String movieName = (i < movieNames.length) ? movieNames[i] : "";
                String duration = (i < movieDurations.length) ? movieDurations[i] : "";
                Date releaseDate = (i < movieReleaseDates.length) ? DataHelper.parseDateFromString(movieReleaseDates[i]) : null;
                String description = (i < movieDescriptions.length) ? movieDescriptions[i] : "";

                Bitmap poster = DataHelper.getBitmapFromDrawable(context, posterDrawableId);

                Movie movie = new Movie(poster, movieName, duration, releaseDate, description);

                m_movie.add(movie);
            }
        }
        return m_movie;
    }

    public static Movie getMovieByName(Context context, String movieName){
        if (m_movie == null){
            getAllMoviesInfo(context);
        }

        for (int i = 0; i < m_movie.size(); i++) {
            if (movieName.equals(m_movie.get(i).getMovieName())) {
                return m_movie.get(i);
            }
        }
        return null;

    }

    public static Bitmap getPosterByMovieName(Context context, String movie_name){
        if (m_movie == null)
            getAllMoviesInfo(context);
        for(Movie movie : m_movie){
            if (movie.getMovieName().equals(movie_name))
                return movie.getPosterImage();
        }
        return null;
    }
}
