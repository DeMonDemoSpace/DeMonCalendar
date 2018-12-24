package com.demon.demoncalendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.demon.calendar.listener.OnDateListener;
import com.demon.calendar.model.CalendarDate;
import com.demon.calendar.view.CalendarMemoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<String> list = new ArrayList<>();
    private CalendarMemoView calendarView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001) {
                list.clear();
                list.addAll(Arrays.asList(getResources().getStringArray(R.array.items)));
                calendarView.adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.cv);
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2018-10-9", "班");
        markData.put("2018-10-19", "休");
        markData.put("2018-10-29", "假");
        markData.put("2018-10-10", "班");
        calendarView.setMarkData(markData);
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));
        calendarView.setAdapter(new ExampleAdapter(this, list));
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                Log.i(TAG, "onSelectDate: " + date.toString());
                handler.sendEmptyMessage(0x001);
            }

            @Override
            public void onPageDateChange(CalendarDate date) {
                Log.i(TAG, "onPageDateChange: " + date.toString());
                list.clear();
                list.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));
                calendarView.adapter.notifyDataSetChanged();
            }
        });
    }
}
