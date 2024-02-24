package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.midterm.Object.Booking;
import com.google.gson.Gson;
import com.google.zxing.Dimension;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class ConfirmQR extends AppCompatActivity {
    private String json_bookings;
    private Bitmap bitmap;
    private String savePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_qr);

        savePath = Environment.getExternalStorageDirectory().toString();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                json_bookings= null;
                CharSequence text = "Create QR failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getBaseContext(), text, duration);
                toast.show();
                Log.e("QR","null extras");
                return;
            } else {
                json_bookings= extras.getString("INFO");
                Log.e("QR", "caught");
            }
        } else {
            json_bookings = (String) savedInstanceState.getSerializable("INFO");
            Log.e("QR", "3rd");
        }

        ImageView qrImage = findViewById(R.id.qrImage);

        QRGEncoder qrgEncoder = new QRGEncoder(json_bookings,null, QRGContents.Type.TEXT, 700);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);

        try {
            bitmap = qrgEncoder.getBitmap();

            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("QR","Create image QR failed");
        }

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save = false;

                String filename = DataHelper.getCurrentTimestampString();
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, filename , "");
                    save = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String result = save ? "Image Saved" : "Image Not Saved";
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        Button mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                ArrayList<String> bookings = gson.fromJson(json_bookings, ArrayList.class);
                Booking booking = null;
                for(String str : bookings){
                    booking = gson.fromJson(str, Booking.class);
                    break;
                }
                String dest = booking.getCineName();
                System.out.println( " **** " + dest);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);

                String geoUri = "geo:0,0?q=" + Uri.encode(dest);
                mapIntent.setData(Uri.parse(geoUri));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

    }


}