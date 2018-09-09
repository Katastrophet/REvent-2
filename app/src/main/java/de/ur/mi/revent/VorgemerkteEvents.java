package de.ur.mi.revent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.ur.mi.revent.LocalDatabase.LocalDatabase;
import de.ur.mi.revent.Menu._NavigationMenu;
import de.ur.mi.revent.Template.EventItem;
import de.ur.mi.revent.Template._EventItemArrayAdapter;

public class VorgemerkteEvents extends Activity{
    private _NavigationMenu navigationMenu;
    private LocalDatabase markedEventsDatabase;
    private ListView eventList_VorgemerkteEvents;
    private ArrayList<EventItem>markedEventsList;
    private _EventItemArrayAdapter aa;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marked_events);
        navigationMenu=new _NavigationMenu(this);
        eventList_VorgemerkteEvents=(ListView)findViewById(R.id.eventList_vorgemerkteEvents);
        markedEventsDatabase=MainActivity.getMarkedEventsDatabase();
        markedEventsList=markedEventsDatabase.getAllEventItems();

        aa=new _EventItemArrayAdapter(this,R.layout.event_list_items,markedEventsList);
        eventList_VorgemerkteEvents.setAdapter(aa);

        eventList_VorgemerkteEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventItem eventItem =(EventItem) eventList_VorgemerkteEvents.getItemAtPosition(i);
                navigationMenu.showEvent(eventItem.getTitle(),eventItem.getDate().toString(),eventItem.getTime().toString(),eventItem.getLocation(),eventItem.getOrganizer(),eventItem.getType(),eventItem.getNotes(),eventItem.getId());
            }
        });
    }

    @Override
    public void onRestart(){
        super.onRestart();
        markedEventsList=markedEventsDatabase.getAllEventItems();
        aa.notifyDataSetChanged();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
