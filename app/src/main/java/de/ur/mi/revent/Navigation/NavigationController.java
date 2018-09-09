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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import de.ur.mi.revent.AppConfig.AppConfig;
import de.ur.mi.revent.MapsActivity;
import de.ur.mi.revent.R;

public class NavigationController implements LocationListener {

    private Context context;

    private NavigationListener navigationListener;
    private LocationManager locationManager;
    private Location lastKnownLocation;
    private String bestProvider;

    public NavigationController(Context context) {
        this.context = context.getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        setBestProvider();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
        }
    }

    private void setBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        bestProvider = locationManager.getBestProvider(criteria, true);
        if (bestProvider == null) {
            Log.e("setbestprovider", "no Provider set");
        }
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public void startNavigation(Context con) {
        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))){
            //Weise den Nutzer daraufhin, dass die Standortbestimmung aktiviert sein muss um den Standort anzuzeigen. Duh.
            Toast.makeText(con, R.string.gps_deactivated, Toast.LENGTH_SHORT).show();
        }
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(bestProvider, AppConfig.LOCATION_UPDATE_INTERVAL, AppConfig.LOCATION_DISTANCE_THRESHOLD, this);
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AppConfig.LOCATION_UPDATE_INTERVAL, AppConfig.LOCATION_DISTANCE_THRESHOLD, this);
            }
    }

    public void stopNavigation() {
        locationManager.removeUpdates(this);
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
        return new LatLng(latitude, longitude);
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
            return new LatLng(lat, lng);
        }
        catch (Exception e) {
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
