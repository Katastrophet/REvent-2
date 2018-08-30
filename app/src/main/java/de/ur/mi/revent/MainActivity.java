package de.ur.mi.revent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DataDownload;
import de.ur.mi.revent.Template.EventItem;

public class MainActivity extends Activity implements DownloadListener {
    private ArrayList<EventItem> table = new ArrayList<EventItem>();
    private final static String ADDRESS = "https://json-server-android-db.herokuapp.com/events";
    private Button buttonMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DataDownload(this, table).execute(ADDRESS);
        System.out.println("Hello MainActivity");
        buttonMap = (Button)findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showMap();
            }
        });

    }


    @Override
    public void onDownloadFinished() {
        System.out.println("Download finished");
        System.out.println(table.get(2).getOrganizer());
    }

    private void showMap() {
        Intent i = new Intent(this,  MapsActivity.class);
        startActivity(i);
    }
}
