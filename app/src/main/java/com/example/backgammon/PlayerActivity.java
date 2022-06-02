package com.example.backgammon;

import static com.example.backgammon.GameActivity.Player1;
import static com.example.backgammon.GameActivity.Player2;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PlayerActivity extends AppCompatActivity implements OnClickListener {
    Button play;
    EditText whitePlayer, blackPlayer;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        play = findViewById(R.id.btnPlay);
        whitePlayer = findViewById(R.id.editWhitePlayer);
        blackPlayer = findViewById(R.id.editBlackPlayer);

        play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        if (!checked(whitePlayer,blackPlayer)) return;
        intent.putExtra(Player1, whitePlayer.getText().toString());
        intent.putExtra(Player2, whitePlayer.getText().toString());
        startActivity(intent);
    }

    private boolean checked(EditText player1, EditText player2){
        String first = player1.getText().toString();
        String second = player2.getText().toString();
        return !first.isEmpty() && !second.isEmpty() && !first.equals(second);
    }
}