package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midterm.Adapter.CinemaShowtimeAdapter;
import com.example.midterm.Adapter.DateAdapter;
import com.example.midterm.DAO.MovieDAO;
import com.example.midterm.DAO.ShowDAO;
import com.example.midterm.Object.Movie;
import com.example.midterm.Object.Show;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoviePage extends AppCompatActivity implements DateAdapter.OnItemClickListener{

    private TextView movieName;
    private List<Pair> dateList;

    private CinemaShowtimeAdapter showtimeAdapter;
    private List<Show> ShowbyDate;
    private RecyclerView cinemaShowTimeRecyclerView;

    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);

        // Retrieve the Movie object from the intent
        String movieNamePassed = (String) getIntent().getStringExtra("MOVIE");
        movie = MovieDAO.getMovieByName(this, movieNamePassed);

        ImageView posterView = findViewById(R.id.moviePoster);
        posterView.setImageBitmap(movie.getPosterImage());
        movieName = findViewById(R.id.movieName);
        movieName.setText(movie.getMovieName());
        TextView duration = findViewById(R.id.duration);
        duration.setText(movie.getTimeDuration());
        TextView description = findViewById(R.id.description);
        description.setText(movie.getDescription());
        ImageView backButt = findViewById(R.id.backButton1);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                if(ShowbyDate == null)
                    return;
                for(Show show : ShowbyDate){
                    if (show.getStatus() == Show.Status.SELECTED){
                        show.setStatus(Show.Status.AVAILABLE);
                    }
                }
            }
        });

        RecyclerView dateRecyclerView = findViewById(R.id.dateRecyclerView);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        dateList = generateDateList();
        DateAdapter dateAdapter = new DateAdapter(this,dateList, dateRecyclerView);
        dateRecyclerView.setAdapter(dateAdapter);

        cinemaShowTimeRecyclerView = findViewById(R.id.cinemaShowTimeRecyclerView);
        cinemaShowTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        showtimeAdapter = new CinemaShowtimeAdapter(this, new ArrayList<>());
        cinemaShowTimeRecyclerView.setAdapter(showtimeAdapter);

        ImageView nextButton = findViewById(R.id.myNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShowbyDate==null || ShowbyDate.isEmpty()){
                    CharSequence text = "Must select movie show time first !";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getBaseContext(), text, duration);
                    toast.show();
                    return;
                }
                for (Show show:ShowbyDate){
                    if (show.getStatus()== Show.Status.SELECTED){
                        Intent intent = new Intent(getBaseContext(), SeatBooking.class);
                        intent.putExtra("CINEMA", show.getPlayAt().getCinemaName());
                        intent.putExtra("HALL", show.getPlayAt().getHallId());
                        intent.putExtra("DATE", show.getDateOfShow());
                        intent.putExtra("MOVIE", show.getMovieName());
                        intent.putExtra("TIME", show.getShowStartTime());
                        show.setStatus(Show.Status.AVAILABLE);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private List<Pair> generateDateList() {
        List<Pair> dateList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            String dayOfWeek = new SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.getTime());
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            Log.e("Date", date);

            dateList.add(new Pair(dayOfWeek, date));

            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }

        return dateList;
    }


    @Override
    public void onDateClick(int position) {
        Pair clickedItem = dateList.get(position);
        String selectedFullDate = clickedItem.second.toString();
        Log.e("Date click", selectedFullDate);
        ShowbyDate = ShowDAO.getShowsByMovieNameAndDate( this,movieName.getText().toString() ,selectedFullDate);

        if (!ShowbyDate.isEmpty()){
            Log.e("Date in", selectedFullDate);
            showtimeAdapter.setShowList(ShowbyDate);
            cinemaShowTimeRecyclerView.setVisibility(View.VISIBLE);
        }else{
            CharSequence text = "No show on this day!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getBaseContext(), text, duration);
            toast.show();
            cinemaShowTimeRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

}