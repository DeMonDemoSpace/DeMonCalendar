package com.demon.calendar.model;

import com.demon.calendar.util.CalendarUtil;

import java.io.Serializable;
import java.util.Calendar;

public class CalendarDate implements Serializable {
    private static final long serialVersionUID = 1L;
    public int year;
    public int month;  //1~12
    public int day;
    private int week;
    //显示
    public String chinaMonth;
    public String chinaDay;

    public CalendarDate(int year, int month, int day) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = CalendarUtil.getDayOfWeek(year, month, day);
        String[] chinaDate = ChinaDate.getChinaDate(year, month, day);
        this.chinaMonth = chinaDate[0];
        this.chinaDay = chinaDate[1];
    }

    public CalendarDate() {
        this.year = CalendarUtil.getYear();
        this.month = CalendarUtil.getMonth();
        this.day = CalendarUtil.getDay();
        this.week = CalendarUtil.getDayOfWeek(year, month, day);
        String[] chinaDate = ChinaDate.getChinaDate(year, month, day);
        this.chinaMonth = chinaDate[0];
        this.chinaDay = chinaDate[1];
    }

    public String getDisplayWeek() {
        String s = "";
        switch (week) {
            case 1:
                s = "星期日";
                break;
            case 2:
                s = "星期一";
                break;
            case 3:
                s = "星期二";
                break;
            case 4:
                s = "星期三";
                break;
            case 5:
                s = "星期四";
                break;
            case 6:
                s = "星期五";
                break;
            case 7:
                s = "星期六";
                break;

        }
        return s;
    }

    public boolean isWeekday() {
        if (week == 1 || week == 7) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过修改当前Date对象的天数返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyDay(int day) {
        int lastMonthDays = CalendarUtil.getMonthDays(this.year, this.month - 1);
        int currentMonthDays = CalendarUtil.getMonthDays(this.year, this.month);

        CalendarDate modifyDate;
        if (day > currentMonthDays) {
            modifyDate = new CalendarDate(this.year, this.month, this.day);
            //Log.e("ldf", "移动天数过大");
        } else if (day > 0) {
            modifyDate = new CalendarDate(this.year, this.month, day);
        } else if (day > 0 - lastMonthDays) {
            modifyDate = new CalendarDate(this.year, this.month - 1, lastMonthDays + day);
        } else {
            modifyDate = new CalendarDate(this.year, this.month, this.day);
            //Log.e("ldf", "移动天数过大");
        }
        return modifyDate;
    }

    /**
     * 通过修改当前Date对象的所在周返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyWeek(int offset) {
        CalendarDate result = new CalendarDate();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DATE, offset * 7);
        result.setYear(c.get(Calendar.YEAR));
        result.setMonth(c.get(Calendar.MONTH) + 1);
        result.setDay(c.get(Calendar.DATE));
        return result;
    }

    /**
     * 通过修改当前Date对象的所在月返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyMonth(int offset) {
        CalendarDate result = new CalendarDate();
        int addToMonth = this.month + offset;
        if (offset > 0) {
            if (addToMonth > 12) {
                result.setYear(this.year + (addToMonth - 1) / 12);
                result.setMonth(addToMonth % 12 == 0 ? 12 : addToMonth % 12);
            } else {
                result.setYear(this.year);
                result.setMonth(addToMonth);
            }
        } else {
            if (addToMonth == 0) {
                result.setYear(this.year - 1);
                result.setMonth(12);
            } else if (addToMonth < 0) {
                result.setYear(this.year + addToMonth / 12 - 1);
                int month = 12 - Math.abs(addToMonth) % 12;
                result.setMonth(month == 0 ? 12 : month);
            } else {
                result.setYear(this.year);
                result.setMonth(addToMonth == 0 ? 12 : addToMonth);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String smonth = month + "";
        String sday = day + "";
        if (month < 10) {
            smonth = "0" + month;
        }

        if (day < 10) {
            sday = "0" + day;
        }
        return year + "-" + smonth + "-" + sday;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getChinaMonth() {
        return chinaMonth;
    }

    public void setChinaMonth(String chinaMonth) {
        this.chinaMonth = chinaMonth;
    }

    public String getChinaDay() {
        return chinaDay;
    }

    public void setChinaDay(String chinaDay) {
        this.chinaDay = chinaDay;
    }

    public boolean equals(CalendarDate date) {
        if (date == null) {
            return false;
        }
        if (this.getYear() == date.getYear()
                && this.getMonth() == date.getMonth()
                && this.getDay() == date.getDay()) {
            return true;
        }
        return false;
    }

    public CalendarDate cloneSelf() {
        return new CalendarDate(year, month, day);
    }

}