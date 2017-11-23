package com.example.czjakac.a2048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import Common.Constants;

import static android.widget.Toast.LENGTH_LONG;

public class MenuActivity extends Activity {

    private Button keepGoing;
    private Button newGame;
    private Button multiplayer;
    private ToggleButton sounds;
    private SharedPreferences mySharedPref;
    private SharedPreferences.Editor mySharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initElements();
        initButtonClick();
        if(mySharedPref.contains("sounds")){
            sounds.setChecked(mySharedPref.getBoolean("sounds",true));
        }
        else {
            sounds.setChecked(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.trans_right_in,R.transition.trans_right_out);

    }

    private void initButtonClick(){
        keepGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_intent);
                overridePendingTransition(R.transition.trans_right_in,R.transition.trans_right_out);
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSharedPref();
                Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_intent);
                overridePendingTransition(R.transition.trans_right_in,R.transition.trans_right_out);
            }
        });

        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySharedEditor = mySharedPref.edit();
                boolean checked = sounds.isChecked();
                mySharedEditor.putBoolean("sounds",checked);
                mySharedEditor.apply();
            }
        });
    }

    private void initElements() {
        this.keepGoing = findViewById(R.id.menu_btn_keep);
        this.newGame = findViewById(R.id.menu_btn_new);
        this.multiplayer = findViewById(R.id.menu_btn_multiplayer);
        this.sounds = findViewById(R.id.menu_btn_sounds);
        mySharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
    }

    private void removeSharedPref(){
        mySharedEditor = mySharedPref.edit();
        mySharedEditor.remove("best");
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
}
