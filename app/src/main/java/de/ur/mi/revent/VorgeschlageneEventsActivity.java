package de.ur.mi.revent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import de.ur.mi.revent.Download.DownloadListener;
import de.ur.mi.revent.Download.DownloadManager;
import de.ur.mi.revent.Menu._NavigationMenu;
import de.ur.mi.revent.Template.EventItem;
import de.ur.mi.revent.Template._EventItemArrayAdapter;

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
                navigationMenu.showEvent(eventItem.getTitle(),eventItem.getDate().toString(),eventItem.getTime().toString(),eventItem.getLocation(),eventItem.getOrganizer(),eventItem.getType(),eventItem.getNotes(),eventItem.getId());
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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
        ArrayList<EventItem> typeList=new ArrayList<EventItem>();
        ArrayList<EventItem> organList=new ArrayList<EventItem>();
        for (int i=0;table.size()>i;i++){
            switch (table.get(i).getType()) {
                case "Party":
                    if (sharedPref.getBoolean("pref_party", true)) {
                        typeList.add(table.get(i));
                    }
                    break;
                case "Ausflug":
                    if (sharedPref.getBoolean("pref_ausflug", true)) {
                        typeList.add(table.get(i));
                    }
                    break;
                case "Vortrag":
                    if (sharedPref.getBoolean("pref_vortrag", true)) {
                        typeList.add(table.get(i));
                    }
                    break;
            }
            switch (table.get(i).getOrganizer()){
                case "Fachschaft Biologie":
                    if (sharedPref.getBoolean("pref_FA_bio",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Chemie":
                    if (sharedPref.getBoolean("pref_FA_chem",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Geschichte":
                    if (sharedPref.getBoolean("pref_FA_ges",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Humanmedizin":
                    if (sharedPref.getBoolean("pref_FA_hum",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Jura":
                    if (sharedPref.getBoolean("pref_FA_jura",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Katholische Theologie":
                    if (sharedPref.getBoolean("pref_FA_kath",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Lehramt":
                    if (sharedPref.getBoolean("pref_FA_lehr",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft  Mathe":
                    if (sharedPref.getBoolean("pref_FA_math",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Physik":
                    if (sharedPref.getBoolean("pref_FA_phy",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Pharmazie":
                    if (sharedPref.getBoolean("pref_FA_phar",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Philosophie":
                    if (sharedPref.getBoolean("pref_FA_phil",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Politikwissenschaften":
                    if (sharedPref.getBoolean("pref_FA_poli",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Psychologie/Pädagogik/Sport":
                    if (sharedPref.getBoolean("pref_FA_psy",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Pädagogik":
                    if (sharedPref.getBoolean("pref_FA_päd",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Romanistik":
                    if (sharedPref.getBoolean("pref_FA_rom",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Slavistik":
                    if (sharedPref.getBoolean("pref_FA_slav",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Sport":
                    if (sharedPref.getBoolean("pref_FA_spo",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft SLK":
                    if (sharedPref.getBoolean("pref_FA_slk",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft SüdOst":
                    if (sharedPref.getBoolean("pref_FA_süd",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Wirtschaft":
                    if (sharedPref.getBoolean("pref_FA_wirt",true)){
                        organList.add(table.get(i));
                    }break;
                case "Fachschaft Zahnmedizin":
                    if (sharedPref.getBoolean("pref_FA_zahn",true)){
                        organList.add(table.get(i));
                    }break;
            }
        }
        for (int i=0;i<typeList.size();i++){
            for (int j=0;j<organList.size();j++){
                if (typeList.get(i).getId()==organList.get(j).getId()){
                    tableVorgeschlageneEvents.add(typeList.get(i));
                }
            }

        }
        Collections.sort(tableVorgeschlageneEvents);
    }
}
