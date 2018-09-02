package de.ur.mi.revent;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DataDownload;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Template.EventItem;

import de.ur.mi.revent.Download.DownloadListener;

public class CommingEventsActivity extends Activity implements DownloadListener {
    private ArrayList<EventItem> table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getDownloadData();
    }

    @Override
    public void onDownloadFinished() {
        try {
            table = DownloadManager.getResults();
            printData();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void getDownloadData(){
        try {
            if (DownloadManager.getStatus() == AsyncTask.Status.FINISHED) {
                table = DownloadManager.getResults();
                printData();
            } else {
                DownloadManager.setListener(this);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void printData(){
        System.out.println("printData");
        System.out.println(table);
    }
}