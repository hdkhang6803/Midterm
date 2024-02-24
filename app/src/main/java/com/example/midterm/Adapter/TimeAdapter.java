package com.example.midterm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.midterm.DAO.SeatDAO;
import com.example.midterm.DataHelper;
import com.example.midterm.Object.CinemaSeat;
import com.example.midterm.Object.Show;
import com.example.midterm.R;
import com.example.midterm.SeatBooking;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {

    private Context context;
    private List<Show> showList;
    private OnTimeItemClickListener onTimeItemClickListener;
    public interface OnTimeItemClickListener {
        void onTimeItemClick(Show clickedShow);
    }


    public TimeAdapter(Context context, List<Show> list, OnTimeItemClickListener clickListener) {
        this.context = context;
        this.showList = list;
        this.onTimeItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        Show showInfo = showList.get(position);
//        Log.e("BIND TIMEADAPTER", showInfo.getStatus().toString());
        holder.textViewTime.setText(showInfo.getShowStartTime());

        // Update UI based on the status
        if (showInfo.getStatus() == Show.Status.SELECTED) {
            holder.textViewTime.setBackgroundResource(R.drawable.selected_background);
            holder.textViewTime.setTextColor(context.getResources().getColor(android.R.color.black));
        } else if (showInfo.getStatus() == Show.Status.AVAILABLE) {
            holder.textViewTime.setBackgroundResource(R.drawable.available_background);
            holder.textViewTime.setTextColor(context.getResources().getColor(android.R.color.black));
        } else {
            holder.textViewTime.setBackgroundResource(R.drawable.unavailable_background);
            holder.textViewTime.setTextColor(context.getResources().getColor(android.R.color.white));
        }

        holder.textViewTime.setTag(showInfo);
        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showInfo.getStatus() == Show.Status.FULL)
                    return;
                onTimeItemClickListener.onTimeItemClick(showInfo);
                showInfo.setStatus(Show.Status.SELECTED);
                int hallId = showInfo.getPlayAt().getHallId();
                String cinemaName = showInfo.getPlayAt().getCinemaName();

                Log.e("Time click", "Hall ID: " + hallId + ", Cinema Name: " + cinemaName+", Status: "+showInfo.getStatus());


            }
        });
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }

    static class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;

        TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}
