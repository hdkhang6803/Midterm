package com.example.midterm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.R;

import java.util.ArrayList;
import java.util.List;

public class NameAdapter  extends RecyclerView.Adapter<NameAdapter.NameViewHolder> {

    private NameAdapter.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onNameClick(String movieName); // Pass any relevant data
    }

    private Context context;
    private List<String> originalNames;  // Original list of names
    private List<String> filteredNames;  // Filtered list of names

    public NameAdapter(Context context, List<String> names) {
        this.context = context;
        this.originalNames = new ArrayList<>(names);
        this.filteredNames = new ArrayList<>(names);
        this.onItemClickListener = (NameAdapter.OnItemClickListener) context;
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_name, parent, false);
        return new NameViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        String movie = filteredNames.get(position);

        // Set movie name
        holder.textViewMovieName.setText(movie);


        holder.textViewMovieName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onNameClick(movie);

            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredNames.size();
    }

    public void filter(String query) {
        query = query.toLowerCase().trim();

        filteredNames.clear();
        if (query.isEmpty()) {
            filteredNames.addAll(originalNames);
        } else {
            for (String name : originalNames) {
                if (name.toLowerCase().contains(query)) {
                    filteredNames.add(name);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class NameViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMovieName;

        NameViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMovieName = itemView.findViewById(R.id.text1);
        }
    }





}