package Entities;

import android.widget.TextView;

/**
 * Created by CZJAKAC on 25.10.2017.
 */

public class Field {
    private int x;
    private int y;
    private Integer value;
    private TextView textView;

    public Field(int x, int y, Integer value, TextView textView) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.textView = textView;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
