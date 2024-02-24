package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midterm.Adapter.SeatAdapter;
import com.example.midterm.Adapter.SeatConfirmAdapter;
import com.example.midterm.DAO.CinemaDAO;
import com.example.midterm.DAO.MovieDAO;
import com.example.midterm.DAO.SeatDAO;
import com.example.midterm.DAO.ShowDAO;
import com.example.midterm.DAO.UserDAO;
import com.example.midterm.Object.Booking;
import com.example.midterm.Object.CinemaHall;
import com.example.midterm.Object.CinemaSeat;
import com.example.midterm.Object.Show;
import com.example.midterm.Object.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeatBooking extends AppCompatActivity {
    private CinemaSeat[][] seats;
    private ArrayList<Integer> rowIdx;
    private ArrayList<Integer> colIdx;
    private Show m_show;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve data from the intent extras
        String cinemaName = intent.getStringExtra("CINEMA");
        int hallId = intent.getIntExtra("HALL", -1); // -1 is the default value in case the extra is not found
        Date date = (Date) intent.getSerializableExtra("DATE");
        String movieName = intent.getStringExtra("MOVIE");
        String time = intent.getStringExtra("TIME");

        initView(cinemaName,movieName,date,time);

        if (hallId == -1){
            Log.e("CinemaHall", "hall not found");
            return;
        }

        List<Show> m_show_ = ShowDAO.getShowsByCinemaMovieNameDateStartTime(this,cinemaName,movieName,DataHelper.convertDateToString(date), time);
        if(m_show_.isEmpty()){
            Log.e("GETSEATS", "NO MATCH SHOW");
            return;
        }



        m_show = m_show_.get(0);
        seats = m_show.getSeats();
        RecyclerView gridView = findViewById(R.id.seatGrid);
        gridView.setLayoutManager(new GridLayoutManager(getBaseContext(), seats[0].length, GridLayoutManager.VERTICAL, false));
        SeatAdapter seatAdapter = new SeatAdapter(getBaseContext(), seats, findViewById(R.id.ticketNum), findViewById(R.id.totalMoney));
        gridView.setAdapter(seatAdapter);

        m_show.getSeats(new Show.SeatCallback() {
            @Override
            public void onSeatsRetrieved(CinemaSeat[][] retrievedSeats) {
                seats = retrievedSeats;
                seatAdapter.setSeats(seats);

            }
        });
    }

    private void initView(String cinemaName, String movieName, Date date, String time){
        TextView movieNameView = findViewById(R.id.movieName);
        movieNameView.setText(movieName);

        TextView cineNameView = findViewById(R.id.cinemaTextView);
        cineNameView.setText(cinemaName);

        TextView dateView = findViewById(R.id.dateTextView);
        dateView.setText(DataHelper.convertDateToString(date));

        TextView timeView = findViewById(R.id.timeTextView);
        timeView.setText(time);

        ImageView poster = findViewById(R.id.moviePoster);
        poster.setImageBitmap(MovieDAO.getPosterByMovieName(this, movieName));

        RecyclerView seatString = findViewById(R.id.allSelectedSeats);
        SeatConfirmAdapter adapter = new SeatConfirmAdapter(getBaseContext(),new ArrayList<>(), new ArrayList<>());
        seatString.setAdapter(adapter);

        ImageView backButt = findViewById(R.id.backButton);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView nextButton = findViewById(R.id.myNextButton1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                UserDAO.getUserbyUID(user.getUid(), new UserDAO.UserCallback() {
                    @Override
                    public void onUserRetrieved(User m_user) {
                        TextInputEditText userPhoneInput = findViewById(R.id.userPhone);
                        userPhoneInput.setText(m_user.getPhoneNum());

                        rowIdx = new ArrayList<>();
                        colIdx = new ArrayList<>();
                        for (int i = 0; i < seats.length; i++){
                            for(int j = 0; j < seats[0].length;j++){
                                if (seats[i][j].getStatus() == CinemaSeat.Status.SELECTED){
                                    rowIdx.add(i);
                                    colIdx.add(j);
                                }
                            }
                        }
                        if (rowIdx.isEmpty()){
                            CharSequence text = "Must select seat(s) first !";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getBaseContext(), text, duration);
                            toast.show();
                            return;
                        }

                        RelativeLayout confirmBox = findViewById(R.id.confirmBox);
                        confirmBox.setVisibility(View.VISIBLE);

                        userPhoneInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(hasFocus){
                                    userPhoneInput.setText("");
                                    userPhoneInput.setTextColor(R.color.black);
                                }
                            }
                        });

                        TextView costConfirm = findViewById(R.id.totalMoneyConfirm);
                        TextView totalCost = findViewById(R.id.totalMoney);
                        costConfirm.setText(DataHelper.formatMoney(totalCost.getText().toString()));

                        RecyclerView seatString = findViewById(R.id.allSelectedSeats);
                        SeatConfirmAdapter adapter = new SeatConfirmAdapter(getBaseContext(),rowIdx,colIdx);
                        seatString.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
                        seatString.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public void onCancelClick(View view){
        RelativeLayout confirmBox = findViewById(R.id.confirmBox);
        confirmBox.setVisibility(View.INVISIBLE);
    }
    public void onConfirmClick(View view){
        TextInputEditText userPhoneInput = findViewById(R.id.userPhone);
        String userPhone = userPhoneInput.getText().toString();
        String cineName = m_show.getPlayAt().getCinemaName();
        int hallID = m_show.getPlayAt().getHallId();
        String date = DataHelper.convertDateToString(m_show.getDateOfShow());
        String startTime = m_show.getShowStartTime();

        ArrayList<String> bookings = new ArrayList<>();
        for(int i = 0; i < rowIdx.size(); i++){
            SeatDAO.insertSeat(this,cineName, hallID, date, startTime, rowIdx.get(i),colIdx.get(i),userPhone);
            Booking this_book = new Booking(cineName, hallID, date, startTime, rowIdx.get(i),colIdx.get(i),userPhone);
            bookings.add(this_book.toJson());
        }

        CharSequence text = "Seat booking done! Please wait for QR!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getBaseContext(), text, duration);
        toast.show();

        Intent intent = new Intent(this, ConfirmQR.class);

        Gson gson = new Gson();
        String json_bookings = gson.toJson(bookings);

        System.out.println(json_bookings);
        intent.putExtra("INFO", json_bookings);
        finish();
        startActivity(intent);
    }
}