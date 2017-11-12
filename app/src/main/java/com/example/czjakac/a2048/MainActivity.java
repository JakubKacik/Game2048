package com.example.czjakac.a2048;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.pwittchen.swipe.library.Swipe;
import com.github.pwittchen.swipe.library.SwipeListener;
import java.util.Random;
import Common.Constants;
import DBHelper.DBHelper;
import Entities.Field;
import io.palaima.smoothbluetooth.SmoothBluetooth;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends Activity{

    private Field[][] fields = new Field[Constants.SIZE][Constants.SIZE];
    private TextView score;
    private TextView best;
    private Button menu;
    private Button leaderboard;
    private Swipe swipe;
    private int freeCells;
    private int totalScore;
    private int bestScore;
    private int maxNum;
    private SmoothBluetooth mSmoothBluetooth;
    private SharedPreferences mySharedPref;
    private SharedPreferences.Editor mySharedEditor;
    private MediaPlayer join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        /*mSmoothBluetooth = new SmoothBluetooth(getApplicationContext(), SmoothBluetooth.ConnectionTo.ANDROID_DEVICE, SmoothBluetooth.Connection.SECURE, new SmoothBluetooth.Listener() {
            @Override
            public void onBluetoothNotSupported() {
                //device does not support bluetooth
            }

            @Override
            public void onBluetoothNotEnabled() {
                //bluetooth is disabled, probably call Intent request to enable bluetooth
            }

            @Override
            public void onConnecting(Device device) {
                //called when connecting to particular device
            }

            @Override
            public void onConnected(Device device) {
                //called when connected to particular device
            }

            @Override
            public void onDisconnected() {
                //called when disconnected from device
            }

            @Override
            public void onConnectionFailed(Device device) {
                //called when connection failed to particular device
            }

            @Override
            public void onDiscoveryStarted() {
                //called when discovery is started
            }

            @Override
            public void onDiscoveryFinished() {
                //called when discovery is finished
            }

            @Override
            public void onNoDevicesFound() {
                //called when no devices found
            }

            @Override
            public void onDevicesFound(final List<Device> deviceList, final SmoothBluetooth.ConnectionCallback connectionCallback) {
                for ( int i = 0 ; i < deviceList.size();i++  ){
                    deviceList.get(i).getName();
                }
            }

            @Override
            public void onDataReceived(int data) {
                //receives all bytes
            }
        });
        */

        initElements();
        initSwipe();
        initButtonClick();
        join = MediaPlayer.create(this,R.raw.join);

        mySharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
        if(!mySharedPref.contains("best")){
            newGame();
        }
        else {
            LoadGame();
        }
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
        this.swipe = new Swipe(20,200);
        swipe.setListener(new SwipeListener() {
            @Override
            public void onSwipingLeft(MotionEvent event) {
            }

            @Override
            public void onSwipedLeft(MotionEvent event) {
                swipeLeft();
            }

            @Override
            public void onSwipingRight(MotionEvent event) {

            }

            @Override
            public void onSwipedRight(MotionEvent event) {
                swipeRight();
            }

            @Override
            public void onSwipingUp(MotionEvent event) {

            }

            @Override
            public void onSwipedUp(MotionEvent event) {
                swipeUp();
            }

            @Override
            public void onSwipingDown(MotionEvent event) {

            }

            @Override
            public void onSwipedDown(MotionEvent event) {
                swipeDown();
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

    private void fillMatrix(){
        for (int i = 0; i < Constants.SIZE ;i++){
            for (int j = 0; j < Constants.SIZE ;j++) {
                String s = "textView"+ Integer.toString(i) + Integer.toString(j);
                TextView textView = this.findViewById(this.getResources().getIdentifier(s, "id", this.getPackageName()));
                Field field = fields[j][i]  = new Field(0,textView);

                float density = this.getResources().getDisplayMetrics().density;
                float px = 65 * density;

                field.getTextView().setHeight((getDisplayWidth() - Math.round(px))/4);
                field.getTextView().setWidth((getDisplayWidth() - Math.round(px))/4);
            }
        }
    }

    private void generateRandomField(){
        int x = 0;
        int y = 0;
        boolean isGenerated = false;
        Random rnd = new Random();

        while (!isGenerated){
            x = rnd.nextInt(4);
            y = rnd.nextInt(4);
            if(fields[x][y].getValue() == 0){
                isGenerated = true;
            }
        }

        int value = rnd.nextInt();
        if(value % 6 == 0){
            value = 4;
        }else{
            value = 2;
        }
        fields[x][y].setValue(value);
        drawField(fields[x][y]);
        freeCells--;
    }

    private void drawField(Field field){

        int id;
        boolean isWhite = true;

        switch (field.getValue()){
            case 2:
                id = R.drawable.field2;
                isWhite = false;
                break;
            case 4:
                id = R.drawable.field4;
                isWhite = false;
                break;
            case 8:
                id = R.drawable.field8;
                break;
            case 16:
                id = R.drawable.field16;
                break;
            case 32:
                id = R.drawable.field32;
                break;
            case 64:
                id = R.drawable.field64;
                break;
            case 128:
                id = R.drawable.field128;
                break;
            case 256:
                id = R.drawable.field256;
                break;
            case 512:
                id = R.drawable.field512;
                break;
            case 1024:
                id = R.drawable.field1024;
                break;
            case 2048:
                id = R.drawable.field2048;
                break;
            default:
                id = R.drawable.emptyfield;
                break;
        }

        field.getTextView().setBackgroundResource(id);

        if(field.getValue() == 0){
            field.getTextView().setText("");
            field.getTextView().setTextColor(Color.GRAY);
        }
        else{
            field.getTextView().setText(String.valueOf(field.getValue()));
            field.getTextView().setTextColor(Color.GRAY);
        }

        if(isWhite){
            field.getTextView().setTextColor(Color.WHITE);
        }

    }

    public void newGame(){
        freeCells = Constants.SIZE * Constants.SIZE;
        fillMatrix();

        totalScore = 0;
        maxNum = 4;

        Cursor res =  new DBHelper(this).getBestScore();
        if(res.getCount() == 0){
            bestScore = 0;
        }
        else{
            res.moveToFirst();
            bestScore = res.getInt(0);
        }

        setTwoLinesTextViews(score,"Score",String.valueOf(totalScore));
        setTwoLinesTextViews(best,"Best",String.valueOf(bestScore));

        generateRandomField();
        generateRandomField();

        saveSharedPref();
    }

    private void LoadGame(){
        freeCells = mySharedPref.getInt("free",Context.MODE_PRIVATE);
        bestScore = mySharedPref.getInt("best",Context.MODE_PRIVATE);
        totalScore = mySharedPref.getInt("score",Context.MODE_PRIVATE);
        maxNum = mySharedPref.getInt("max",Context.MODE_PRIVATE);

        fillMatrix();

        for (int i = 0; i < Constants.SIZE; i ++){
            for (int j = 0; j < Constants.SIZE;j++){
                fields[i][j].setValue(mySharedPref.getInt(String.valueOf(i)+","+String.valueOf(j),Context.MODE_PRIVATE));
            }
        }

        setTwoLinesTextViews(score,"Score",String.valueOf(totalScore));
        setTwoLinesTextViews(best,"Best",String.valueOf(bestScore));

        redrawFields();
    }

    private void setTwoLinesTextViews(TextView textView,String name,String value){
        String text1 = name;
        String text2 = value;

        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(Math.round(textView.getTextSize())), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(75), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.WHITE),0,text2.length(), SPAN_INCLUSIVE_INCLUSIVE);

        CharSequence finalText = TextUtils.concat(span1, "\n", span2);
        textView.setText(finalText);
    }

    public void swipeDown(){

        boolean generateNew = false;

        for (int y = Constants.SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < Constants.SIZE; x++) {
                if(fields[x][y].getValue() != 0){
                    int row = y;
                    while(row + 1 < Constants.SIZE){
                        int v = fields[x][row+1].getValue();
                        if(v == 0){
                            int value = fields[x][row].getValue();
                            fields[x][row+1].setValue(value);
                            fields[x][row].setValue(0);
                            row++;
                            generateNew = true;
                        }
                        else if(fields[x][row+1].getValue() == fields[x][row].getValue()){
                            int value = fields[x][row+1].getValue()*2;
                            fields[x][row+1].setValue(value);
                            totalScore += fields[x][row+1].getValue();
                            setTwoLinesTextViews(score,"Score",String.valueOf(totalScore));
                            if(totalScore > bestScore){
                                setTwoLinesTextViews(best,"Best",String.valueOf(totalScore));
                                bestScore = totalScore;
                            }
                            PlaySound(value);
                            fields[x][row].setValue(0);
                            freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }
                }
            }
        }
        clearMatrix();
        redrawFields();

        if(!generateNew && freeCells == 0){
            removeSharedPref();
        }

        if(generateNew){
            generateRandomField();
        }
        saveSharedPref();
    }

    public void swipeRight(){

        boolean generateNew = false;

        for (int y = 0; y < Constants.SIZE; y++) {
            for (int x = Constants.SIZE -1 ; x >= 0; x--) {
                if(fields[x][y].getValue() != 0){
                    int coll = x;
                    while(coll < Constants.SIZE - 1){
                        if(fields[coll+1][y].getValue() == 0){
                            int value = fields[coll][y].getValue();
                            fields[coll+1][y].setValue(value);
                            fields[coll][y].setValue(0);
                            coll++;
                            generateNew = true;
                        }
                        else if(fields[coll][y].getValue() == fields[coll+1][y].getValue()){
                            int value = fields[coll+1][y].getValue() *2;
                            fields[coll+1][y].setValue(value);
                            totalScore += value;
                            setTwoLinesTextViews(score,"Score",String.valueOf(totalScore));
                            if(totalScore > bestScore){
                                setTwoLinesTextViews(best,"Best",String.valueOf(totalScore));
                                bestScore = totalScore;
                            }
                            PlaySound(value);
                            fields[coll][y].setValue(0);
                            freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }
                }
            }
        }

        clearMatrix();
        redrawFields();

        if(!generateNew && freeCells == 0){
            removeSharedPref();
        }

        if(generateNew){
            generateRandomField();
        }
        saveSharedPref();
    }

    public void swipeUp(){

        boolean generateNew = false;

        for (int y = 0; y < Constants.SIZE; y++) {
            for (int x = 0; x < Constants.SIZE ; x++) {
                if(fields[x][y].getValue() != 0){
                    int row = y;
                    while(row > 0){
                        if(fields[x][row-1].getValue() == 0){
                            int value = fields[x][row].getValue();
                            fields[x][row-1].setValue(value);
                            fields[x][row].setValue(0);
                            row--;
                            generateNew = true;
                        }
                        else if(fields[x][row-1].getValue() == fields[x][row].getValue()){
                            int value = fields[x][row-1].getValue()*2;
                            fields[x][row-1].setValue(value);
                            totalScore += value;
                            setTwoLinesTextViews(score,"Score",String.valueOf(totalScore));
                            if(totalScore > bestScore){
                                setTwoLinesTextViews(best,"Best",String.valueOf(totalScore));
                                bestScore = totalScore;
                            }
                            PlaySound(value);
                            fields[x][row].setValue(0);
                            freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }
                }
            }
        }
        clearMatrix();
        redrawFields();

        if(!generateNew && freeCells == 0){
            removeSharedPref();
        }

        if(generateNew){
            generateRandomField();
        }
        saveSharedPref();
    }

    public void swipeLeft(){

        boolean generateNew = false;

        for (int y = 0; y < Constants.SIZE; y++) {
            for (int x = 0; x < Constants.SIZE; x++) {
                if(fields[x][y].getValue() != 0){
                    int coll = x;
                    while(coll - 1 >= 0){
                        if(fields[coll-1][y].getValue() == 0){
                            int value = fields[coll][y].getValue();
                            fields[coll-1][y].setValue(value);
                            fields[coll][y].setValue(0);
                            coll--;
                            generateNew = true;
                        }
                        else if(fields[coll][y].getValue() == fields[coll-1][y].getValue()){
                            int value = fields[coll-1][y].getValue() * 2;
                            fields[coll-1][y].setValue(value);
                            totalScore += value;
                            setTwoLinesTextViews(score,"Score",String.valueOf(totalScore));
                            if(totalScore > bestScore){
                                setTwoLinesTextViews(best,"Best",String.valueOf(totalScore));
                                bestScore = totalScore;
                            }
                            PlaySound(value);
                            fields[coll][y].setValue(0);
                            freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }
                }
            }
        }
        clearMatrix();
        redrawFields();

        if(!generateNew && freeCells == 0){
            removeSharedPref();
        }

        if(generateNew){
            generateRandomField();
        }
        saveSharedPref();
    }

    private void clearMatrix(){
        for (int x = 0; x < Constants.SIZE;x++){
            for(int y = 0; y < Constants.SIZE ;y++){
                fields[x][y].getTextView().setBackgroundResource(R.drawable.emptyfield);
                fields[x][y].getTextView().setText("");
            }
        }
    }

    private void redrawFields(){
        for (int x = 0; x < 4;x++){
            for(int y = 0; y< 4 ;y++){
                drawField(fields[x][y]);
            }
        }
    }

    private void saveSharedPref(){
        mySharedEditor = mySharedPref.edit();
        mySharedEditor.putInt("best",bestScore);
        mySharedEditor.putInt("score",totalScore);
        mySharedEditor.putInt("free",freeCells);
        mySharedEditor.putInt("max",maxNum);
        for (int i = 0; i < Constants.SIZE; i++) {
            for(int j = 0; j< Constants.SIZE;j++){
                mySharedEditor.putInt(String.valueOf(i)+","+String.valueOf(j),fields[j][i].getValue());
            }
        }
        mySharedEditor.apply();
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

    private void PlaySound(int value){
        if(value > maxNum){
            if(value == 2048){
                MediaPlayer win = MediaPlayer.create(this,R.raw.cheer);
                win.start();
                removeSharedPref();
            }
            else {
                join.start();
            }
            maxNum = value;
        }
    }
}
