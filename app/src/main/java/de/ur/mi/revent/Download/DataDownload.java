package de.ur.mi.revent.Download;



import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;


import de.ur.mi.revent.Template.EventItem;


public class DataDownload extends AsyncTask<String, Void, Void>{
    private ArrayList<EventItem> table = new ArrayList<>();
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static DateTimeFormatter timeFormatter =  DateTimeFormatter.ofPattern("HH:mm:ss");
    //private static final String TITLE = "title";
    //private static final String TYPE = "type";
    //private static final String ORGANIZER = "organizer";

    private DownloadListener listener;

    public DataDownload() {
        //this.listener = listener;
        //this.table = table;
    }


    @Override
    protected Void doInBackground(String... params) {
        System.out.println("doInBackground");
        JSONArray jsonArray;
        new JSONObject();

        try {
            //check again
            jsonArray = getJSONArrayFromURL(params[0]);
            processJson(jsonArray);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(listener != null) {
            listener.onDownloadFinished();
        }
    }


    private void processJson(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectEvent = jsonArray.getJSONObject(i);

                String title = jsonObjectEvent.getString("title");
                String type = jsonObjectEvent.getString("type");
                String organizer = jsonObjectEvent.getString("organizer");
                String dateString = jsonObjectEvent.getString("date");
                String timeString = jsonObjectEvent.getString("time");
                String location = jsonObjectEvent.getString("address");

                LocalDate date = getDateFromString(dateString);
                LocalTime time = getTimeFromString(timeString);

                if(date.compareTo(LocalDate.now())>=0) {
                //Das Event wird nur dann hinzugefügt, falls es noch in der Zukunft (oder im Heute) liegt.
                    EventItem item = new EventItem(title, type, organizer, date, time, location);
                    table.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "ISO-8859-1"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        bufferedReader.close();
        urlConnection.disconnect();
        return new JSONArray(stringBuilder.toString());
    }

    private static LocalDate getDateFromString(String dateString){
        //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);
        //int year = date.getYear(); // 2018
        //int day = date.getDayOfMonth(); // 2
        //Month month = date.getMonth(); // JANUARY
        //int monthAsInt = month.getValue(); // 1
        return date;
    }

    private static LocalTime getTimeFromString(String timeString){
        //DateTimeFormatter timeFormatter =  DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(timeString, timeFormatter);
        return time;
    }

    public ArrayList<EventItem> getData() {
        return table;
    }

    public void setListener(DownloadListener listener) {
        this.listener = listener;
    }
}
