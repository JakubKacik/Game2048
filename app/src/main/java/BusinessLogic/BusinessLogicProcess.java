package BusinessLogic;
import android.app.Activity;
import android.graphics.Color;
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
        for (int i = 0; i < Constants.SIZE ;i++){
            for (int j = 0; j < Constants.SIZE ;j++) {
                String s = "textView"+ Integer.toString(i) + Integer.toString(j);
                TextView textView = activity.findViewById(activity.getResources().getIdentifier(s, "id", activity.getPackageName()));
                Field field = fields[j][i]  = new Field(0,textView);

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

        for (int y = Constants.SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < Constants.SIZE; x++) {
                if(fields[x][y].getValue() != 0){
                    int row = y;
                    while(row + 1 < Constants.SIZE){
                        int v = fields[x][row+1].getValue();
                        if(v == 0){
                            int value = fields[x][row].getValue();
                            fields[x][row+1].setValue(value);
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
        clearMatrix(fields);
        redrawFields(fields);

        if(generateNew){
            generateRandomField(fields);
        }
    }

    public void swipeRight(Field[][] fields){

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
                            //playSound(rects[coll+1][y].value);
                            //score += rects[coll+1][y].value;
                            fields[coll][y].setValue(0);
                            //freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }
                }
            }
        }
        clearMatrix(fields);
        redrawFields(fields);

        if(generateNew){
            generateRandomField(fields);
        }
    }

    public void swipeUp(Field[][] fields){

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
                            //playSound(rects[x][row-1].value);
                            //score += rects[x][row-1].value;
                            fields[x][row].setValue(0);
                            //freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }

                }
            }
        }

        clearMatrix(fields);
        redrawFields(fields);
        if(generateNew){
            generateRandomField(fields);
        }
    }

    public void swipeLeft(Field[][] fields){

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
                            //playSound(rects[coll-1][y].value);
                            //score += rects[coll-1][y].value;
                            fields[coll][y].setValue(0);
                            //freeCells++;
                            generateNew = true;
                            break;
                        }
                        else break;
                    }
                }
            }
        }
        clearMatrix(fields);
        redrawFields(fields);

        //if(generateNew === false && freeCells === 0){
        //    gameOver();
        //}
        if(generateNew){
            generateRandomField(fields);
            generateNew = false;
        }

    }

    private void clearMatrix(Field[][] fields){
        for (int x = 0; x < Constants.SIZE;x++){
            for(int y = 0; y < Constants.SIZE ;y++){
                fields[x][y].getTextView().setBackgroundResource(R.drawable.emptyfield);
                fields[x][y].getTextView().setText("");
            }
        }
    }

    private void redrawFields(Field[][] fields){
        for (int x = 0; x < 4;x++){
            for(int y = 0; y< 4 ;y++){
                drawField(fields[x][y]);
            }
        }
    }
}
