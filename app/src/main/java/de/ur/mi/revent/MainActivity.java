package de.ur.mi.revent;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DataDownload;
import de.ur.mi.revent.Template.EventItem;

public class MainActivity extends Activity implements DownloadListener {
    private ArrayList<EventItem> table;
    private final static String ADDRESS = "localhost:3000/events";
    //json-server --host 192.168.1.XXX db.json

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DataDownload(this, table).execute(ADDRESS);

    }


    @Override
    public void onDownloadFinished() {
        System.out.println("Download finished");
    }
}
