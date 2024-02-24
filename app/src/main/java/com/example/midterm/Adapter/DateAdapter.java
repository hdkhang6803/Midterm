package com.example.midterm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.DataHelper;
import com.example.midterm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// DateAdapter.java
public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private List<Pair> dateList;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private RecyclerView recyclerView;

    public DateAdapter(Context context, List<Pair> dateList, RecyclerView recyclerView) {
        this.dateList = dateList;
        onItemClickListener = (OnItemClickListener) context;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public interface OnItemClickListener {
        void onDateClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair dateModel = dateList.get(position);
        holder.dayOfWeekTextView.setText(dateModel.first.toString());
        holder.fullDate = dateModel.second.toString();
        holder.dateTextView.setText(holder.fullDate.substring(8, 10));

        holder.dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                onItemClickListener.onDateClick(clickedPosition);

                // Iterate through all items in dateList to reset backgrounds
                for (int i = 0; i < dateList.size(); i++) {
                    if (i == clickedPosition) {
                        // Set selected_background for the clicked item
                        holder.dateContainer.setBackgroundResource(R.drawable.selected_background);
                    } else {
                        // Set rounded_container for other items
                        RecyclerView.ViewHolder otherHolder = recyclerView.findViewHolderForAdapterPosition(i);
                        if (otherHolder instanceof ViewHolder) {
                            ((ViewHolder) otherHolder).dateContainer.setBackgroundResource(R.drawable.rounded_container);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeekTextView;
        TextView dateTextView;

        RelativeLayout dateContainer;

        String fullDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            dateContainer = itemView.findViewById(R.id.dateContainer);
        }
    }
}
