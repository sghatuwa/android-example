package com.example.myjavaapps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myjava.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user";
    private static final String USER_ID = "id";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_EMAIL = "email";
    private static final String USER_GENDER = "gender";
    private static final String USER_CREATED_DATE = "created_date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_user_table = "create table "+TABLE_NAME+" ("+USER_ID+" integer primary key autoincrement," +
                USER_USERNAME+" string unique, "+USER_PASSWORD+" string, "+USER_EMAIL+" string unique, "+USER_GENDER+" string," +
                USER_CREATED_DATE+" string)";
        sqLiteDatabase.execSQL(create_user_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertStudent(String username, String password, String email,
                                 String gender, String created_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME, username);
        contentValues.put(USER_PASSWORD, password);
        contentValues.put(USER_EMAIL, email);
        contentValues.put(USER_GENDER, gender);
        contentValues.put(USER_CREATED_DATE, created_date);
        long status = db.insert(TABLE_NAME, USER_ID, contentValues);
        db.close();
        return (status != -1);
    }
}
