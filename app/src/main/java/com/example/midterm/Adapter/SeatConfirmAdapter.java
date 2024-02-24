package com.example.midterm.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.DataHelper;
import com.example.midterm.R;

import java.util.ArrayList;
import java.util.List;

public class SeatConfirmAdapter  extends RecyclerView.Adapter<SeatConfirmAdapter.NameViewHolder> {


    private Context context;
    private List<Integer> rowIdx;
    private List<Integer> colIdx;

    public SeatConfirmAdapter(Context context, List<Integer> rowIdx, List<Integer> colIdx) {
        this.context = context;
        this.colIdx = colIdx;
        this.rowIdx = rowIdx;
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_confirm, parent, false);
        return new NameViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        Integer m_rowIdx = this.rowIdx.get(position);
        Integer m_colIdx = this.colIdx.get(position);

        String res = DataHelper.integerToAlphabet(m_rowIdx) + String.valueOf(m_colIdx);
        holder.textViewSeat.setText(res);
    }

    @Override
    public int getItemCount() {
        return rowIdx.size();
    }


    static class NameViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSeat;

        NameViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSeat = itemView.findViewById(R.id.seatConfirmString);
        }
    }
}