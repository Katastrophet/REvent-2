package de.ur.mi.revent;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

import de.ur.mi.revent.Menu._NavigationMenu;

public class Settings extends PreferenceActivity {
    private _NavigationMenu navigationMenu;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        navigationMenu=new _NavigationMenu(this);
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
}
