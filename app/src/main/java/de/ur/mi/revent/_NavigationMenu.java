package de.ur.mi.revent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import java.time.LocalDate;

import de.ur.mi.revent.Template.EventItem;

public class _NavigationMenu {
    private Activity activity;
    public _NavigationMenu(Activity activity){
        this.activity=activity;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.menu_KommendeEvents: showKommendeEvents();break;
            case R.id.menu_VorgemerkteEvents: showVorgemerkteEvents();break;
            case R.id.menu_REvent: showMainActivity();break;
            case R.id.menu_Maps: showMap();break;
            case R.id.menu_Settings: showSettings();break;
            case R.id.menu_VorgeschlageneEvents: showVorgeschlageneEvents();break;
        }
        return true;
    }

    public void showMap() {
        Intent i = new Intent(activity,  MapsActivity.class);
        activity.startActivity(i);
    }
    public void showEvent(String eventTitle, String eventDate, String eventTime, String eventLocation, String eventOrganizer, String eventType,String eventNotes,Integer eventID){
        Intent i = new Intent(activity,  Event.class);
        i.putExtra("event_title",eventTitle);
        i.putExtra("event_date",eventDate);
        i.putExtra("event_time",eventTime);
        i.putExtra("event_location",eventLocation);
        i.putExtra("event_organizer",eventOrganizer);
        i.putExtra("event_type",eventType);
        i.putExtra("event_notes",eventNotes);
        i.putExtra("event_ID",eventID);
        activity.startActivity(i);
    }
    public void showVorgemerkteEvents(){
        Intent i = new Intent(activity,  VorgemerkteEvents.class);
        activity.startActivity(i);
    }
    public void showKommendeEvents(){
        Intent i = new Intent(activity,  KommendeEvents.class);
        activity.startActivity(i);
    }
    public void showMainActivity(){
        Intent i = new Intent(activity,  MainActivity.class);
        activity.startActivity(i);
    }
    public void showSettings(){
        Intent i = new Intent(activity,  Settings.class);
        activity.startActivity(i);
    }
    public void showVorgeschlageneEvents(){
        Intent i = new Intent(activity,  VorgeschlageneEventsActivity.class);
        activity.startActivity(i);
    }
}
