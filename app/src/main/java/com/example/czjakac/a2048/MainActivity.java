package com.example.czjakac.a2048;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.Swipe;
import com.github.pwittchen.swipe.library.SwipeListener;

import java.util.List;

import BusinessLogic.BusinessLogicProcess;
import Common.Constants;
import Entities.Field;
import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;


import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends Activity{

    private Field[][] fields = new Field[Constants.SIZE][Constants.SIZE];
    private TextView score;
    private TextView best;
    private Button menu;
    private Button leaderboard;
    private Swipe swipe;
    BusinessLogicProcess blp;
    private SmoothBluetooth mSmoothBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSmoothBluetooth = new SmoothBluetooth(getApplicationContext(), SmoothBluetooth.ConnectionTo.ANDROID_DEVICE, SmoothBluetooth.Connection.SECURE, new SmoothBluetooth.Listener() {
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

        blp = new BusinessLogicProcess();
        initElements();
        initSwipe();
        initButtonClick();

        blp.newGame(this,fields,getDisplayWidth(), score, best);
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
                blp.swipeLeft(fields,score,best);
            }

            @Override
            public void onSwipingRight(MotionEvent event) {

            }

            @Override
            public void onSwipedRight(MotionEvent event) {
                blp.swipeRight(fields,score,best);
            }

            @Override
            public void onSwipingUp(MotionEvent event) {

            }

            @Override
            public void onSwipedUp(MotionEvent event) {
                blp.swipeUp(fields,score,best);
            }

            @Override
            public void onSwipingDown(MotionEvent event) {

            }

            @Override
            public void onSwipedDown(MotionEvent event) {
                blp.swipeDown(fields,score,best);
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
}
