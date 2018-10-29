package com.demon.calendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demon.calendar.R;
import com.demon.calendar.component.State;
import com.demon.calendar.listener.IDayRenderer;
import com.demon.calendar.model.CalendarDate;
import com.demon.calendar.util.CalendarUtil;

/**
 * Created by ldf on 17/6/26.
 */

@SuppressLint("ViewConstructor")
public class DefaultDayView extends DayView {

    private TextView dateTv;
    private TextView lunarTv;
    private ImageView marker;
    private LinearLayout layout;
    private boolean isLunar;
    private final CalendarDate today = new CalendarDate();

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public DefaultDayView(Context context, boolean isLunar) {
        super(context, R.layout.default_day);
        this.isLunar = isLunar;
        dateTv = findViewById(R.id.date);
        lunarTv = findViewById(R.id.lunar);
        layout = findViewById(R.id.layout);
        marker = findViewById(R.id.maker);
        if (!isLunar) {
            lunarTv.setVisibility(GONE);
        }
    }

    @Override
    public void refreshContent() {
        renderToday(day.getDate());
        renderSelect(day.getState());
        renderMarker(day.getDate(), day.getState());
        super.refreshContent();
    }

    private void renderMarker(CalendarDate date, State state) {
        if (CalendarUtil.loadMarkData().containsKey(date.toString())) {
            marker.setVisibility(VISIBLE);
            if (CalendarUtil.loadMarkData().get(date.toString()).equals("0")) {
                marker.setEnabled(true);
            } else {
                marker.setEnabled(false);
            }

        } else {
            marker.setVisibility(GONE);
        }
    }

    private void renderSelect(State state) {
        if (state == State.SELECT) {
            dateTv.setTextColor(Color.WHITE);
            lunarTv.setTextColor(Color.WHITE);
            layout.setEnabled(true);
        } else if (state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            dateTv.setTextColor(Color.GRAY);
            lunarTv.setTextColor(Color.GRAY);
            layout.setEnabled(false);
        } else {
            dateTv.setTextColor(Color.BLACK);
            lunarTv.setTextColor(Color.BLACK);
            layout.setEnabled(false);
        }
    }

    private void renderToday(CalendarDate date) {
        if (date != null) {
            if (date.equals(today)) {
                dateTv.setText("今");
                layout.setEnabled(true);
            } else {
                dateTv.setText(date.day + "");
                layout.setEnabled(false);
            }

            if (date.getChinaDay().equals("初一")) {
                lunarTv.setText(date.getChinaMonth());
            } else {
                lunarTv.setText(date.getChinaDay());
            }
        }
    }

    @Override
    public IDayRenderer copy() {
        return new DefaultDayView(context, isLunar);
    }
}
