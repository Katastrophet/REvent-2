package de.ur.mi.revent;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DataDownload;
import de.ur.mi.revent.Template.EventItem;

public class MainActivity extends Activity implements DownloadListener {
    private ArrayList<EventItem> table = new ArrayList<EventItem>();
    private final static String ADDRESS = "https://json-server-android-db.herokuapp.com/events";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DataDownload(this, table).execute(ADDRESS);
        System.out.println("Hello MainActivity");

    }


    @Override
    public void onDownloadFinished() {
        System.out.println("Download finished");
        System.out.println(table.get(2).getOrganizer());
    }
}
