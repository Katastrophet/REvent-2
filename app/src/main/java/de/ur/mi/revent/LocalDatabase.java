
package de.ur.mi.revent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


import de.ur.mi.revent.Template.EventItem;

public class LocalDatabase {
    private static final String DATABASE_NAME = "markedEvents.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "markedEvents";

    public static final String KEY_ID="id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ORGANIZER = "organizer";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_LOCATION = "location";

    public static final int COLUMN_TITLE_INDEX = 1;
    public static final int COLUMN_TYPE_INDEX = 2;
    public static final int COLUMN_ORGANIZER_INDEX = 3;
    public static final int COLUMN_DATE_INDEX = 4;
    public static final int COLUMN_TIME_INDEX = 5;
    public static final int COLUMN_LOCATION_INDEX = 6;

    private MarkedEventsDBOpenHelper dbHelper;

    private SQLiteDatabase db;

    public LocalDatabase(Context context) {
        dbHelper = new MarkedEventsDBOpenHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public void open() throws SQLException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public long insertEventItem(EventItem item) {
        ContentValues itemValues = new ContentValues();
        itemValues.put(KEY_TITLE, item.getTitle());
        itemValues.put(KEY_TYPE, item.getType());
        itemValues.put(KEY_ORGANIZER, item.getOrganizer());

        itemValues.put(KEY_DATE, item.getDate().toString());

        itemValues.put(KEY_TIME, item.getTime().toString());
        itemValues.put(KEY_LOCATION, item.getLocation());


        return db.insert(DATABASE_TABLE, null, itemValues);
    }

    public void removeEventItem(EventItem item) {
        String toDelete = KEY_TITLE + "=?";
        String[] deleteArguments = new String[]{item.getTitle()};
        db.delete(DATABASE_TABLE, toDelete, deleteArguments);
    }

    public ArrayList<EventItem> getAllEventItems() {
        ArrayList<EventItem> items = new ArrayList<EventItem>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[] { KEY_ID,KEY_TITLE,KEY_TYPE,KEY_ORGANIZER,KEY_DATE,KEY_TIME,KEY_LOCATION}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(COLUMN_TITLE_INDEX);
                String type = cursor.getString(COLUMN_TYPE_INDEX);
                String organizer = cursor.getString(COLUMN_ORGANIZER_INDEX);
                String strDate = cursor.getString(COLUMN_DATE_INDEX);
                LocalDate date=LocalDate.parse(strDate);
                String strTime = cursor.getString(COLUMN_TIME_INDEX);
                String location = cursor.getString(COLUMN_LOCATION_INDEX);

                System.out.println(title);
                items.add(new EventItem(title,type,organizer,date,LocalTime.parse(strTime),location));

            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(items);
        return items;
    }

    private class MarkedEventsDBOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_CREATE = "create table "
                + DATABASE_TABLE + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_TITLE + " text not null, "
                + KEY_TYPE + " text not null, "
                + KEY_ORGANIZER +" text not null, "
                + KEY_DATE + " text not null, "
                + KEY_TIME + " text, "
                + KEY_LOCATION + " text);";

        public MarkedEventsDBOpenHelper(Context c, String dbname,
                                        SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
