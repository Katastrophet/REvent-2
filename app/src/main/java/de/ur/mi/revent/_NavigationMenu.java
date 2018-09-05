package de.ur.mi.revent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class _NavigationMenu {
    private Activity activity;
    public _NavigationMenu(Activity activity){
        this.activity=activity;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.menu_Event: showEvent();break;
            case R.id.menu_KommendeEvents: showKommendeEvents();break;
            case R.id.menu_VorgemerkteEvents: showVorgemerkteEvents();break;
            case R.id.menu_REvent: showMainActivity();break;
            case R.id.menu_Maps: showMap();break;
        }
        return true;
    }

    public void showMap() {
        Intent i = new Intent(activity,  MapsActivity.class);
        activity.startActivity(i);
    }
    public void showEvent(){
        Intent i = new Intent(activity,  Event.class);
        activity.startActivity(i);
    }
    public void showVorgemerkteEvents(){
        Intent i = new Intent(activity,  VorgemerkteEvents.class);
        activity.startActivity(i);
    }
    public void showKommendeEvents(){
        Intent i = new Intent(activity,  KommendeEvents.class);
        activity.startActivity(i);
    }
    public void showMainActivity(){
        Intent i = new Intent(activity,  MainActivity.class);
        activity.startActivity(i);
    }
}
