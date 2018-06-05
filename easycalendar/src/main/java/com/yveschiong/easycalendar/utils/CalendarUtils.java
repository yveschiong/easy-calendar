package com.yveschiong.easycalendar.utils;

import android.support.annotation.NonNull;

import com.yveschiong.easycalendar.models.CalendarRange;

import java.util.Calendar;

public class CalendarUtils {
    public static Calendar createCalendar() {
        return setEarliestCalendarDay(Calendar.getInstance());
    }

    // Sets the given calendar to the earliest time for the day
    public static Calendar setEarliestCalendarDay(@NonNull Calendar calendar) {
        if (calendar == null) {
            throw new NullPointerException("Calendar shouldn't be null");
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    // Sets the given calendar to the latest time for the day
    public static Calendar setLatestCalendarDay(@NonNull Calendar calendar) {
        if (calendar == null) {
            throw new NullPointerException("Calendar shouldn't be null");
        }

        setEarliestCalendarDay(calendar);
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar;
    }

    public static CalendarRange createCalendarRange() {
        return createCalendarRange(Calendar.getInstance());
    }

    // Create a calendar range for the earliest and latest calendar of the day
    public static CalendarRange createCalendarRange(Calendar day) {
        Calendar startDay = (Calendar) day.clone();
        Calendar endDay = (Calendar) day.clone();
        return new CalendarRange(setEarliestCalendarDay(startDay), setLatestCalendarDay(endDay));
    }

    public static CalendarRange createCalendarMonthRange() {
        return createCalendarMonthRange(Calendar.getInstance());
    }

    // Create a calendar range for the earliest and latest calendar of the month given a day in the month
    public static CalendarRange createCalendarMonthRange(Calendar day) {
        Calendar startDay = (Calendar) day.clone();
        Calendar endDay = (Calendar) day.clone();

        startDay.set(Calendar.DATE, startDay.getActualMinimum(Calendar.DATE));
        endDay.set(Calendar.DATE, endDay.getActualMaximum(Calendar.DATE));

        return new CalendarRange(setEarliestCalendarDay(startDay), setLatestCalendarDay(endDay));
    }
}
