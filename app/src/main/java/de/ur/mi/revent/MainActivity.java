package de.ur.mi.revent;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DataDownload;
import de.ur.mi.revent.Template.EventItem;

public class MainActivity extends Activity implements DownloadListener {
    private ArrayList<EventItem> table = new ArrayList<EventItem>();
    private final static String ADDRESS = "https://json-server-android-db.herokuapp.com/events";

    private _NavigationMenu navigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationMenu=new _NavigationMenu(this);
        new DataDownload(this, table).execute(ADDRESS);
        System.out.println("Hello MainActivity");


    }


    @Override
    public void onDownloadFinished() {
        System.out.println("Download finished");
        System.out.println(table.get(2).getOrganizer());
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
