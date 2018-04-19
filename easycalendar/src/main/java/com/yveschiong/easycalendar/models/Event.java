package com.yveschiong.easycalendar.models;

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
}
