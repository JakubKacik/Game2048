package com.example.czjakac.a2048;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.Swipe;
import com.github.pwittchen.swipe.library.SwipeListener;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends Activity {

    private Field[] fields = new Field[16];
    private TextView score;
    private TextView best;
    private Button menu;
    private Button leaderboard;
    private Swipe swipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        fillMatrix();
        newgame();
        initSwipe();

        GridLayout gl = findViewById(R.id.gridLayout);
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
                Toast.makeText(getApplicationContext(),"onSwipedDown",LENGTH_LONG).show();
            }
        });
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private void initElements(){
        this.score = findViewById(R.id.m_tw_score);
        this.best = findViewById(R.id.m_tw_best);
        this.menu = findViewById(R.id.m_tw_menu);
        this.leaderboard = findViewById(R.id.m_tw_leaderboard);
    }

    private void newgame(){

        setTwoLinesTextViews(this.score,"0");
        setTwoLinesTextViews(this.best,"0");

    }

    private void fillMatrix(){
        for (int i = 0; i< 4;i++){
            for (int j = 0; j<4;j++) {
                String s = "textView"+ Integer.toString(i) + Integer.toString(j);
                TextView textView =  findViewById(getResources().getIdentifier(s, "id", this.getPackageName()));
                Field field = fields[(i * 4)+j]  = new Field(i,j,null,textView);

                float density = getResources().getDisplayMetrics().density;
                float px = 65 * density;

                field.getTextView().setHeight((getDisplayWidth() - Math.round(px))/4);
                field.getTextView().setWidth((getDisplayWidth() - Math.round(px))/4);
            }
        }
    }

    private int getDisplayWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void setTwoLinesTextViews(TextView textView,String value){
        String text1 = textView.getText().toString();
        String text2 = value;

        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(Math.round(textView.getTextSize())), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(75), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.WHITE),0,text2.length(), SPAN_INCLUSIVE_INCLUSIVE);

        CharSequence finalText = TextUtils.concat(span1, "\n", span2);
        textView.setText(finalText);
    }
}
