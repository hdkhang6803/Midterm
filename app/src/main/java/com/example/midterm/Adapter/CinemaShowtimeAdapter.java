package com.example.midterm.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.DAO.SeatDAO;
import com.example.midterm.DataHelper;
import com.example.midterm.R;
import com.example.midterm.Object.Show;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinemaShowtimeAdapter extends RecyclerView.Adapter<CinemaShowtimeAdapter.CinemaViewHolder> implements TimeAdapter.OnTimeItemClickListener {

    private Context context;
    private List<Show> showList;
    private Map<String, List<String>> cinemaShowtimesMap;

    List<Map.Entry<String, List<String>>> entryList;

    public CinemaShowtimeAdapter(Context context, List<Show> showList) {
        this.context = context;
        this.showList = showList;
        this.cinemaShowtimesMap = organizeShowtimesByCinema(showList);
        this.entryList = new ArrayList<>(cinemaShowtimesMap.entrySet());
    }

    public void setShowList(List<Show> showList) {
        this.showList = showList;
        this.cinemaShowtimesMap = organizeShowtimesByCinema(showList);
        this.entryList = new ArrayList<>(cinemaShowtimesMap.entrySet());
        notifyDataSetChanged();
    }

    private Map<String, List<String>> organizeShowtimesByCinema(List<Show> showList) {
        Map<String, List<String>> resultMap = new HashMap<>();
        if(showList.isEmpty()){
            return resultMap;
        }
        for (Show show : showList){
            System.out.println(show);
        }

        for (Show show : showList) {
            String cinemaName = show.getPlayAt().getCinemaName();
            String startTime = show.getShowStartTime();

            // Check if cinemaName is already a key in the map
            if (resultMap.containsKey(cinemaName)) {
                resultMap.get(cinemaName).add(startTime);
            } else {
                List<String> startTimeList = new ArrayList<>();
                startTimeList.add(startTime);
                resultMap.put(cinemaName, startTimeList);
            }
        }

        return resultMap;
    }

    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cinema_showtime, parent, false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Map.Entry<String, List<String>> entry = entryList.get(position);

        // Access the key and value
        String cinemaName = entry.getKey();
        List<String> startTimeList = entry.getValue();
        List<Show> startTimeandStatus = new ArrayList<>();
        for (String time : startTimeList){
            for(Show show:showList){
                if (show.getPlayAt().getCinemaName().equals(cinemaName) && show.getShowStartTime().equals(time)){
                   SeatDAO.getCinemaReservedSeat(cinemaName, show.getPlayAt().getHallId()
                            , DataHelper.convertDateToString(show.getDateOfShow()), time, new SeatDAO.SeatCallback() {
                                @Override
                                public void onSeatsRetrieved(List<Pair> reserved) {
                                    int rows = show.getPlayAt().getRowCount();
                                    int cols = show.getPlayAt().getColCount();
                                    if (reserved.size() == rows * cols){
                                        show.setStatus(Show.Status.FULL);
                                        notifyDataSetChanged();
                                    }
                                }
                            });

                    startTimeandStatus.add(show);
                }
            }
        }

        // Set movie name
        holder.textViewMovieName.setText(cinemaName);

        // Set up Showtime RecyclerView
        TimeAdapter showtimeAdapter = new TimeAdapter(context, startTimeandStatus, this);
        holder.recyclerViewShowtime.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.recyclerViewShowtime.setAdapter(showtimeAdapter);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public void onTimeItemClick(Show clickedShow) {
        // Handle the click event from TimeAdapter
        for (Show show : showList) {
            if (show.getStatus() == Show.Status.SELECTED) {
                show.setStatus(Show.Status.AVAILABLE);
            }
        }
        notifyDataSetChanged();
    }


    static class CinemaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMovieName;
        RecyclerView recyclerViewShowtime;

        CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMovieName = itemView.findViewById(R.id.textViewMovieName);
            recyclerViewShowtime = itemView.findViewById(R.id.recyclerViewShowtime);
        }
    }
}
