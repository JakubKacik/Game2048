package com.example.czjakac.a2048;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

    GridLayout gridLayout;


    TextView tw00;
    TextView tw01;
    TextView tw02;
    TextView tw03;

    TextView tw10;
    TextView tw11;
    TextView tw12;
    TextView tw13;

    TextView tw20;
    TextView tw21;
    TextView tw22;
    TextView tw23;

    TextView tw30;
    TextView tw31;
    TextView tw32;
    TextView tw33;

    TextView[][] fields = new TextView[][]{{tw00,tw01,tw02,tw03},{tw10,tw11,tw12,tw13},{tw20,tw21,tw22,tw23},{tw30,tw31,tw32,tw33}};

    int displayWidth;
    int displayHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillMatrix();
    }

    private void fillMatrix(){
        getDisplaySize();

        for (int i = 0; i< 4;i++){
            for (int j = 0; j<4;j++) {

                String s = "textView"+ Integer.toString(i) + Integer.toString(j);
                TextView tw = fields[i][j] = findViewById(getResources().getIdentifier(s, "id", this.getPackageName()));

                float density = getResources().getDisplayMetrics().density;
                float px = 65 * density;

                tw.setHeight((displayWidth - Math.round(px))/4);
                tw.setWidth((displayWidth - Math.round(px))/4);



                //tw.setHeight(displayWidth/4);
                //tw.setHeight(displayHeight/4);
            }
        }
    }

    private void getDisplaySize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;
    }



}
