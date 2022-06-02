package com.example.backgammon;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backgammon.info.DatabaseHelper;
import com.example.backgammon.info.Statistics;
import com.example.backgammon.logic.Checker;
import com.example.backgammon.logic.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<Checker> checkers, left, right;
    ArrayAdapter<Checker> leftAdapter, rightAdapter;
    Game game;
    Context mContext;
    TextView curTurnView, whiteBarView, blackBarView;
    ImageView dv1, dv2, dv3, dv4;
    private Integer from;

    public static final String Player1 = "first player", Player2 = "second player";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mContext = this;

        //Определяю выводы

        curTurnView = findViewById(R.id.current_turn);
        whiteBarView = findViewById(R.id.white_bar);
        blackBarView = findViewById(R.id.black_bar);

        dv1 = findViewById(R.id.dice1);
        dv2 = findViewById(R.id.dice2);
        dv3 = findViewById(R.id.dice3);
        dv4 = findViewById(R.id.dice4);

        //Инициализация игры

        game = new Game();

        barShow();
        diceShow();

        curTurnShow();

        checkers = game.getLogicBoard().getBoard();
        left = checkers.subList(0, 12);
        right = checkers.subList(12, 24);
        from = game.from;

        //Инициализация и подключение адаптеров
        leftAdapter = new BoardAdapter(this, left);
        rightAdapter = new BoardAdapter(this, right);
        GridView leftView = findViewById(R.id.leftView);
        GridView rightView = findViewById(R.id.rightView);
        leftView.setAdapter(leftAdapter);
        rightView.setAdapter(rightAdapter);
        leftView.setOnItemClickListener(this);
        rightView.setOnItemClickListener(this);

        //Отключение прокрутки
        View.OnTouchListener scrollDisable = (v, event) -> event.getAction() == MotionEvent.ACTION_MOVE;
        leftView.setOnTouchListener(scrollDisable);
        rightView.setOnTouchListener(scrollDisable);

        //Обработчик для баров и текущего хода

        View.OnClickListener ocl = v -> {
            int id = v.getId();
            if (id == R.id.current_turn && from != null) {
                TextView tv = (TextView) findViewById(from);
                if (game.curTurn == 1) tv.setTextColor(Color.BLACK);
                else tv.setTextColor(Color.WHITE);
                from = null;

            } else if (id == R.id.black_bar || id == R.id.white_bar) {
                if (from != null && game.logicBoard.canPut(from, game.curTurn, game.dice1)) {
                    game.playerPut(from);
                    from = null;
                    barShow();
                    leftAdapter.notifyDataSetChanged();
                    rightAdapter.notifyDataSetChanged();
                    if (game.win) {
                        onWin();
                    }
                }

            }
        };

        curTurnView.setOnClickListener(ocl);
        blackBarView.setOnClickListener(ocl);
        whiteBarView.setOnClickListener(ocl);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int selectedId = view.getId();
        TextView tv = (TextView) view;

        //Перемещение шашки

        if (from == null && game.curTurn == checkers.get(selectedId).getColor() && game.possibleMoves.containsKey(selectedId)) {
            from = selectedId;
            /*
            Map<Integer,Integer> curPossibleMoves = new HashMap<>();

            curPossibleMoves.putAll(game.possibleMoves);

            while (curPossibleMoves.containsKey(from)){
                TextView possibleMoveView = (TextView) findViewById(curPossibleMoves.get(from));
                possibleMoveView.setBackgroundResource(R.drawable.highlighted);
                curPossibleMoves.remove(from);
            }
             */
            tv.setTextColor(Color.rgb(0, 255, 0));
        } else if (from != null && from.equals(selectedId)) {
            TextView fv = (TextView) findViewById(from);
            if (game.curTurn == 1) fv.setTextColor(Color.BLACK);
            else fv.setTextColor(Color.WHITE);
            from = null;
        } else if (from != null && game.logicBoard.canMove(from, selectedId, game.curTurn, game.dice1, game.dice2)) {
            game.playerMove(from, selectedId);
            TextView fv = findViewById(from);
            if (game.curTurn == 1) {
                fv.setTextColor(Color.BLACK);
            } else fv.setTextColor(Color.WHITE);
            from = null;
            game.target = null;

            leftAdapter.notifyDataSetChanged();
            rightAdapter.notifyDataSetChanged();
        }

        //Проверка победы

        if (game.win) {
            onWin();
        }

        //Конец хода и подготовка к следующему

        if ((game.uses1 == 0 && game.uses2 == 0) ||
                (game.possibleMoves.isEmpty())) {
            game.roll();
            game.prepareMove(game.curTurn);
            diceShow();
            curTurnShow();
        }

    }

    public void onWin() {
        String winnerName, loserName;
        if (game.curTurn == 1) {
            winnerName = Player1;
            loserName = Player2;
        } else {
            winnerName = Player2;
            loserName = Player1;
        }

        DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

        if (!dbh.isInDatabase(winnerName)) {
            dbh.InsertPlayer(new Statistics(winnerName, 1, 1));
        } else {
            dbh.updatePlayer(winnerName, true);
        }

        if (!dbh.isInDatabase(loserName)) {
            dbh.InsertPlayer(new Statistics(loserName, 1, 0));
        } else {
            dbh.updatePlayer(winnerName, false);
        }

        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }


    //Класс адаптера(создает фишки и контроллирует их состояние)

    private class BoardAdapter extends ArrayAdapter<Checker> {

        public BoardAdapter(Context context, List<Checker> currentList) {
            super(context, R.layout.board_item, currentList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) convertView;
            int id;

            if (convertView == null) {
                convertView = new TextView(mContext);
                label = (TextView) convertView;
                label.setHeight(128);
                switch (parent.getId()) {
                    case R.id.leftView:
                        switch (position) {
                            case 0:
                                id = 11;
                                break;
                            case 1:
                                id = 10;
                                break;
                            case 2:
                                id = 9;
                                break;
                            case 3:
                                id = 8;
                                break;
                            case 4:
                                id = 7;
                                break;
                            case 5:
                                id = 6;
                                break;
                            case 6:
                                id = 12;
                                break;
                            case 7:
                                id = 13;
                                break;
                            case 8:
                                id = 14;
                                break;
                            case 9:
                                id = 15;
                                break;
                            case 10:
                                id = 16;
                                break;
                            case 11:
                                id = 17;
                                break;
                            default:
                                id = position;
                                break;
                        }
                        break;
                    case R.id.rightView:
                        switch (position) {
                            case 0:
                                id = 5;
                                break;
                            case 1:
                                id = 4;
                                break;
                            case 2:
                                id = 3;
                                break;
                            case 3:
                                id = 2;
                                break;
                            case 4:
                                id = 1;
                                break;
                            case 5:
                                id = 0;
                                break;
                            case 6:
                                id = 18;
                                break;
                            case 7:
                                id = 19;
                                break;
                            case 8:
                                id = 20;
                                break;
                            case 9:
                                id = 21;
                                break;
                            case 10:
                                id = 22;
                                break;
                            case 11:
                                id = 23;
                                break;
                            default:
                                id = position;
                                break;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                label.setId(id);
            } else {
                id = label.getId();
            }
            int color = checkers.get(id).getColor();
            int count = checkers.get(id).getCount();

            if (color == 1) {
                label.setBackgroundResource(R.drawable.white);
                label.setTextColor(Color.BLACK);
            } else if (color == 2) {
                label.setBackgroundResource(R.drawable.black);
                label.setTextColor(Color.WHITE);

            } else {
                label.setBackgroundResource(0);
                label.setText("");
            }
            if (color != game.curTurn) label.setEnabled(false);
            if (count != 0) label.setText(String.valueOf(count));
            label.setGravity(Gravity.CENTER);
            return (convertView);
        }
    }

    //Вывод на экран элементов

    private void diceShow() {
        int diceImage1, diceImage2;
        if (game.uses1 == 2) {
            switch (game.dice1) {
                case 1:
                    diceImage1 = R.drawable.d1;
                    break;
                case 2:
                    diceImage1 = R.drawable.d2;
                    break;
                case 3:
                    diceImage1 = R.drawable.d3;
                    break;
                case 4:
                    diceImage1 = R.drawable.d4;
                    break;
                case 5:
                    diceImage1 = R.drawable.d5;
                    break;
                case 6:
                    diceImage1 = R.drawable.d6;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            dv1.setImageResource(diceImage1);
            dv2.setImageResource(diceImage1);
            dv3.setImageResource(diceImage1);
            dv4.setImageResource(diceImage1);
        } else {
            switch (game.dice1) {
                case 1:
                    diceImage1 = R.drawable.d1;
                    break;
                case 2:
                    diceImage1 = R.drawable.d2;
                    break;
                case 3:
                    diceImage1 = R.drawable.d3;
                    break;
                case 4:
                    diceImage1 = R.drawable.d4;
                    break;
                case 5:
                    diceImage1 = R.drawable.d5;
                    break;
                case 6:
                    diceImage1 = R.drawable.d6;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            switch (game.dice2) {
                case 1:
                    diceImage2 = R.drawable.d1;
                    break;
                case 2:
                    diceImage2 = R.drawable.d2;
                    break;
                case 3:
                    diceImage2 = R.drawable.d3;
                    break;
                case 4:
                    diceImage2 = R.drawable.d4;
                    break;
                case 5:
                    diceImage2 = R.drawable.d5;
                    break;
                case 6:
                    diceImage2 = R.drawable.d6;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            dv1.setImageResource(0);
            dv2.setImageResource(diceImage1);
            dv3.setImageResource(diceImage2);
            dv4.setImageResource(0);
        }
    }

    private void barShow() {
        whiteBarView.setText(String.valueOf(game.barWhite));
        blackBarView.setText(String.valueOf(game.barBlack));
    }

    private void curTurnShow() {
        int curTurnImage;
        if (game.curTurn == 1) curTurnImage = R.drawable.white;
        else curTurnImage = R.drawable.black;
        curTurnView.setBackgroundResource(curTurnImage);
    }

}