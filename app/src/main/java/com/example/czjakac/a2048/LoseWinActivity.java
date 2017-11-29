package com.example.czjakac.a2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import Common.Constants;
import DBHelper.DBHelper;
import Entities.Score;

public class LoseWinActivity extends Activity {

    final Context context = this;
    private Button save;
    private Button newGame;
    private TextView score;
    private TextView title;
    private SharedPreferences mySharedPref;
    private SharedPreferences.Editor mySharedEditor;
    private int totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose_win);

        newGame = findViewById(R.id.lw_btn_newGame);
        save = findViewById(R.id.lw_btn_saveScore);
        score = findViewById(R.id.lw_tw_score);
        title = findViewById(R.id.lw_tw_title);

        Intent intent = getIntent();
        totalScore = intent.getIntExtra("score",0);
        score.setText(String.valueOf(totalScore));
        title.setText(intent.getStringExtra("title"));

        mySharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);



        initButtonClick();
    }

    private void initButtonClick(){
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSharedPref();
                Intent menu_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(menu_intent);
                overridePendingTransition(R.transition.trans_left_in,R.transition.trans_left_out);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.inputdialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                    if ( ! (userInput.getText().toString().equals(""))) {
                                        DBHelper db = new DBHelper(context);
                                        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                        db.insertScore(new Score(userInput.getText().toString(),date,totalScore));
                                        removeSharedPref();

                                        Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(main_intent);
                                        overridePendingTransition(R.transition.trans_right_in,R.transition.trans_right_out);
                                    }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
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
