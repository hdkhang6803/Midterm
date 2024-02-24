package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.midterm.Adapter.CarouselAdapter;
import com.example.midterm.Adapter.MovieAdapter;
import com.example.midterm.DAO.CinemaDAO;
import com.example.midterm.DAO.MovieDAO;
import com.example.midterm.DAO.ShowDAO;
import com.example.midterm.Object.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CinemaDAO.getAllCinemas(this);
        List<Movie> movieList = MovieDAO.getAllMoviesInfo(this);


        CarouselAdapter carouselAdapter = new CarouselAdapter(this, MovieDAO.getTop4RecentMovies(this, movieList));
        CarouselRecyclerview hotView = findViewById(R.id.carouselRecyclerview);
        hotView.setAdapter(carouselAdapter);
        hotView.setAlpha(true);
        hotView.setIntervalRatio(0.3F);

        TextView logOutBtn = findViewById(R.id.logOutBtn);

        ImageView imageViewUserAvatar = findViewById(R.id.imageViewUserAvatar);
        imageViewUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logOutBtn.getVisibility() == View.INVISIBLE){
                    logOutBtn.setVisibility(View.VISIBLE);
                }else{
                    logOutBtn.setVisibility(View.INVISIBLE);
                }
            }
        });


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getBaseContext(), LogIn.class);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        RecyclerView movieListView = (RecyclerView)findViewById(R.id.movieList);
        // Create and set the adapter for the RecyclerView
        MovieAdapter movieAdapter = new MovieAdapter(this, movieList );
        movieListView.setAdapter(movieAdapter);

        // Set the layout manager to a horizontal LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        movieListView.setLayoutManager(layoutManager);

        TextView viewAll = findViewById(R.id.textViewViewAll);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllMovie.class);
                startActivity(intent);
            }
        });
    }




    @Override
    public void onItemClick(String movieName) {
        // Start MoviePageActivity with the selected movieName
        Intent intent = new Intent(MainActivity.this, MoviePage.class);
        intent.putExtra("MOVIE", movieName);
        startActivity(intent);
    }



}