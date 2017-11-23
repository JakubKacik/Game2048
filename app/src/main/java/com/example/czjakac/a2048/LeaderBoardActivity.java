package com.example.czjakac.a2048;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import DBHelper.DBHelper;
import Entities.Score;

public class LeaderBoardActivity extends Activity {

    private Button back;
    private Button clear;

    ListView list;
    CustomAdapter adapter;
    public  LeaderBoardActivity CustomListView = null;
    public  ArrayList<Score> scores = new ArrayList<Score>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        back = findViewById(R.id.lb_btn_back);
        clear = findViewById(R.id.lb_btn_clear);

        CustomListView = this;

        setListData();

        Resources res =getResources();
        list = ( ListView )findViewById( R.id.lb_lw_score);  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter =new CustomAdapter( CustomListView, scores,res );
        list.setAdapter( adapter );

        initButtonClick();
    }

    private void initButtonClick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(menu_intent);
                overridePendingTransition(R.transition.trans_bottom_in,R.transition.trans_bottom_out);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setListData()
    {
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.getAllOrderByScore();
        while (cursor.moveToNext()){
            scores.add(new Score(cursor.getString(0),new Date(cursor.getString(1)),cursor.getInt(2)));
        }
    }
}
