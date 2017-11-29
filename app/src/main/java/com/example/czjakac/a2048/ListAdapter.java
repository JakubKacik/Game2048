package com.example.czjakac.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import DBHelper.DBHelper;
import Entities.Score;

/**
 * Created by CZJAKAC on 23.11.2017.
 */

public class ListAdapter extends ArrayAdapter<Score> {


    public ListAdapter(Context context, int resource, List<Score> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.list_item, null);
        }

        Score score = getItem(position);

        if (score != null) {
            TextView tw_name = view.findViewById(R.id.li_name);
            TextView tw_date = view.findViewById(R.id.li_date);
            TextView tw_score = view.findViewById(R.id.li_score);

            tw_name.setText(score.getName());
            tw_date.setText(score.getDate());
            tw_score.setText(String.valueOf(score.getScore()));

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeScore(position);
                    return true;
                }
            });
        }
        return view;
    }

    private void removeScore(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure you want to delete this?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Score score  = getItem(position);
                DBHelper db = new DBHelper(getContext());
                db.delete(score.getId());
                remove(score);
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}