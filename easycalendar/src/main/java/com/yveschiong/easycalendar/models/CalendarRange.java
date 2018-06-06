package com.yveschiong.easycalendar.models;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.yveschiong.easycalendar.utils.ObjectUtils;

import java.util.Calendar;

public class CalendarRange {
    private Pair<Calendar, Calendar> range;

    public CalendarRange(@NonNull Calendar start, @NonNull Calendar end) {
        setRange(start, end);
    }

    public CalendarRange(CalendarRange range) {
        this((Calendar) range.getStart().clone(), (Calendar) range.getEnd().clone());
    }

    public Pair<Calendar, Calendar> getRange() {
        return range;
    }

    public Calendar getStart() {
        return range.first;
    }

    public Calendar getEnd() {
        return range.second;
    }

    public void setRange(@NonNull Calendar start, @NonNull Calendar end) {
        if (start == null || end == null) {
            throw new NullPointerException("The start or end calendar is null");
        }

        if (end.before(start)) {
            // Must fulfill start <= end in terms of calendar dates
            throw new IllegalArgumentException("The start calendar has to be before the end calendar");
        }

        range = new Pair<>(start, end);
    }

    public boolean intersects(@NonNull CalendarRange calendarRange) {
        if (calendarRange == null) {
            return false;
        }

        Calendar startCalendarA = this.range.first;
        Calendar endCalendarA = this.range.second;
        Calendar startCalendarB = calendarRange.getStart();
        Calendar endCalendarB = calendarRange.getEnd();
        return !startCalendarA.after(endCalendarB) && !endCalendarA.before(startCalendarB);
    }

    public boolean intersects(@NonNull Calendar day) {
        if (day == null) {
            return false;
        }

        Calendar startCalendar = this.range.first;
        Calendar endCalendar = this.range.second;
        return !startCalendar.after(day) && !endCalendar.before(day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CalendarRange that = (CalendarRange) o;
        return ObjectUtils.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(range);
    }
}
