package com.example.czjakac.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import DBHelper.DBHelper;
import Entities.Score;

public class LeaderBoardActivity extends Activity {

    private Button back;
    private Button clear;
    final Context context = this;
    ListView listView;
    ListAdapter adapter;
    public  ArrayList<Score> scores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        initElements();
        initButtonClick();
    }

    private void initElements(){
        back = findViewById(R.id.lb_btn_back);
        clear = findViewById(R.id.lb_btn_clear);
        listView = findViewById(R.id.lb_lw_score);

        setListData();
        adapter  = new ListAdapter(this, R.layout.list_item, scores);
        listView.setAdapter(adapter);
    }

    private void initButtonClick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scores.size() != 0) {
                    new AlertDialog.Builder(context)
                            .setTitle("Reset Score")
                            .setMessage("Do you really want to reset score?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    DBHelper db = new DBHelper(context);
                                    db.deleteAll();
                                    scores.clear();
                                    listView.setAdapter(null);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });
    }

    private void setListData() {
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.getAllOrderByScore();
        while (cursor.moveToNext()){
            Score score  = new Score(cursor.getString(1),cursor.getString(2),cursor.getInt(3));
            score.setId(cursor.getInt(0));
            scores.add(score);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.transition.trans_bottom_in,R.transition.trans_bottom_out);
    }
}
