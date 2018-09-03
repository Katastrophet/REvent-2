package de.ur.mi.revent;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Template.EventItem;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DownloadListener{
    private ArrayList<EventItem> table;
    private Context con;
    private String strAddress;
    private List<Address> address;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        //mMap.addMarker(new MarkerOptions().position(regensburg).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(regensburg));
        getDownloadData();
        for(int i = 0; i< table.size(); i++) {
            strAddress = table.get(i).getLocation();
            LatLng pos = getLocationFromAddress(strAddress);
            mMap.addMarker(new MarkerOptions().position(pos).title(table.get(i).getTitle()));
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
}
