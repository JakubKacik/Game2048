package com.example.czjakac.a2048;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.Swipe;
import com.github.pwittchen.swipe.library.SwipeListener;

import BusinessLogic.BusinessLogicProcess;
import Common.Constants;
import Entities.Field;


import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends Activity{


    private Field[][] fields = new Field[Constants.SIZE][Constants.SIZE];
    private TextView score;
    private TextView best;
    private Button menu;
    private Button leaderboard;
    private Swipe swipe;
    BusinessLogicProcess blp = new BusinessLogicProcess();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElements();
        initSwipe();
        initButtonClick();

        blp.newGame(this,fields,getDisplayWidth(), score, best);

    }


    private void initButtonClick(){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Menu",LENGTH_LONG).show();
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Leaderboard",LENGTH_LONG).show();
            }
        });
    }

    private void initSwipe(){
        this.swipe = new Swipe();
        swipe.setListener(new SwipeListener() {
            @Override
            public void onSwipingLeft(MotionEvent event) {
                //Toast.makeText(getApplicationContext(),"onSwipingLeft",LENGTH_LONG).show();
            }

            @Override
            public void onSwipedLeft(MotionEvent event) {
                Toast.makeText(getApplicationContext(),"onSwipedLeft",LENGTH_LONG).show();
            }

            @Override
            public void onSwipingRight(MotionEvent event) {
                //Toast.makeText(getApplicationContext(),"onSwipingright",LENGTH_LONG).show();
            }

            @Override
            public void onSwipedRight(MotionEvent event) {
                Toast.makeText(getApplicationContext(),"onSwipedRight",LENGTH_LONG).show();
            }

            @Override
            public void onSwipingUp(MotionEvent event) {
                //Toast.makeText(getApplicationContext(),"onSwipingUp",LENGTH_LONG).show();
            }

            @Override
            public void onSwipedUp(MotionEvent event) {
                Toast.makeText(getApplicationContext(),"onSwipedUp",LENGTH_LONG).show();
            }

            @Override
            public void onSwipingDown(MotionEvent event) {
                //Toast.makeText(getApplicationContext(),"onSwipingDown",LENGTH_LONG).show();
            }

            @Override
            public void onSwipedDown(MotionEvent event) {
                blp.swipeDown(fields);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private void initElements() {
        this.score = findViewById(R.id.m_tw_score);
        this.best = findViewById(R.id.m_tw_best);
        this.menu = findViewById(R.id.m_tw_menu);
        this.leaderboard = findViewById(R.id.m_tw_leaderboard);
    }

    private int getDisplayWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
