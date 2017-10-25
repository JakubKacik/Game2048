package Entities;

import android.widget.TextView;

/**
 * Created by CZJAKAC on 25.10.2017.
 */

public class Field {
    private int value;
    private TextView textView;

    public Field(Integer value, TextView textView) {
        this.value = value;
        this.textView = textView;
    }



    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
