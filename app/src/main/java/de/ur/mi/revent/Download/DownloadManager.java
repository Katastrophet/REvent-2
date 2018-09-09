package de.ur.mi.revent.Download;

import android.os.AsyncTask;
import java.util.ArrayList;

import de.ur.mi.revent.AppConfig.AppConfig;
import de.ur.mi.revent.Template.EventItem;

public class DownloadManager {
    private static DataDownload dataDownloader;
    private final static String ADDRESS = AppConfig.LINK;

    public static void startDownload(){
        dataDownloader = new DataDownload();
        dataDownloader.execute(ADDRESS);
    }

    public static void setListener(DownloadListener listener) throws Exception{
        if(dataDownloader != null) {
            dataDownloader.setListener(listener);
        } else{
            throw new Exception("download start failed!");
        }
    }

    public static AsyncTask.Status getStatus() throws Exception{
        if(dataDownloader != null) {
            return dataDownloader.getStatus();
        } else{
            throw new Exception("download start failed!");
        }
    }

    public static ArrayList<EventItem> getResults() throws Exception{
       if(dataDownloader != null) {
           return dataDownloader.getData();
       } else {
           throw new Exception("download start failed!");
       }
    }
}
