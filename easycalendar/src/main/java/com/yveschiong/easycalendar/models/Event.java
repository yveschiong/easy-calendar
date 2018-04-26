package com.yveschiong.easycalendar.models;

import com.yveschiong.easycalendar.utils.ObjectUtils;

public class Event {
    private String name;
    private String description;
    private CalendarRange calendarRange;

    public Event(String name, String description, CalendarRange calendarRange) {
        this.name = name;
        this.description = description;
        this.calendarRange = calendarRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CalendarRange getCalendarRange() {
        return calendarRange;
    }

    public void setCalendarRange(CalendarRange calendarRange) {
        this.calendarRange = calendarRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;
        return ObjectUtils.equals(name, event.name) &&
                ObjectUtils.equals(description, event.description) &&
                ObjectUtils.equals(calendarRange, event.calendarRange);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(name, description, calendarRange);
    }
}
