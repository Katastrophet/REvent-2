package de.ur.mi.revent;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Template.EventItem;

public class VorgeschlageneEventsActivity extends Activity implements DownloadListener {
    private _NavigationMenu navigationMenu;
    //EventList
    private ArrayList<EventItem> tableVorgeschlageneEvents;
    private ArrayList<EventItem> table;
    private ListView eventList_VorgeschlageneEvents;
    private _EventItemArrayAdapter aa;
    //
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vorgeschlagene_events);
        navigationMenu = new _NavigationMenu(this);
        //EventList
        tableVorgeschlageneEvents=new ArrayList<>();
        eventList_VorgeschlageneEvents=(ListView)findViewById(R.id.eventList_vorgeschlageneEvents);
        table = new ArrayList<EventItem>();
        getDownloadData();
        fillVorgeschlageneEvents();
        aa=new _EventItemArrayAdapter(this,R.layout.event_list_items,tableVorgeschlageneEvents);
        eventList_VorgeschlageneEvents.setAdapter(aa);
        //aa.notifyDataSetChanged();



        eventList_VorgeschlageneEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventItem eventItem =(EventItem) eventList_VorgeschlageneEvents.getItemAtPosition(i);
                navigationMenu.showEvent(eventItem.getTitle(),eventItem.getDate().toString(),eventItem.getTime().toString(),eventItem.getLocation(),eventItem.getOrganizer(),eventItem.getType());
            }
        });
        //


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        navigationMenu.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        navigationMenu.onOptionsItemSelected(item);
        return true;
    }
    @Override
    public void onDownloadFinished() {
        try {
            table = DownloadManager.getResults();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void getDownloadData(){
        try {
            if (DownloadManager.getStatus() == AsyncTask.Status.FINISHED) {
                table = DownloadManager.getResults();
            } else {
                DownloadManager.setListener(this);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void fillVorgeschlageneEvents(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        for (int i=0;table.size()>i;i++){
            switch (table.get(i).getType()){
                case "Party": if (sharedPref.getBoolean("pref_party",true)){
                    tableVorgeschlageneEvents.add(table.get(i));
                    }break;
                case "Ausflug":
                    if (sharedPref.getBoolean("pref_ausflug",true)){
                        tableVorgeschlageneEvents.add(table.get(i));
                    }break;
                case "Vortrag":
                    if (sharedPref.getBoolean("pref_vortrag",true)){
                        tableVorgeschlageneEvents.add(table.get(i));
                    }break;
            }
        }
        Collections.sort(tableVorgeschlageneEvents);
    }
}
