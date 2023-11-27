package com.example2.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example2.newsapp.model.NewsData;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context  ) {
        super(context, Constants.DATABASE_NAME,null, Constants.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + Constants.TABLE_NAME  +" ("
                + Constants.COLOUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.COLOUMN_TITLE + " TEXT,"
                + Constants.COLOUMN_DESCRIPTION + " TEXT,"
                + Constants.COLOUMN_IMAGE_URL + " TEXT,"
                + Constants.COLOUMN_NEWS_URL + " TEXT )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public  long insertData(NewsData data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLOUMN_TITLE , data.getTitle());
        cv.put(Constants.COLOUMN_DESCRIPTION , data.getDesc());
        cv.put(Constants.COLOUMN_IMAGE_URL , data.getImageUrl());
        cv.put(Constants.COLOUMN_NEWS_URL , data.getNewsUrl());
        long id = db.insert(Constants.TABLE_NAME , null , cv);
        db.close();
        return  id;
    }
    public ArrayList<NewsData> getAllData() {
        ArrayList<NewsData> allData = new ArrayList<>();
        String query = "SELECT * FROM "+ Constants.TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query ,null);
        if(cursor.moveToFirst())
            do{
                NewsData data = new NewsData();
                data.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLOUMN_TITLE)));
                data.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLOUMN_DESCRIPTION)));
                data.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLOUMN_IMAGE_URL)));
                data.setNewsUrl(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLOUMN_NEWS_URL)));
                allData.add(data);

            }while (cursor.moveToNext());
        db.close();
        return allData;
    }

    public  void deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Constants.TABLE_NAME ,null , null);
        db.execSQL("delete from "+ Constants.TABLE_NAME);
        db.close();
    }
}
