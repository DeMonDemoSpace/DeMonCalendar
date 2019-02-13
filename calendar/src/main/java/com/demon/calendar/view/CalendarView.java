package com.demon.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.demon.calendar.R;
import com.demon.calendar.component.CalendarAttr;
import com.demon.calendar.component.CalendarViewAdapter;
import com.demon.calendar.listener.OnDateListener;
import com.demon.calendar.listener.OnSelectDateListener;
import com.demon.calendar.model.CalendarItem;
import com.demon.calendar.model.CalendarDate;
import com.demon.calendar.util.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author DeMon
 * @date 2018/10/29
 * @description
 */
public class CalendarView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "CalendarView";
    private ImageView ivLast, ivToday, ivNext;
    private TextView tvDate;
    private View viewMon, viewSun;
    private MonthPager monthPager;
    private boolean isLunar, isShowToday;
    private int showDay, theme;
    public CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private ArrayList<CalendarItem> currentCalendars = new ArrayList<>();
    private OnDateListener onDateListener;
    private CalendarDate currentCalendarDate;
    private CalendarAttr.WeekArrayType weekArrayType = CalendarAttr.WeekArrayType.Monday;
    private CalendarItem currentCalendar;
    private int currentPos = MonthPager.CURRENT_DAY_INDEX;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_calendar_view, this);
        ivLast = findViewById(R.id.iv_last);
        ivLast.setOnClickListener(this);
        ivNext = findViewById(R.id.iv_next);
        ivNext.setOnClickListener(this);
        ivToday = findViewById(R.id.iv_today);
        ivToday.setOnClickListener(this);
        tvDate = findViewById(R.id.tv_date);
        monthPager = findViewById(R.id.month_pager);
        viewMon = findViewById(R.id.week_monday);
        viewSun = findViewById(R.id.week_sunday);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);
        showDay = a.getInteger(R.styleable.CalendarView_cv_day, 0);
        theme = a.getInteger(R.styleable.CalendarView_cv_theme, 0);
        isLunar = a.getBoolean(R.styleable.CalendarView_cv_isLunar, true);
        isShowToday = a.getBoolean(R.styleable.CalendarView_cv_isShowToday, true);
        if (showDay == 1) {
            viewSun.setVisibility(VISIBLE);
            viewMon.setVisibility(GONE);
            weekArrayType = CalendarAttr.WeekArrayType.Sunday;
        }
        if (!isShowToday) {
            ivToday.setVisibility(GONE);
        }
        initCurrentDate(new CalendarDate());
        initCalendarView();
        a.recycle();
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate(CalendarDate date) {
        currentCalendarDate = date;
        tvDate.setText(date.toString());
    }

    private void initCalendarView() {
        initListener();
        DayView dayView;
        if (theme == 0) {
            dayView = new DefaultDayView(getContext(), isLunar);
        } else {
            dayView = new CustomDayView(getContext(), isLunar);
        }
        calendarAdapter = new CalendarViewAdapter(
                getContext(),
                onSelectDateListener,
                CalendarAttr.CalendarType.MONTH,
                weekArrayType,
                dayView);
        initMonthPager();
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                initCurrentDate(date);
                if (onDateListener != null) {
                    onDateListener.onDateChange(date);
                }
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(currentPos);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        currentCalendars = calendarAdapter.getPagers();
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentCalendar = currentCalendars.get(position % currentCalendars.size());
                if (position != MonthPager.CURRENT_DAY_INDEX) {
                    tvDate.setText(currentCalendar.getSeedDate().toString());
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int position, int state) {
                if (state == 0 && currentCalendar != null && currentCalendar.getSeedDate().getDay() == 1) {
                    currentCalendar.selectDefaultDate();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_last) {
            monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
        } else if (id == R.id.iv_next) {
            monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
        } else if (id == R.id.iv_today) {
            CalendarDate today = new CalendarDate();
            calendarAdapter.notifyDataChanged(today);
            initCurrentDate(today);
            if (onDateListener != null) {
                onDateListener.onDateChange(today);
            }
        }
    }


    /**
     * 初始化标记数据，HashMap的形式，可自定义
     * 如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
     */
    public void setMarkData(HashMap<String, String> markData) {
        calendarAdapter.setMarkData(markData);
    }

    public void refreshMarkData(HashMap<String, String> markData) {
        calendarAdapter.setMarkData(markData);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calendarAdapter.invalidateCurrentCalendar();
            }
        }, 200);
    }

    public void setOnDateListener(OnDateListener onDateListener) {
        this.onDateListener = onDateListener;
    }

    public CalendarDate getCurrentCalendarDate() {
        return currentCalendarDate;
    }
}
