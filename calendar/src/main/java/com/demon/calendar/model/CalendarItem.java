package com.demon.calendar.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.demon.calendar.Const;
import com.demon.calendar.component.CalendarAttr;
import com.demon.calendar.component.CalendarRenderer;
import com.demon.calendar.listener.IDayRenderer;
import com.demon.calendar.listener.OnAdapterSelectListener;
import com.demon.calendar.listener.OnSelectDateListener;
import com.demon.calendar.util.CalendarUtil;

import java.util.Calendar;

@SuppressLint("ViewConstructor")
public class CalendarItem extends View {
    /**
     * 日历列数
     */
    private CalendarAttr.CalendarType calendarType;
    private int cellHeight; // 单元格高度
    private int cellWidth; // 单元格宽度

    private OnSelectDateListener onSelectDateListener;    // 单元格点击回调事件
    private Context context;
    private CalendarAttr calendarAttr;
    private CalendarRenderer renderer;

    private OnAdapterSelectListener onAdapterSelectListener;
    private float touchSlop;

    public CalendarItem(Context context,
                    OnSelectDateListener onSelectDateListener,
                    CalendarAttr attr) {
        super(context);
        this.onSelectDateListener = onSelectDateListener;
        calendarAttr = attr;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        touchSlop = CalendarUtil.getTouchSlop(context);
        initAttrAndRenderer();
    }

    private void initAttrAndRenderer() {
        renderer = new CalendarRenderer(this, calendarAttr, context);
        renderer.setOnSelectDateListener(onSelectDateListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderer.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        cellHeight = h / Const.TOTAL_ROW;
        cellWidth = w / Const.TOTAL_COL;
        calendarAttr.setCellHeight(cellHeight);
        calendarAttr.setCellWidth(cellWidth);
        renderer.setAttr(calendarAttr);
    }

    private float posX = 0;
    private float posY = 0;

    /*
     * 触摸事件为了确定点击的位置日期
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                posX = event.getX();
                posY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - posX;
                float disY = event.getY() - posY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (posX / cellWidth);
                    int row = (int) (posY / cellHeight);
                    onAdapterSelectListener.cancelSelectState();
                    renderer.onClickDate(col, row);
                    onAdapterSelectListener.updateSelectState();
                    invalidate();
                }
                break;
        }
        return true;
    }

    public CalendarAttr.CalendarType getCalendarType() {
        return calendarAttr.getCalendarType();
    }

    public void switchCalendarType(CalendarAttr.CalendarType calendarType) {
        calendarAttr.setCalendarType(calendarType);
        renderer.setAttr(calendarAttr);
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void resetSelectedRowIndex() {
        renderer.resetSelectedRowIndex();
    }

    public int getSelectedRowIndex() {
        return renderer.getSelectedRowIndex();
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        renderer.setSelectedRowIndex(selectedRowIndex);
    }

    public void setOnAdapterSelectListener(OnAdapterSelectListener onAdapterSelectListener) {
        this.onAdapterSelectListener = onAdapterSelectListener;
    }

    public void showDate(CalendarDate current) {
        renderer.showDate(current);
    }

    public void updateWeek(int rowCount) {
        renderer.updateWeek(rowCount);
        invalidate();
    }

    public void update() {
        renderer.update();
    }

    public void cancelSelectState() {
        renderer.cancelSelectState();
    }

    public CalendarDate getSeedDate() {
        return renderer.getSeedDate();
    }

    public void setDayRenderer(IDayRenderer dayRenderer) {
        renderer.setDayRenderer(dayRenderer);
    }


    public void selectDate(CalendarDate date) {
        for (int row = 0; row < Const.TOTAL_ROW; row++) {
            for(int col = 0; col < Const.TOTAL_COL; col++ ){
                CalendarDate calendarDate = renderer.getDate(row,col);
                if(calendarDate != null
                        && date.getYear() == calendarDate.getYear()
                        && date.getMonth() == calendarDate.getMonth()
                        && date.getDay() == calendarDate.getDay()
                        ){
                    onAdapterSelectListener.cancelSelectState();
                    renderer.onClickDate(col, row);
                    onAdapterSelectListener.updateSelectState();
                }
            }
        }
    }

    /**
     * 选中页面中的默认天：<br/>
     * 1. 如果是月视图 = 是本月？今天：本月1号
     * 2. 如果是周视图 = 本周1号
     */
    public void selectDefaultDate() {
        if (CalendarAttr.CalendarType.MONTH == getCalendarType()) {
            CalendarDate date = new CalendarDate(renderer.getSeedDate().year, renderer.getSeedDate().month, 1);
            Calendar todayCalendar = Calendar.getInstance();
            if (isSameMonth(todayCalendar, getSeedDate())) {
                date = calendarDateForToday(todayCalendar);
            }
            for (int row = 0; row < Const.TOTAL_ROW; row++) {
                for (int col = 0; col < Const.TOTAL_COL; col++) {
                    CalendarDate calendarDate = renderer.getDate(row, col);
                    if (calendarDate != null
                            && date.getYear() == calendarDate.getYear()
                            && date.getMonth() == calendarDate.getMonth()
                            && date.getDay() == calendarDate.getDay()
                            ) {
                        //onAdapterSelectListener.cancelSelectState();
                        renderer.onClickDate(col, row);
                        onAdapterSelectListener.updateSelectState();
                    }
                }
            }
        } else if (CalendarAttr.CalendarType.WEEK == getCalendarType()) {
            //onAdapterSelectListener.cancelSelectState();
            renderer.onClickDate(0, renderer.getSelectedRowIndex());
            onAdapterSelectListener.updateSelectState();
        }
    }

    private CalendarDate calendarDateForToday(Calendar calendar) {
        return new CalendarDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private boolean isSameMonth(Calendar calendar, CalendarDate currentDate) {
        return calendar.get(Calendar.MONTH) + 1 == currentDate.getMonth()
                && calendar.get(Calendar.YEAR) == currentDate.getYear();
    }

    public CalendarRenderer getRenderer() {
        return renderer;
    }

}