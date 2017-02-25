package com.example.mytravelapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mavis on 22/2/2017.
 */

public class databaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "travelDiary.db";
    public static final String USER_TABLE = "user";
    public static final String DIARY_TABLE = "diaries";

    //user table
    public static final String user_id = "ID";
    public static final String user_name = "USER_NAME";
    public static final String user_pass = "USER_PASSWORD";
    public static final String email_add = "EMAIL_ADDRESS";

    //diary table
    public static final String diary_id = "DIARY_ID";
    public static final String diary_image = "DIARY_IMG";
    public static final String diary_info ="DIARY_INFO";
    public static final String created_at = "CREATED_AT";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + USER_TABLE + "(" + user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + user_name + " TEXT, " + user_pass + " TEXT, " + email_add + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + DIARY_TABLE + "(" + diary_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + diary_image + " TEXT, " + diary_info +" TEXT, " + created_at + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DIARY_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUserData(String userName, String password, String emailAdd)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_name, userName);
        contentValues.put(user_pass, password);
        contentValues.put(email_add, emailAdd);

        long result = sqLiteDatabase.insert(USER_TABLE, null, contentValues); //this line got error, table not found , suspected db not initialised

        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean insertDiaryData(String dairyImg, String dairyInfo, String createAt)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(diary_image, dairyImg);
        contentValues.put(diary_info, dairyInfo);
        contentValues.put(created_at, createAt);

        long result = sqLiteDatabase.insert(DIARY_TABLE, null, contentValues);

        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor viewAllUserRecords()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE, null);

        return res;
    }

    public Cursor viewAllDairyRecords()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + DIARY_TABLE, null);

        return res;
    }

    public Integer deleteDiaryRecord(String id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(DIARY_TABLE, "DIARY_ID = ?", new String[] {id});
    }

    public Cursor viewSelectedDiaryRecord(String id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + DIARY_TABLE + " WHERE DIARY_ID = " + id, null);

        return res;
    }

    public Cursor viewSelectedDiaryDateRecord(String date)
    {
        String args[] = {date};
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + DIARY_TABLE + " WHERE CREATED_AT = ?", args);

        return res;
    }


}

