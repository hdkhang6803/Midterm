package com.example.midterm.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Object.Movie;
import com.example.midterm.R;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnItemClickListener onItemClickListener;

    public void setMovies(List<Movie> displayedMovies) {
        movieList = displayedMovies;
        notifyDataSetChanged();
    }

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(String movieName); // Pass any relevant data
    }

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.onItemClickListener = (OnItemClickListener) context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Set movie poster
        holder.imageViewPoster.setImageBitmap(movie.getPosterImage());

        // Set movie name
        holder.textViewMovieName.setText(movie.getMovieName());

        // Set time duration
        holder.textViewTimeDuration.setText(movie.getTimeDuration());

        holder.imageViewPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Movie movie = movieList.get(position);
                onItemClickListener.onItemClick(movie.getMovieName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPoster;
        TextView textViewMovieName;
        TextView textViewTimeDuration;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewMovieName = itemView.findViewById(R.id.textViewMovieName);
            textViewTimeDuration = itemView.findViewById(R.id.textViewTimeDuration);
        }
    }
}