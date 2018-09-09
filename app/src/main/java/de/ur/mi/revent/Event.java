package de.ur.mi.revent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.ur.mi.revent.AppConfig.AppConfig;
import de.ur.mi.revent.LocalDatabase.LocalDatabase;
import de.ur.mi.revent.Menu._NavigationMenu;
import de.ur.mi.revent.Navigation.NavigationController;


import java.time.LocalDate;
import java.time.LocalTime;

import de.ur.mi.revent.Navigation.NavigationListener;
import de.ur.mi.revent.Template.EventItem;

public class Event extends FragmentActivity implements OnMapReadyCallback, NavigationListener{
    private _NavigationMenu navigationMenu;

    private TextView date;
    private TextView time;
    private TextView location;
    private TextView fachschaft;
    private TextView type;
    private TextView notes;
    private Switch switch_teilnehmen;

    private LocalDatabase markedEventsDatabase;

    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventOrganizer;
    private String eventType;
    private String eventNotes;
    private Integer eventID;
    //Map
    private MarkerOptions ownLocationMarkerOptions;
    private Marker ownLocationMarker;
    private NavigationController navigationController;
    private LatLng lastKnownLocation;
    private LatLng regensburg;
    private GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent i=getIntent();
        Bundle extras=i.getExtras();

        navigationMenu=new _NavigationMenu(this);
        getMapFragment();
        initNavigation();

        markedEventsDatabase = MainActivity.getMarkedEventsDatabase();

        getExtras(extras);
        setTextView();
        setSwitchButton();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        regensburg = AppConfig.REGENSBURG;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(regensburg));
        setOwnLocationMarker();
        showLocation();
        drawEventMarker(eventLocation);
    }

    public void getMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_map);
        mapFragment.getMapAsync(this);
    }

    public void setOwnLocationMarker(){
        //Finde und zeige die zuletzt gefundene Position des Nutzers
        lastKnownLocation = navigationController.getLastKnownLocation();
        ownLocationMarkerOptions = new MarkerOptions()
                .position(lastKnownLocation)
                .title(getString(R.string.own_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .zIndex(1.0f);
        ownLocationMarker = mMap.addMarker(ownLocationMarkerOptions);
        ownLocationMarker.setTag(AppConfig.OWN_POSITION);
    }

    public void showLocation(){
        lastKnownLocation = navigationController.getLastKnownLocation();
        ownLocationMarker.setPosition(lastKnownLocation);
    }

    public void drawEventMarker(String address){
        LatLng pos = navigationController.getLocationFromAddress(address, this);
        if(pos!=null) {
            float[] distance = navigationController.getEstimatedDistanceForLocation(pos);
            String distanceString = String.valueOf(Math.round(distance[0]));
            MarkerOptions eventMarkerOptions = new MarkerOptions()
                    .position(pos)
                    .title(eventTitle)
                    .snippet(distanceString + getString(R.string.meter));
            mMap.addMarker(eventMarkerOptions);
        }
    }

    public void initNavigation(){
        navigationController = new NavigationController(this);
        navigationController.setNavigationListener(this);
        navigationController.startNavigation(getApplicationContext());
    }

    public void setTextView(){
        date=(TextView) findViewById(R.id.date);
        time=(TextView) findViewById(R.id.time);
        location=(TextView) findViewById(R.id.location);
        fachschaft=(TextView) findViewById(R.id.fachschaft);
        type=(TextView) findViewById(R.id.eventType);
        notes=(TextView) findViewById(R.id.notes);

        date.setText(getString(R.string.date)+" "+eventDate);
        time.setText(getString(R.string.time)+" "+eventTime);
        location.setText(getString(R.string.location)+" "+eventLocation);
        fachschaft.setText(getString(R.string.fachschaft)+" "+eventOrganizer);
        type.setText(getString(R.string.eventType)+" "+eventType);
        notes.setText(getString(R.string.notes)+" "+eventNotes);
    }

    public void getExtras(Bundle extras){
        eventTitle=extras.getString("event_title");
        eventDate=extras.getString("event_date");
        eventTime=extras.getString("event_time");
        eventLocation=extras.getString("event_location");
        eventOrganizer=extras.getString("event_organizer");
        eventType=extras.getString("event_type");
        eventNotes=extras.getString("event_notes");
        eventID=extras.getInt("event_ID");
        setTitle(eventTitle);
    }

    public void setSwitchButton(){
        switch_teilnehmen=(Switch)findViewById(R.id.switch_teilnehmen);
        switch_teilnehmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ChangeColor
                if (getCheckedState()){
                    switch_teilnehmen.getThumbDrawable().setTint(getResources().getColor(R.color._Green));
                    switch_teilnehmen.getTrackDrawable().setTint(getResources().getColor(R.color._Green));
                    markedEventsDatabase.insertEventItem(new EventItem(eventTitle,eventType,eventOrganizer, LocalDate.parse(eventDate), LocalTime.parse(eventTime),eventLocation,eventNotes,eventID));
                }
                else{
                    switch_teilnehmen.getThumbDrawable().setTint(getResources().getColor(R.color._Grey));
                    switch_teilnehmen.getTrackDrawable().setTint(getResources().getColor(R.color._Grey));
                    markedEventsDatabase.removeEventItem(new EventItem(eventTitle,eventType,eventOrganizer, LocalDate.parse(eventDate), LocalTime.parse(eventTime),eventLocation,eventNotes,eventID));
                }
            }
        });
        checkIfEventChecked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        navigationMenu.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        navigationMenu.onOptionsItemSelected(item);
        return true;
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
        return switchState;
    }

    public boolean getCheckedState(){
        return switch_teilnehmen.isChecked();
    }

    @Override
    public void onSignalFound(){
        showLocation();
    }

    @Override
    public void onSignalLost(){
        //nicht nötig
    }

    @Override
    public void onLocationChanged(){
        showLocation();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
