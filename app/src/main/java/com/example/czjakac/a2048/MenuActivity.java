package com.example.czjakac.a2048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import Common.Constants;
import Common.GameEngine;


public class MenuActivity extends Activity {

    private Button keepGoing;
    private Button newGame;
    private ToggleButton vibration;
    private ToggleButton sounds;
    private SharedPreferences mySharedPref;
    private SharedPreferences.Editor mySharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initElements();
        initButtonClick();
    }

    private void initButtonClick(){
        keepGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);
                GameEngine.bounceView(v);
                newGame.setEnabled(false);
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeSharedPref();
                        Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                        main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(main_intent);
                        onBackPressed();
                    }
                }, 1000);
                GameEngine.bounceView(v);
                keepGoing.setEnabled(false);
            }
        });

        vibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySharedEditor = mySharedPref.edit();
                mySharedEditor.putBoolean("vibrations",vibration.isChecked());
                mySharedEditor.apply();
                GameEngine.bounceView(v);
            }
        });

        sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySharedEditor = mySharedPref.edit();
                mySharedEditor.putBoolean("sounds",sounds.isChecked());
                mySharedEditor.apply();
                GameEngine.bounceView(v);
            }
        });
    }

    private void initElements() {
        this.keepGoing = findViewById(R.id.menu_btn_keep);
        this.newGame = findViewById(R.id.menu_btn_new);
        this.vibration = findViewById(R.id.menu_btn_vibration);
        this.sounds = findViewById(R.id.menu_btn_sounds);
        mySharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);

        if(mySharedPref.contains("sounds")){
            sounds.setChecked(mySharedPref.getBoolean("sounds",true));
        }
        else {
            sounds.setChecked(true);
        }

        if(mySharedPref.contains("vibrations")){
            vibration.setChecked(mySharedPref.getBoolean("vibrations",true));
        }
        else {
            vibration.setChecked(true);
        }
    }

    private void removeSharedPref(){
        mySharedEditor = mySharedPref.edit();
        mySharedEditor.remove("score");
        mySharedEditor.remove("free");
        mySharedEditor.remove("max");

        for (int i = 0; i < Constants.SIZE; i++) {
            for(int j = 0; j< Constants.SIZE;j++){
                mySharedEditor.remove(String.valueOf(i)+","+String.valueOf(j));
            }
        }
        mySharedEditor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.transition.trans_right_in,R.transition.trans_right_out);
    }
}
