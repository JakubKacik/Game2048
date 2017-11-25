package Entities;

import java.util.Date;

/**
 * Created by CZJAKAC on 10.11.2017.
 */

public class Score {

    private String name;
    private String date;
    private int score;

    public Score(String name, String date, int score){
        this.name = name;
        this.date = date;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
