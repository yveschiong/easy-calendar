package com.yveschiong.easycalendar.models;

import java.util.Calendar;

public class Event {
    private String eventName;
    private String eventDescription;
    private Calendar startCalendar;
    private Calendar endCalendar;

    public Event(String eventName, String eventDescription, Calendar startCalendar, Calendar endCalendar) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startCalendar = startCalendar;
        this.endCalendar = endCalendar;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    public void setStartCalendar(Calendar startCalendar) {
        this.startCalendar = startCalendar;
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }

    public void setEndCalendar(Calendar endCalendar) {
        this.endCalendar = endCalendar;
    }
}
