package de.ur.mi.revent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import de.ur.mi.revent.Template.EventItem;

public class Event extends Activity{
    private _NavigationMenu navigationMenu;
    private TextView date;
    private TextView time;
    private TextView location;
    private TextView fachschaft;
    private TextView type;
    private TextView notes;
    private Switch switch_teilnehmen;
    //!
    private LocalDatabase markedEventsDatabase;
    //!
    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventOrganizer;
    private String eventType;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        navigationMenu=new _NavigationMenu(this);
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        //!
        markedEventsDatabase = MainActivity.getMarkedEventsDatabase();

        //!
         eventTitle=extras.getString("event_title");
         eventDate=extras.getString("event_date");
         eventTime=extras.getString("event_time");
         eventLocation=extras.getString("event_location");
         eventOrganizer=extras.getString("event_organizer");
         eventType=extras.getString("event_type");
        setTitle(eventTitle);


        date=(TextView) findViewById(R.id.date);
        time=(TextView) findViewById(R.id.time);
        location=(TextView) findViewById(R.id.location);
        fachschaft=(TextView) findViewById(R.id.fachschaft);
        type=(TextView) findViewById(R.id.eventType);
        notes=(TextView) findViewById(R.id.notes);
        switch_teilnehmen=(Switch)findViewById(R.id.switch_teilnehmen);

        date.setText(getString(R.string.date)+eventDate);
        time.setText(getString(R.string.time)+eventTime);
        location.setText(getString(R.string.location)+eventLocation);
        fachschaft.setText(getString(R.string.fachschaft)+eventOrganizer);
        type.setText(getString(R.string.eventType)+eventType);
        notes.setText(getString(R.string.notes));
        switch_teilnehmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ChangeColor
                if (getCheckedState()){
                    switch_teilnehmen.getThumbDrawable().setTint(getResources().getColor(R.color._Green));
                    switch_teilnehmen.getTrackDrawable().setTint(getResources().getColor(R.color._Green));
                    markedEventsDatabase.insertEventItem(new EventItem(eventTitle,eventType,eventOrganizer, LocalDate.parse(eventDate), LocalTime.parse(eventTime),eventLocation));

                    ArrayList stuff=markedEventsDatabase.getAllEventItems();
                    System.out.println(stuff.size());
                }
                else{
                    switch_teilnehmen.getThumbDrawable().setTint(getResources().getColor(R.color._Grey));
                    switch_teilnehmen.getTrackDrawable().setTint(getResources().getColor(R.color._Grey));
                    markedEventsDatabase.removeEventItem(new EventItem(eventTitle,eventType,eventOrganizer, LocalDate.parse(eventDate), LocalTime.parse(eventTime),eventLocation));


                    ArrayList stuff=markedEventsDatabase.getAllEventItems();
                    System.out.println(stuff.size());
                }
            }
        });
        checkIfEventChecked();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        navigationMenu.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        navigationMenu.onOptionsItemSelected(item);
        return true;
    }
    public boolean getCheckedState(){
        return switch_teilnehmen.isChecked();
    }
    private boolean checkIfEventChecked(){
        boolean switchState=false;
        for (int i=0;i<markedEventsDatabase.getAllEventItems().size();i++){
            if (markedEventsDatabase.getAllEventItems().get(i).getTitle().equals(eventTitle)) {
                switchState = true;
                switch_teilnehmen.getThumbDrawable().setTint(getResources().getColor(R.color._Green));
                switch_teilnehmen.getTrackDrawable().setTint(getResources().getColor(R.color._Green));
                switch_teilnehmen.setChecked(true);
            }
        }
        System.out.println(switchState);
        return switchState;
    }


}
