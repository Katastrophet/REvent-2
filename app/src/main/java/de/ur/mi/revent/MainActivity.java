package de.ur.mi.revent;

import android.app.ActionBar;
import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DataDownload;
import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Template.EventItem;

public class MainActivity extends Activity implements DownloadListener {
    private ArrayList<EventItem> table = new ArrayList<EventItem>();
    private final static String ADDRESS = "https://json-server-android-db.herokuapp.com/events";
    private _NavigationMenu navigationMenu;
    private Button buttonCommingEvents;
    private static final int PERMISSIONS_REQUEST_CODE = 0;

    @Override
    //  TODO: Handle removal/denial of permission
    //  TODO: Sort List
    //  TODO: Show Map in Event
    //  TODO: Show Distance in ?(Event)
    //  TODO:
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        } else {
            init();
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    private void init() {
        initUI();
        DownloadManager.startDownload();
    }

    private void initUI(){
        setContentView(R.layout.activity_main);
        navigationMenu=new _NavigationMenu(this);
        System.out.println("Hello MainActivity");


        buttonCommingEvents = findViewById(R.id.button_commingEvents);
        buttonCommingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommingEvents();
            }
        });
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

    private void showCommingEvents() {
        Intent i = new Intent(this,  CommingEventsActivity.class);
        startActivity(i);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                    init();
                } else {
                    Toast.makeText(this, "Permissions not granted", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onDownloadFinished() {

    }
}
