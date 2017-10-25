package BusinessLogic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.czjakac.a2048.R;

import java.util.Random;

import Common.Constants;
import Entities.Field;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by CZJAKAC on 25.10.2017.
 */

public class BusinessLogicProcess {

    private void fillMatrix(Activity activity,Field[][] fields, int displayWidth){
        for (int x = 0; x < Constants.SIZE ; x++){
            for (int y = 0; y < Constants.SIZE ;y++) {
                String s = "textView"+ Integer.toString(x) + Integer.toString(y);
                TextView textView = activity.findViewById(activity.getResources().getIdentifier(s, "id", activity.getPackageName()));
                Field field = fields[x][y]  = new Field(x,y,0,textView);

                float density = activity.getResources().getDisplayMetrics().density;
                float px = 65 * density;

                field.getTextView().setHeight((displayWidth - Math.round(px))/4);
                field.getTextView().setWidth((displayWidth - Math.round(px))/4);
            }
        }
    }

    private void generateRandomField(Field[][] fields){
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
                isWhite = false;
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
        }
        else{
            field.getTextView().setText(String.valueOf(field.getValue()));
        }

        if(isWhite){
            field.getTextView().setTextColor(Color.WHITE);
        }

    }

    public void newGame(Activity activity, Field[][] fields, int displayWidth, TextView score, TextView best){
        fillMatrix(activity,fields,displayWidth);

        setTwoLinesTextViews(score,"0");
        setTwoLinesTextViews(best,"0");

        generateRandomField(fields);
        generateRandomField(fields);
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

    public void swipeDown(Field[][] fields){

        boolean generateNew = false;

        for (int x = 2; x >= 0; x--) {
            for (int y = 0; y < 4; y++) {
                if(fields[x][y].getValue() != 0){
                    int row = x;
                    while(row + 1 < 4){
                        if(fields[x][row+1].getValue() == 0){
                            fields[x][row+1].setValue(fields[x][row].getValue());
                            //drawField(fields[x][row+1]);
                            fields[x][row].setValue(0);
                            //drawField(fields[x][row]);
                            row++;
                            generateNew = true;
                        }
                        else if(fields[x][row+1].getValue() == fields[x][row].getValue()){
                            fields[x][row+1].setValue(fields[x][row+1].getValue()*2);
                            //drawField(fields[x][row+1]);
                            //playSound(rects[x][row+1].value);
                            //score += rects[x][row+1].value;
                            fields[x][row].setValue(0);
                            drawField(fields[x][row]);
                            //freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }
                }
            }
        }

        //if(generateNew == false && freeCells == 0){
            //gameOver();
       // }
        redrawFields(fields);

        if(generateNew){
            generateRandomField(fields);
        }
    }

    private void redrawFields(Field[][] fields){

        for (int x = 1; x < 4;x++){
            for(int y = 0; y< 4 ;y++){

                drawField(fields[x][y]);
            }
        }
    }
}
