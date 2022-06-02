package com.example.backgammon.info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static String dbName = "db.db";
    public static final String TABLE_NAME = "Statistics";
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "games";
    public static final String COL4 = "wins";
    public static final String COL5 = "loses";
    public static final String COL6 = "percent";
    private static final String CREATE_TABLE =
            "create table if not exists " +
                    TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL2 + " TEXT NOT NULL,"
                    + COL3 + " INTEGER, " + COL4 + " INTEGER, " + COL5 + " TEXT, " + COL6 + " INTEGER);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, dbName, null, version);
        context = this.context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public boolean InsertPlayer(Statistics objEmp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, objEmp.getName());
        cv.put(COL3, objEmp.getGames());
        cv.put(COL4, objEmp.getWins());
        cv.put(COL5, objEmp.getLoses());
        cv.put(COL6, objEmp.getPercent());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean isInDatabase(String playerName) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean checked;
        Cursor cursor = db.query(TABLE_NAME, new String[]{"name"}, "name = " + playerName, null, null, null, null);
        cursor.moveToFirst();
        checked = cursor.getCount() != 0;
        cursor.close();
        return checked;
    }

    public boolean updatePlayer(String playerName, boolean win) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        HashMap<String, Object> player;

        Cursor cursor = db.query(TABLE_NAME, null, "name = " + playerName, null, null, null, null);

        int games = cursor.getInt(2);
        int wins = cursor.getInt(3);
        cursor.close();
        games++;
        if(win) wins++;

        Statistics statistics = new Statistics(playerName, games, wins);
        cv.put(COL3, games);
        cv.put(COL4, wins);
        cv.put(COL5, statistics.getLoses());
        cv.put(COL6, statistics.getPercent());

        long result = db.update(TABLE_NAME, cv,null,null);
        if (result == -1) return false;
        else return true;
    }
}
