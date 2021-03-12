package com.yveschiong.easycalendar.utils;

import androidx.annotation.NonNull;

import com.yveschiong.easycalendar.models.CalendarRange;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarUtils {

    public static final int DAYS_IN_A_WEEK = 7;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

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

    public static String getMonthName(@NonNull Calendar day) {
        if (day == null) {
            return "";
        }

        return day.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    public static String getWeekdayName(@NonNull Calendar day) {
        if (day == null) {
            return "";
        }

        return day.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public static String getWeekdayShortName(@NonNull Calendar day) {
        if (day == null) {
            return "";
        }

        return getWeekdayName(day).substring(0, 1);
    }

    public static String[] getWeekdayShortNames() {
        String[] names = new String[DAYS_IN_A_WEEK];

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            // The days of the week start at 1
            calendar.set(Calendar.DAY_OF_WEEK, i + 1);
            names[i] = getWeekdayShortName(calendar);
        }

        return names;
    }

    public static String getDayString(Calendar day) {
        if (day == null) {
            return "";
        }

        return String.valueOf(day.get(Calendar.DATE));
    }

    public static Calendar getEarliestStartOfWeek(Calendar day) {
        Calendar startOfWeekDay = (Calendar) day.clone();
        startOfWeekDay.add(Calendar.DAY_OF_WEEK, -(startOfWeekDay.get(Calendar.DAY_OF_WEEK) - 1));
        return startOfWeekDay;
    }

    public static boolean isSameDay(Calendar firstDay, Calendar secondDay) {
        if (firstDay == null || secondDay == null) {
            return false;
        }

        return firstDay.get(Calendar.YEAR) == secondDay.get(Calendar.YEAR)
                && firstDay.get(Calendar.DAY_OF_YEAR) == secondDay.get(Calendar.DAY_OF_YEAR);
    }

    public static String toSimpleString(@NonNull Calendar day) {
        if (day == null) {
            return "";
        }

        return SIMPLE_DATE_FORMAT.format(day.getTime());
    }
}
