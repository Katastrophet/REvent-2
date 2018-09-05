package de.ur.mi.revent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Template.EventItem;

public class MainActivity extends AppCompatActivity {
    private ArrayList<EventItem> table = new ArrayList<>();
    private Button buttonMap, buttonCommingEvents;
    private static final int PERMISSIONS_REQUEST_CODE = 0;

    @Override
    // TODO: Handle removal/denial of permission
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        } else {
            init();
        }
    }

    private void init() {
        initUI();
        DownloadManager.startDownload();
    }

    private void initUI(){
        setContentView(R.layout.activity_main);
        System.out.println("Hello MainActivity");

        buttonMap = findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showMap();
            }
        });

        buttonCommingEvents = findViewById(R.id.button_commingEvents);
        buttonCommingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommingEvents();
            }
        });
    }

    private void showMap() {
        Intent i = new Intent(this,  MapsActivity.class);
        startActivity(i);
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
}
