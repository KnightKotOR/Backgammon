package com.example.backgammon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = findViewById(R.id.btnPlayers);
        Button leaderboard = findViewById(R.id.btnLeaderboard);
        play.setOnClickListener(this);
        leaderboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayers:
                Intent intent1 = new Intent(this, PlayerActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnLeaderboard:
                Intent intent2 = new Intent(this, LeaderboardActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}