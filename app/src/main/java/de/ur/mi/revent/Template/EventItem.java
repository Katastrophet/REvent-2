package de.ur.mi.revent.Template;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventItem {

    private String title;
    private String type;
    private String organizer;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private int distance;
    private String notes;

    public EventItem(String title, String type, String organizer, LocalDate date, LocalTime time, String location, String notes) {
        this.title = title;
        this.type = type;
        this.organizer = organizer;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notes = notes;

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

    public int getDistance() {return distance;}

    public String getNotes() { return notes; }

    public int setDistance(int distance) {
        this.distance = distance;
        return 0;
    }
}

