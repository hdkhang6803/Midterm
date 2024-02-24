package com.example.midterm.DAO;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.example.midterm.Object.Booking;
import com.example.midterm.Object.Show;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SeatDAO {

    public interface SeatCallback {
        void onSeatsRetrieved(List<Pair> reserved);
    }

    private static final String NODE_SEATS = "seats";

    public static void getCinemaReservedSeat(String m_cinema, int m_hallId, String m_date, String m_start_time, SeatCallback callback) {
        List<Pair> seats = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference seatsRef = database.getReference(NODE_SEATS);
        seatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                    Booking cinemaSeat = seatSnapshot.getValue(Booking.class);

                    if (cinemaSeat != null &&
                            cinemaSeat.getCineName().equals(m_cinema) &&
                            cinemaSeat.getHallID() == m_hallId &&
                            cinemaSeat.getDate().equals(m_date) &&
                            cinemaSeat.getStartTime().equals(m_start_time)) {
                        seats.add(Pair.create(cinemaSeat.getRowIdx(), cinemaSeat.getColIdx()));
                        callback.onSeatsRetrieved(seats);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GETSEATS", "Failed to read value.", error.toException());
            }
        });
    }


    public static void insertSeat(Context context, String cine_name,
                                  int hallID,
                                  String date,
                                  String startTime,
                                  int rowIdx,
                                  int colIdx,
                                  String phone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference seatsRef = database.getReference(NODE_SEATS);

        String seatId = seatsRef.push().getKey();

        if (seatId != null) {
            Booking cinemaSeat = new Booking(cine_name, hallID, date, startTime, rowIdx, colIdx, phone);
            seatsRef.child(seatId).setValue(cinemaSeat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CharSequence text = "Seat booking failed!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    });;
        } else {
            Log.e("INSERTSEAT", "Failed to generate seatId");
        }
    }
}