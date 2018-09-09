package de.ur.mi.revent;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.ArrayList;

import de.ur.mi.revent.AppConfig.AppConfig;
import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Navigation.NavigationController;
import de.ur.mi.revent.Navigation.NavigationListener;
import de.ur.mi.revent.Template.EventItem;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DownloadListener, NavigationListener, InternetConnectivityListener, GoogleMap.OnInfoWindowClickListener {
    private ArrayList<EventItem> table;
    private GoogleMap mMap;
    private String strAddress;
    private EventItem associatedEvent;
    private ArrayList<MarkerOptions> eventMarkerOptions;
    private ArrayList<Integer> idList;
    private MarkerOptions ownLocationMarkerOptions;
    private Marker ownLocationMarker;
    private NavigationController navigationController;
    private LatLng lastKnownLocation;
    private LatLng regensburg;
    private boolean failed;

    private InternetAvailabilityChecker mInternetAvailabilityChecker;

    private _NavigationMenu navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        navigationMenu=new _NavigationMenu(this);
        failed = false;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        navigationController = new NavigationController(this);
        navigationController.setNavigationListener(this);
        navigationController.startNavigation(getApplicationContext());

        getDownloadData();
        getEventMarkers();

        InternetAvailabilityChecker.init(this);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
    }

    //Aufgerufen sobald die Karte bereit ist.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        regensburg = new LatLng(49.01, 12.1);
        setOwnLocationMarker();
        //Bewege Kamera nach Regensburg - TODO: Koordinate auslagern
        drawEventMarkers();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(regensburg));
        mMap.setOnInfoWindowClickListener(this);
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

    private void getEventMarkers(){
        //Stellt die Daten für die Eventmarker zusammen.
        eventMarkerOptions = new ArrayList<>();
        idList = new ArrayList<>();
        if(table!=null || !table.isEmpty()) {
            //Positionen und Distanz der Events werden anhand ihrer Adresse ermittelt und eingetragen.
            for (int i = 0; i < table.size(); i++) {
                strAddress = table.get(i).getLocation();
                LatLng pos = navigationController.getLocationFromAddress(strAddress, this);
                if(pos!=null) {
                    float[] distance = navigationController.getEstimatedDistanceForLocation(pos);
                    String distanceString = String.valueOf(Math.round(distance[0]));
                    MarkerOptions eventMarkerOptions = new MarkerOptions()
                            .position(pos)
                            .title(table.get(i).getTitle())
                            .snippet(distanceString + getString(R.string.meter));
                    this.eventMarkerOptions.add(eventMarkerOptions);
                    this.idList.add(table.get(i).getId());
                } else {
                    continue;
                }

            }
        }
    }

    private void drawEventMarkers() {
        //Zeichnet die Eventmarker anhand der in eventMarkerOptions gesammelten Optionen.
        if (!eventMarkerOptions.isEmpty() && eventMarkerOptions != null) {
            for (int i = 0; i < eventMarkerOptions.size(); i++) {
                if (eventMarkerOptions.get(i) != null) {
                    Marker eventMarker = mMap.addMarker(eventMarkerOptions.get(i));
                    eventMarker.setTag(idList.get(i));
                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTag().toString().equals(AppConfig.OWN_POSITION)) {
            return;
        } else {
            int tag = (int) marker.getTag();
            associatedEvent = filterId(table, tag);
            if (associatedEvent != null) {
                navigationMenu.showEvent(associatedEvent.getTitle(), associatedEvent.getDate().toString(), associatedEvent.getTime().toString(), associatedEvent.getLocation(), associatedEvent.getOrganizer(), associatedEvent.getType(),associatedEvent.getNotes(),associatedEvent.getId());
            }
        }
    }

    public EventItem filterId(ArrayList<EventItem> table, int id) {
        associatedEvent = null;
        EventItem result = null;
        for(int k = 0; k<table.size(); k++) {
            if(id == (int) table.get(k).getId()){
                result = table.get(k);
            }
        }
        return result;
    }

    private void getDownloadData(){
        try{
            switch (DownloadManager.getStatus()){
                case FINISHED:
                    table = DownloadManager.getResults();
                    if(table.isEmpty() && failed){
                        DownloadManager.startDownload();
                        DownloadManager.setListener(this);
                    }
                    break;
                case PENDING:
                    DownloadManager.startDownload();
                    DownloadManager.setListener(this);
                    break;
                case RUNNING:
                    DownloadManager.setListener(this);
                    break;
            }
        }
        catch (Exception e){
        e.printStackTrace();
        }
    }

    public void onDownloadFinished() {
        try {
            table = DownloadManager.getResults();
            getEventMarkers();
            drawEventMarkers();
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.error_occured, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSignalFound() {
        showLocation();
    }

    @Override
    public void onSignalLost() {
        //Toast.makeText(this, "Signal lost!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged() {
        showLocation();
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

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
       try {
           if (isConnected && table.isEmpty()) {
               //(Wieder) Internetverbindung vorhanden, aber table wurde noch nicht befüllt.
               Toast.makeText(this, R.string.connection_success, Toast.LENGTH_SHORT).show();
               failed = true;
               //der Boolean 'failed' deutet daraufhin dass fehlerhaft beendet wurde (da keine Internetverbindung),
               // sofern man anschließend in getDownloadData case 'FINISHED' landet.
               getDownloadData();
               drawEventMarkers();
           } else if (!(isConnected)) {
               //Keine Internetverbindung
               Toast.makeText(this, R.string.connection_failure, Toast.LENGTH_SHORT).show();
           }
       } catch (Exception e){
           e.printStackTrace();
       }
    }
}
