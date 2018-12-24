package com.demon.demoncalendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        markData.put("2018-10-9", "0");
        markData.put("2018-10-19", "1");
        markData.put("2018-10-29", "0");
        markData.put("2018-10-10", "1");
        calendarView.setMarkData(markData);//绑定需要标记的日期
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onDateChange(CalendarDate date) {
                //日期改变时回调
                Log.i(TAG, "onDateChange: " + date.toString());
            }
        });
    }
}
