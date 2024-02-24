package com.example.midterm.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Object.CinemaSeat;
import com.example.midterm.R;
public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private Context context;
    private CinemaSeat[][] seats;
    private TextView num;
    private TextView cost;

    public SeatAdapter(Context context, CinemaSeat[][] seats, TextView num, TextView cost) {
        this.context = context;
        this.seats = seats;
        this.num = num;
        this.cost = cost;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        CinemaSeat seat = getItem(position);

        // Set the background based on the seat status (Assuming you have appropriate drawables)
        if (seat.getStatus() == CinemaSeat.Status.AVAILABLE) {
            holder.seatImageView.setBackgroundResource(R.drawable.circle_available);
        } else if (seat.getStatus() == CinemaSeat.Status.RESERVED) {
            holder.seatImageView.setBackgroundResource(R.drawable.circle_reserved);
        }

        // Calculate and set item size based on the number of columns and rows
//        ViewGroup.LayoutParams layoutParams_1 = holder.itemView.getLayoutParams();
//        int recyclerViewWidth = layoutParams_1.width;
//        int recyclerViewHeight = layoutParams_1.height;
//        int numColumns = seats[0].length;
//        int numRows = seats.length;
//
//        int itemWidth = recyclerViewWidth / numColumns;
//        int itemHeight = recyclerViewHeight / numRows;
//        Log.e("ITEM SEATs", String.valueOf(itemHeight) + " " + String.valueOf(itemWidth));

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidth, itemHeight);
//        layoutParams.setMargins(10, 10, 10, 10);
//        holder.seatImageView.setLayoutParams(layoutParams);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Seat click", seat.getStatus().toString());
                if (seat.getStatus() == CinemaSeat.Status.AVAILABLE) {
                    holder.seatImageView.setBackgroundResource(R.drawable.circle_selected);
                    seat.setStatus(CinemaSeat.Status.SELECTED);
                    int cur_num = Integer.parseInt(num.getText().toString());
                    int cur_cost = Integer.parseInt(cost.getText().toString());
                    num.setText(String.valueOf(cur_num + 1));
                    cost.setText(String.valueOf(cur_cost + seat.getPrice()));
                } else if (seat.getStatus() == CinemaSeat.Status.SELECTED) {
                    holder.seatImageView.setBackgroundResource(R.drawable.circle_available);
                    seat.setStatus(CinemaSeat.Status.AVAILABLE);
                    int cur_num = Integer.parseInt(num.getText().toString());
                    int cur_cost = Integer.parseInt(cost.getText().toString());
                    num.setText(String.valueOf(cur_num - 1));
                    cost.setText(String.valueOf(cur_cost - seat.getPrice()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return seats.length * seats[0].length;
    }

    private CinemaSeat getItem(int position) {
        int row = position / seats[0].length;
        int col = position % seats[0].length;
        return seats[row][col];
    }

    public void setSeats(CinemaSeat[][] seats) {
        this.seats = seats;
        notifyDataSetChanged();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        ImageView seatImageView;

        public SeatViewHolder(View itemView) {
            super(itemView);
            seatImageView = itemView.findViewById(R.id.seatImageView);
        }
    }
}

//
//public class SeatAdapter extends BaseAdapter {
//
//    private Context context;
//    private CinemaSeat[][] seats;
//
//    private TextView num;
//    private TextView cost;
//    public SeatAdapter(Context context, CinemaSeat[][] seats, TextView num, TextView cost) {
//        this.context = context;
//        this.seats = seats;
//        this.num = num;
//        this.cost = cost;
//    }
//
//    @Override
//    public int getCount() {
//        return seats.length * seats[0].length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        int row = position / seats[0].length;
//        int col = position % seats[0].length;
//        return seats[row][col];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View seatView = convertView;
//        if (seatView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            seatView = inflater.inflate(R.layout.item_seat, null);
//        }
//
//        CinemaSeat seat = (CinemaSeat) getItem(position);
//        ImageView seatImageView = seatView.findViewById(R.id.seatImageView);
//
//        // Set the background based on the seat status (Assuming you have appropriate drawables)
//        if (seat.getStatus() == CinemaSeat.Status.AVAILABLE) {
//            seatImageView.setBackgroundResource(R.drawable.circle_available);
//        } else if (seat.getStatus() == CinemaSeat.Status.RESERVED) {
//            seatImageView.setBackgroundResource(R.drawable.circle_reserved);
//        }
//        // Calculate and set item size based on the number of columns and rows
//        int gridViewWidth = parent.getWidth();
//        int gridViewHeight = parent.getHeight();
//        int numColumns = seats[0].length;
//        int numRows = seats.length;
//
//        int itemWidth = gridViewWidth / numColumns;
//        int itemHeight = gridViewHeight / numRows;
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidth, itemHeight);
//        layoutParams.setMargins(10,10,10,10);
//        seatImageView.setLayoutParams(layoutParams);
//
//
//        seatView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("Seat click",seat.getStatus().toString());
//                if (seat.getStatus() == CinemaSeat.Status.AVAILABLE){
//                    seatImageView.setBackgroundResource(R.drawable.circle_selected);
//                    seat.setStatus(CinemaSeat.Status.SELECTED);
//                    int cur_num = Integer.parseInt(num.getText().toString());
//                    int cur_cost = Integer.parseInt(cost.getText().toString());
//                    num.setText(String.valueOf(cur_num + 1));
//                    cost.setText(String.valueOf(cur_cost + seat.getPrice()));
//                } else if (seat.getStatus() == CinemaSeat.Status.SELECTED) {
//                    seatImageView.setBackgroundResource(R.drawable.circle_available);
//                    seat.setStatus(CinemaSeat.Status.AVAILABLE);
//                    int cur_num = Integer.parseInt(num.getText().toString());
//                    int cur_cost = Integer.parseInt(cost.getText().toString());
//                    num.setText(String.valueOf(cur_num - 1));
//                    cost.setText(String.valueOf(cur_cost - seat.getPrice()));
//                }
//            }
//        });
//
//        return seatView;
//    }
//}
