package de.ur.mi.revent.Template;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventItem {

    private String title;
    private String type;
    private String organizer;
    private LocalDate date;
    private LocalTime time;
    private String location;

    public EventItem(String title, String type, String organizer, LocalDate date, LocalTime time, String location) {
        this.title = title;
        this.type = type;
        this.organizer = organizer;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getOrganizer() {
        return organizer;
    }

    public LocalDate getDate() { return date; }

    public LocalTime getTime() { return time; }

    public String getLocation() { return location; }
}

