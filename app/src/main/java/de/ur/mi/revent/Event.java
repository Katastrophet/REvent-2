package de.ur.mi.revent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class Event extends Activity{
    private _NavigationMenu navigationMenu;
    private TextView date;
    private TextView time;
    private TextView location;
    private TextView fachschaft;
    private TextView type;
    private TextView notes;
    private Switch switch_teilnehmen;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        navigationMenu=new _NavigationMenu(this);
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        String eventTitle=extras.getString("event_title");
        String eventDate=extras.getString("event_date");
        String eventTime=extras.getString("event_time");
        String eventLocation=extras.getString("event_location");
        String eventOrganizer=extras.getString("event_organizer");
        String eventType=extras.getString("event_type");
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
                }
                else{
                    switch_teilnehmen.getThumbDrawable().setTint(getResources().getColor(R.color._Grey));
                    switch_teilnehmen.getTrackDrawable().setTint(getResources().getColor(R.color._Grey));
                }
                //Add/DeleteLocalLibrary
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public boolean getCheckedState(){
        return switch_teilnehmen.isChecked();
    }
}
