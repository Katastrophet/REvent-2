package de.ur.mi.revent.Template;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EventItem {

    private String title;
    private String type;
    private String organizer;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private int distance;
    private String notes;
    private int id;

    public EventItem(String title, String type, String organizer, LocalDate date, LocalTime time, String location, String notes, int id) {
        this.title = title;
        this.type = type;
        this.organizer = organizer;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notes = notes;
        this.id = id;

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

    //public int getDistance() {return distance;}

    public String getNotes() { return notes; }

    public int getId() { return id;}
}

