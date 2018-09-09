package de.ur.mi.revent.Template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.ur.mi.revent.R;
import de.ur.mi.revent.Template.EventItem;

public class _EventItemArrayAdapter extends ArrayAdapter{
    private Context context;
    private ArrayList<EventItem> eventList;
    public _EventItemArrayAdapter(Context context, int resource, ArrayList<EventItem> eventList){
        super(context,resource,eventList);
        this.context=context;
        this.eventList=eventList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v=convertView;
        if (v==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.event_list_items, null);
        }
        EventItem event=eventList.get(position);
        if (event!=null){
            TextView eventTitle=(TextView)v.findViewById(R.id.event_title);
            TextView eventDate=(TextView)v.findViewById(R.id.event_date);
            TextView eventType=(TextView)v.findViewById(R.id.event_type);
            eventTitle.setText(event.getTitle());
            eventDate.setText(event.getDate().toString());
        }
        return v;
    }
}
