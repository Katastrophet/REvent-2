package de.ur.mi.revent;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Template.EventItem;

public class KommendeEvents extends Activity implements DownloadListener{
    private _NavigationMenu navigationMenu;
    //EventList
    private ArrayList<EventItem> table;
    private ListView eventList_KE;
    private _EventItemArrayAdapter aa;
    //
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke);
        navigationMenu=new _NavigationMenu(this);
        //EventList
        eventList_KE=(ListView)findViewById(R.id.eventList_KE);
        table = new ArrayList<EventItem>();
        getDownloadData();
        Collections.sort(table);
        aa=new _EventItemArrayAdapter(this,R.layout.event_list_items,table);
        eventList_KE.setAdapter(aa);
        //aa.notifyDataSetChanged();

        eventList_KE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventItem eventItem =(EventItem) eventList_KE.getItemAtPosition(i);
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
        System.out.println(table.get(2).getOrganizer());
    }


}
