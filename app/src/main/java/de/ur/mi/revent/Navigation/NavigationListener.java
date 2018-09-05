package de.ur.mi.revent.Navigation;

import com.google.android.gms.maps.model.LatLng;

public interface NavigationListener {
    public void onSignalFound();

    public void onSignalLost();

    public void onLocationChanged();
}