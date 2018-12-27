package com.demon.demoncalendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.demon.calendar.listener.OnDateListener;
import com.demon.calendar.model.CalendarDate;
import com.demon.calendar.view.CalendarMemoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<String> list = new ArrayList<>();
    private CalendarMemoView calendarView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.cv);
        fab = findViewById(R.id.fab_refresh);
        final HashMap<String, String> markData = new HashMap<>();
        markData.put("2018-10-9", "班");
        markData.put("2018-10-19", "休");
        markData.put("2018-10-29", "假");
        markData.put("2018-10-10", "班");
        calendarView.setMarkData(markData);//绑定需要标记的日期
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));
        calendarView.setAdapter(new ExampleAdapter(this, list));//给列表绑定适配器
        calendarView.setOnDateListener(new OnDateListener() {
            @Override
            public void onDateChange(CalendarDate date) {
                //日期改变时回调
                Log.i(TAG, "onDateChange: " + date.toString());
                list.clear();
                list.addAll(Arrays.asList(getResources().getStringArray(R.array.items)));
                calendarView.adapter.notifyDataSetChanged();
            }


        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 30; i++) {
                    markData.put(getDay(i), "班");
                }
                calendarView.refreshMarkData(markData);
            }
        });
    }

    /**
     * 获取当前时间的第n天
     *
     * @param n
     * @return
     */
    public static String getDay(int n) {
        Date date = getNextDay(new Date(), n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date getNextDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, n);
        date = calendar.getTime();
        return date;
    }
}
