package de.ur.mi.revent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DataDownload;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Template.EventItem;

public class MainActivity extends Activity {
    private ArrayList<EventItem> table = new ArrayList<EventItem>();
    private final static String ADDRESS = "https://json-server-android-db.herokuapp.com/events";
    private Button buttonMap, buttonCommingEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadManager.startDownload();
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
}
