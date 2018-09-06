package de.ur.mi.revent;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Navigation.NavigationController;
import de.ur.mi.revent.Navigation.NavigationListener;
import de.ur.mi.revent.Template.EventItem;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DownloadListener, NavigationListener {

    private ArrayList<EventItem> table;
    private Context con;
    private String strAddress;
    private List<Address> address;
    private GoogleMap mMap;
    private _NavigationMenu navigationMenu;
    private MarkerOptions ownLocationMarkerOptions;
    private Marker ownLocationMarker;
    NavigationController navigationController;
    LatLng lastKnownLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        navigationMenu=new _NavigationMenu(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        navigationController = new NavigationController(this);
        navigationController.setNavigationListener(this);
        navigationController.startNavigation();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Bewege Kamera nach Regensburg - TODO: Koordinate auslagern
        LatLng regensburg = new LatLng(49.01, 12.1);
        lastKnownLocation = navigationController.getLastKnownLocation();
        ownLocationMarkerOptions = new MarkerOptions().position(lastKnownLocation).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).zIndex(1.0f);
        ownLocationMarker = mMap.addMarker(ownLocationMarkerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(regensburg));
        getDownloadData();
        for(int i = 0; i< table.size(); i++) {
            float[] results = new float[1];
            strAddress = table.get(i).getLocation();
            LatLng pos = getLocationFromAddress(strAddress);
            mMap.addMarker(new MarkerOptions().position(pos).title(table.get(i).getTitle()));
            results = navigationController.getEstimatedDistanceForLocation(pos);
            System.out.println(table.get(i).getTitle());
            System.out.println(i);
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {
        con = this;
        Geocoder coder = new Geocoder(con);

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            LatLng pos = new LatLng(lat, lng);
            return pos;
        } catch (Exception e) {
            return null;
        }
    }

    public void showLocation(){
        lastKnownLocation = navigationController.getLastKnownLocation();
        //ownLocationMarkerOptions = new MarkerOptions().position(lastKnownLocation).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).zIndex(1.0f);
        ownLocationMarker.setPosition(lastKnownLocation);
    }


    /***Downloadhandling***/

    private void getDownloadData(){
        try {
            if (DownloadManager.getStatus() == AsyncTask.Status.FINISHED) {
                table = DownloadManager.getResults();
                printData();
            } else {
                DownloadManager.setListener(this);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onDownloadFinished() {
        try {
            table = DownloadManager.getResults();
            printData();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void printData(){
        System.out.println(table.get(2).getOrganizer());
    }


/*Navigationhandling*/

    @Override
    public void onSignalFound() {
        Toast.makeText(this, "Signal acquired.", Toast.LENGTH_SHORT).show();
        showLocation();

    }

    @Override
    public void onSignalLost() {
        Toast.makeText(this, "Signal lost!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged() {
        Toast.makeText(this, "location changed.", Toast.LENGTH_SHORT).show();
        showLocation();
        System.out.println("Location changed");
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
}
