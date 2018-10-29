package com.demon.calendar.listener;

import android.graphics.Canvas;

import com.demon.calendar.model.Day;

/**
 * Created by ldf on 17/6/26.
 */

public interface IDayRenderer {

    void refreshContent();

    void drawDay(Canvas canvas, Day day);

    IDayRenderer copy();

}
