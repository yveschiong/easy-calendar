package com.yveschiong.easycalendar.models;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Calendar;

public class CalendarRange {
    private Pair<Calendar, Calendar> range;

    public CalendarRange(@NonNull Calendar start, @NonNull Calendar end) {
        setRange(start, end);
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
}
