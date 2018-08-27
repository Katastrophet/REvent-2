package de.ur.mi.revent.Template;

public class EventItem {

    private String title;
    private String type;
    private String organizer;
    //TODO: Datum in json hinzufügen und abholen.


    public EventItem(String title, String type, String organizer) {
        this.title = title;
        this.type = type;
        this.organizer = organizer;
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

}

