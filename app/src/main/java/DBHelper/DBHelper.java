package DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import Entities.Score;

/**
 * Created by CZJAKAC on 10.11.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Score.db";
    public static final String Table_NAME = "Scores";
    public static final String SCORE_COLUMN_ID = "id";
    public static final String SCORE_COLUMN_NAME = "name";
    public static final String SCORE_COLUMN_SCORE = "score";
    public static final String SCORE_COLUMN_DATE = "date";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Scores(Id integer primary key autoincrement, Name text, Score integer,Date text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Scores");
        onCreate(db);
    }

    public void insertScore (Score score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", score.getName());
        contentValues.put("Score", score.getScore());
        contentValues.put("Date", score.getDate());
        db.insert("Scores", null, contentValues);
    }

    public Cursor getBestScore(){
        return db.rawQuery("SELECT Score FROM Scores ORDER BY score DESC LIMIT 1",null);
    }

    public Cursor getAllOrderByScore(){
        Cursor res = db.rawQuery("SELECT Id ,Name, Date, Score FROM Scores ORDER BY score DESC",null);
        return res;
    }

    public void deleteAll(){
        db.execSQL("DELETE FROM Scores");
    }

    public void delete(int id){
        db.delete("Scores","Id=?",new String[]{String.valueOf(id)});
    }
}
