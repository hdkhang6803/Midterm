package com.example.midterm;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DataHelper {




    // Helper function to convert drawable resource ID to Bitmap
    public static Bitmap getBitmapFromDrawable(Context context, int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    // Helper function to parse release date string to Date object
    public static Date parseDateFromString(String releaseDateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(releaseDateStr);
        } catch (ParseException e) {
            Log.e("Date", "Error parsing");
            return null;

        }
    }
    public static String convertDateToString(Date date) {
        if (date == null) {
            return null;
        }

        // Specify the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Format the date and return the string
        return dateFormat.format(date);
    }
    public static String getCurrentMonthYear() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Format the date to "YYYY-MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return currentDate.format(formatter);
    }
    public static String integerToAlphabet(int num) {
        StringBuilder result = new StringBuilder();

        while (num >= 0) {
            result.insert(0, (char) ('A' + num % 26));
            num = (num / 26) - 1; // Adjust for 0-based indexing
            if (num < 0) {
                break;
            }
        }

        return result.toString();
    }
    public static String getCurrentTimestampString() {
       Long timeStamp = (System.currentTimeMillis()/1000);
        return timeStamp.toString();
    }
    public static String formatMoney(String amount) {
        try {
            // Parse the string to a long (assuming integer money value)
            long moneyValue = Long.parseLong(amount);

            // Create a formatter with the Vietnamese locale and pattern
            NumberFormat formatter = DecimalFormat.getNumberInstance(new Locale("vi", "VN"));
            ((DecimalFormat) formatter).applyPattern("#.###");

            // Format the money value
            return formatter.format(moneyValue);
        } catch (NumberFormatException e) {
            // Handle invalid input gracefully
            e.printStackTrace();
            return "Invalid input";
        }
    }



    public static boolean isCsvFileEmpty(Context context, String file_name) {
        try {
            InputStream inputStream = context.getAssets().open(file_name);
            if (inputStream == null) {
                return true;
            }
            InputStreamReader reader = new InputStreamReader(inputStream);

            CSVReader csvReader = new CSVReader(reader);
            List<String[]> data = csvReader.readAll();
            return data.isEmpty();
        }catch (IOException e){
            e.printStackTrace();
            return true;
        }catch (CsvException e){
            e.printStackTrace();
            return true;
        }
    }
}
