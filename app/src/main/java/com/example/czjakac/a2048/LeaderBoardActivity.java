package com.example.czjakac.a2048;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import DBHelper.DBHelper;
import Entities.Score;

public class LeaderBoardActivity extends Activity {

    private Button back;
    private Button clear;
    final Context context = this;

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

        Resources res = getResources();
        list = ( ListView )findViewById( R.id.lb_lw_score);  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter = new CustomAdapter( CustomListView, scores,res );
        list.setAdapter( adapter );

        initButtonClick();
    }

    private void initButtonClick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu_intent = new Intent(context, MainActivity.class);
                startActivity(menu_intent);
                overridePendingTransition(R.transition.trans_bottom_in,R.transition.trans_bottom_out);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"fsadfdsa",Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(context)
                        .setTitle("Reset Score")
                        .setMessage("Do you really want to reset score?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DBHelper db = new DBHelper(context);
                                db.deleteAll();
                                adapter.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper db = new DBHelper(context);
                                db.insertScore(new Score("Jakub","25/11/2017",482002));
                                adapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
    }

    private void setListData()
    {
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.getAllOrderByScore();
        while (cursor.moveToNext()){
            scores.add(new Score(cursor.getString(0),cursor.getString(1),cursor.getInt(2)));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.trans_bottom_in,R.transition.trans_bottom_out);
    }
}
