package com.example.backgammon;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backgammon.info.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class LeaderboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ArrayList<HashMap<String, Object>> players = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> player;
        DatabaseHelper dbh= new DatabaseHelper(getApplicationContext());


        Cursor cursor = dbh.getReadableDatabase().rawQuery("SELECT * FROM Statistics", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            player = new HashMap<String, Object>();
            player.put("name", cursor.getString(1));
            player.put("games", cursor.getInt(2));
            player.put("wins", cursor.getInt(3));
            player.put("loses", cursor.getInt(4));
            player.put("percent", cursor.getString(5));
            players.add(player);
            cursor.moveToNext();
        }
        cursor.close();

        String[] from = { "name", "games", "wins", "loses", "percent"};
        int[] to = { R.id.playerName, R.id.playerGames, R.id.playerWins, R.id.playerLoses, R.id.playerPercent};

        SimpleAdapter adapter = new SimpleAdapter(this, players, R.layout.statistics_item, from, to);
        ListView listView = findViewById(R.id.playerView);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}