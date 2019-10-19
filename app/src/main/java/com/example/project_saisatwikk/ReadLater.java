package com.example.project_saisatwikk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ReadLater extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ReadLater.db";
    public static final String TABLE_NAME = "ReadLater";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_LINK = "LINK";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;

    public ReadLater(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE + " VARCHAR, " + COLUMN_LINK + " VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void insertRecord(PaperModel paper) {
        database = this.getReadableDatabase();
        database.execSQL("INSERT INTO " + TABLE_NAME + "(" + COLUMN_TITLE + "," + COLUMN_LINK + ") VALUES('" + paper.getTitle() + "','" + paper.getLink() + "')");
        database.close();
    }

    public ArrayList<PaperModel> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<PaperModel> titles = new ArrayList<>();
        PaperModel PaperModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                PaperModel = new PaperModel();
                PaperModel.setID(cursor.getString(0));
                PaperModel.setTitle(cursor.getString(1));
                PaperModel.setLink(cursor.getString(2));
                Log.i("Hell"+i,cursor.getString(2));
                titles.add(PaperModel);
            }
        }
        cursor.close();
        database.close();

        return titles;
    }

    public void deleteRecord(PaperModel paper) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, COLUMN_TITLE + " = ?", new String[]{paper.getTitle()});
        database.close();
    }
}


