package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.midterm.Adapter.MovieAdapter;
import com.example.midterm.Adapter.NameAdapter;
import com.example.midterm.DAO.MovieDAO;
import com.example.midterm.Object.Movie;

import java.util.ArrayList;
import java.util.List;

public class AllMovie extends AppCompatActivity implements MovieAdapter.OnItemClickListener, NameAdapter.OnItemClickListener {
    private List<Movie> displayedMovies;
    private List<Movie> allMovies;
    private List<String> allMoviesName;
    private RecyclerView movieNameLV;
    private NameAdapter nameAdapter;
    private MovieAdapter adapter;
    private  SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movie);
        allMovies = MovieDAO.getAllMoviesInfo(this);

        getAllMoviesNames();
        nameAdapter = new NameAdapter(this, allMoviesName);
        movieNameLV = findViewById(R.id.movieNameLV);
        movieNameLV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false));
        movieNameLV.setAdapter(nameAdapter);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("text submit", query);
                movieNameLV.setVisibility(View.INVISIBLE);
                filterMovies(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the displayedMovies based on the search query
                Log.e("text change", newText);
                movieNameLV.setVisibility(View.VISIBLE);

                if(newText.equals("")){
                    movieNameLV.setVisibility(View.INVISIBLE);
                    adapter.setMovies(allMovies);
                    return false;
                }
                nameAdapter.filter(newText);

                return true;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new MovieAdapter(this, allMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        displayedMovies = new ArrayList<>(allMovies);
    }

    private void filterMovies(String query) {
        displayedMovies.clear();

        for (Movie movie : allMovies) {
            Log.e("Movie",movie.getMovieName().toLowerCase() );
            if (movie.getMovieName().toLowerCase().contains(query.toLowerCase())) {
                Log.e("Query",query.toLowerCase() );
                displayedMovies.add(movie);
            }
        }

        adapter.setMovies(displayedMovies);
    }

    private void getAllMoviesNames() {
        allMoviesName = new ArrayList<>();
        for (Movie movie : allMovies) {

            allMoviesName.add(movie.getMovieName());
        }

    }


    @Override
    public void onItemClick(String movieName) {
        // Start MoviePageActivity with the selected movieName
        Intent intent = new Intent(AllMovie.this, MoviePage.class);
        intent.putExtra("MOVIE", movieName);
        startActivity(intent);
    }
    @Override
    public void onNameClick(String movieName) {
        searchView.setQuery(movieName, false);
    }
}