package de.ur.mi.revent.Navigation;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
//import com.google.android.gms.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import de.ur.mi.revent.Navigation.NavigationListener;

public class NavigationController implements LocationListener {

    private Context context;

    private NavigationListener navigationListener;
    private LocationManager locationManger;
    private Location lastKnownLocation;
    private String bestProvider;

    public NavigationController(Context context) {
        this.context = context.getApplicationContext();
        locationManger = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        setBestProvider();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastKnownLocation = locationManger.getLastKnownLocation(bestProvider);
        }
    }

    private void setBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        bestProvider = locationManger.getBestProvider(criteria, true);
        if (bestProvider == null) {
            Log.e("setbestprovider", "no Provider set");
        }
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
        //Set Listener, make for Loop with setTarget + getDistance
    }

    public void startNavigation() {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManger.requestLocationUpdates(bestProvider, 1000, 0, this);
                locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            }
    }

    public void stopNavigation() {
        locationManger.removeUpdates(this);
    }

    public float[] getEstimatedDistanceForLocation(LatLng location) {
        float[] results = new float[1];
        if (lastKnownLocation == null) {
            lastKnownLocation = new Location(bestProvider);
        }
        try
        {
            double startLat = lastKnownLocation.getLatitude();
            double startLng = lastKnownLocation.getLongitude();
            double targetLat = location.latitude;
            double targetLng = location.longitude;
            Location.distanceBetween(startLat, startLng, targetLat, targetLng, results);
            return results;
        }
        catch (NullPointerException nullPointer)
        {
            nullPointer.printStackTrace();
            return null;
        }
    }

    public LatLng getLastKnownLocation(){
        double latitude = lastKnownLocation.getLatitude();
        double longitude = lastKnownLocation.getLongitude();
        LatLng lastKnownLocationInLatLng = new LatLng(latitude, longitude);
        return lastKnownLocationInLatLng;
    }

    public LatLng getLocationFromAddress(String strAddress, Context con) {
        Geocoder coder = new Geocoder(con);
        List<Address> address;
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
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
        if (navigationListener == null) {
            return;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (navigationListener == null) {
            return;
        }
        if (status == LocationProvider.AVAILABLE) {
            navigationListener.onSignalFound();
        }
        if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            navigationListener.onSignalLost();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Hier keine Implementierung notwendig
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Hier keine Implementierung notwendig
    }
}
