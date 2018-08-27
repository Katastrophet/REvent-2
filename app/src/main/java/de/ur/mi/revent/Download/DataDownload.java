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
import java.util.ArrayList;


import de.ur.mi.revent.Template.EventItem;


public class DataDownload extends AsyncTask<String, Void, Void> {

    private static final String TITLE = "TITEL";
    private static final String TYPE = "TYP";
    private static final String ORGANIZER = "VERANSTALTER";

    private ArrayList<EventItem> table;
    private DownloadListener listener;

    public DataDownload(DownloadListener listener, ArrayList<EventItem> table) {
        this.listener = listener;
        this.table = table;
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
        listener.onDownloadFinished();
    }

    private void processJson(JSONArray jsonArray) {
        try {
            JSONArray events = jsonArray.getJSONArray(0);
            System.out.println(events.length());
            //loop through teams
            for (int i = 0; i < events.length(); i++) {
                //get team object and print
                System.out.println("InLoop");
                JSONObject jsonObjectEvent = events.getJSONObject(i);
                System.out.println(jsonObjectEvent);
                //get desired values according to JSON structure
                String title = jsonObjectEvent.getString(TITLE);
                String type = jsonObjectEvent.getString(TYPE);
                String organizer = jsonObjectEvent.getString(ORGANIZER);
                //add to table
                EventItem item = new EventItem(title, type, organizer);
                table.add(item);
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
        System.out.println(stringBuilder.toString());
        return new JSONArray(stringBuilder.toString());
    }
}
