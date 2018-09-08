package de.ur.mi.revent;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
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
import java.util.List;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Navigation.NavigationController;
import de.ur.mi.revent.Navigation.NavigationListener;
import de.ur.mi.revent.Template.EventItem;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DownloadListener, NavigationListener, InternetConnectivityListener {
    private ArrayList<EventItem> table;
    private Context con;
    private GoogleMap mMap;
    private String strAddress;
    private ArrayList<MarkerOptions> eventMarkerOptions;
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

        //Finde und zeige die zuletzt gefundene Position des Nutzers
        lastKnownLocation = navigationController.getLastKnownLocation();
        ownLocationMarkerOptions = new MarkerOptions()
                .position(lastKnownLocation)
                .title("Deine Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .zIndex(1.0f);
        ownLocationMarker = mMap.addMarker(ownLocationMarkerOptions);

        //Bewege Kamera nach Regensburg - TODO: Koordinate auslagern
        mMap.moveCamera(CameraUpdateFactory.newLatLng(regensburg));

        drawEventMarkers();
    }

    public void showLocation(){
        lastKnownLocation = navigationController.getLastKnownLocation();
        ownLocationMarker.setPosition(lastKnownLocation);
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
            Toast.makeText(this, "Leider ist ein Fehler aufgetreten.", Toast.LENGTH_SHORT).show();
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
               Toast.makeText(this, "aktive Internetverbindung gefunden!", Toast.LENGTH_SHORT).show();
               failed = true;
               //der Boolean 'failed' deutet daraufhin dass fehlerhaft beendet wurde (da keine Internetverbindung),
               // sofern man anschließend in getDownloadData case 'FINISHED' landet.
               getDownloadData();
               drawEventMarkers();
           } else if (!(isConnected)) {
               //Keine Internetverbindung
               Toast.makeText(this, "REvent benötigt eine aktive Internetverbindung um richtig zu funktionieren!", Toast.LENGTH_SHORT).show();
           }
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    private void getEventMarkers(){
        //Stellt die Daten für die Eventmarker zusammen.
        eventMarkerOptions = new ArrayList<>();
        if(!table.isEmpty()) {
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
                            .snippet(distanceString + " m");
                    this.eventMarkerOptions.add(eventMarkerOptions);
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
                    mMap.addMarker(eventMarkerOptions.get(i));
                }
            }
        }
    }
}
