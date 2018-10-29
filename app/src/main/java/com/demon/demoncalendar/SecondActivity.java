package com.demon.demoncalendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.demon.calendar.listener.OnDateListener;
import com.demon.calendar.model.CalendarDate;
import com.demon.calendar.view.CalendarMemoView;
import com.demon.calendar.view.CalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        calendarView = findViewById(R.id.cv);
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2018-10-9", "班");
        markData.put("2018-10-19", "休");
        markData.put("2018-10-29", "假");
        markData.put("2018-10-10", "班");
        calendarView.setMarkData(markData);
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {

            }

            @Override
            public void onPageDateChange(CalendarDate date) {

            }
        });
    }
}
