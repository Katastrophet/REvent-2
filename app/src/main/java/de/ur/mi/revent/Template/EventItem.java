package de.ur.mi.revent.Template;

import java.time.LocalDate;

public class EventItem {

    private String title;
    private String type;
    private String organizer;
    private LocalDate date;
    //TODO: Datum in json hinzufügen und abholen.


    public EventItem(String title, String type, String organizer, LocalDate date) {
        this.title = title;
        this.type = type;
        this.organizer = organizer;
        this.date = date;
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
}

