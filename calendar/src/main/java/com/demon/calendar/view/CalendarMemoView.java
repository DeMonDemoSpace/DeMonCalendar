package com.demon.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.demon.calendar.model.Calendar;
import com.demon.calendar.model.CalendarDate;
import com.demon.calendar.util.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author DeMon
 * @date 2018/10/29
 * @description
 */
public class CalendarMemoView extends FrameLayout implements View.OnClickListener {
    private CoordinatorLayout content;
    private RecyclerView rvToDoList;
    private ImageView ivLast, ivToday, ivNext;
    private TextView tvDate;
    private View viewMon, viewSun;
    private MonthPager monthPager;
    private boolean isLunar, isShowToday;
    private int showDay, theme;
    private CalendarDate currentDate;
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarAttr.WeekArrayType weekArrayType = CalendarAttr.WeekArrayType.Monday;
    private OnDateListener onDateListener;
    public RecyclerView.Adapter adapter;

    public CalendarMemoView(Context context) {
        this(context, null);
    }

    public CalendarMemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarMemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_calendar_memo_view, this);
        ivLast = findViewById(R.id.iv_last);
        ivLast.setOnClickListener(this);
        ivNext = findViewById(R.id.iv_next);
        ivNext.setOnClickListener(this);
        ivToday = findViewById(R.id.iv_today);
        ivToday.setOnClickListener(this);
        tvDate = findViewById(R.id.tv_date);
        monthPager = findViewById(R.id.month_pager);
        monthPager.setViewHeight(CalendarUtil.dpi2px(context, 270));
        viewMon = findViewById(R.id.week_monday);
        viewSun = findViewById(R.id.week_sunday);
        content = findViewById(R.id.content);
        rvToDoList = findViewById(R.id.list);
        rvToDoList.setHasFixedSize(true);
        rvToDoList.setLayoutManager(new LinearLayoutManager(getContext()));
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarMemoView);
        showDay = a.getInteger(R.styleable.CalendarMemoView_cmv_day, 0);
        theme = a.getInteger(R.styleable.CalendarMemoView_cmv_theme, 0);
        isLunar = a.getBoolean(R.styleable.CalendarMemoView_cmv_isLunar, true);
        isShowToday = a.getBoolean(R.styleable.CalendarMemoView_cmv_isShowToday, true);
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
        currentDate = date;
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
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition(0);
            }
        });
        initMonthPager();
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                initCurrentDate(date);
                if (onDateListener != null) {
                    onDateListener.onSelectDate(date);
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
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
                        date = CalendarUtil.getFirstOfWeek(date, weekArrayType);
                    }
                    initCurrentDate(date);
                    onDateListener.onPageDateChange(date);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
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
                onDateListener.onSelectDate(today);
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

    public void setOnDateListener(OnDateListener onDateListener) {
        this.onDateListener = onDateListener;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        rvToDoList.setAdapter(this.adapter);
    }

}
