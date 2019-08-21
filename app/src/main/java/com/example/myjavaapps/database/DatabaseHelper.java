package com.example.myjavaapps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myjavaapps.data.User;

import java.util.ArrayList;
import java.util.List;

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

    public boolean validateUser(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        String whereClauase = USER_USERNAME+ " = ? AND "+USER_PASSWORD +" = ?";
        String[] whereArgs = new String[]{
                username, password
        };
        Cursor cursor = db.query(TABLE_NAME, null, whereClauase, whereArgs, null,
                null, null);
        boolean status = false;
        if(cursor.getCount()>0){
            status = true;
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return status;
    }

    public User getUserData(String username){
        User user = null;
        SQLiteDatabase db =this.getReadableDatabase();
        db.beginTransaction();
        String whereClauase = USER_USERNAME+ " = ?";
        String[] whereArgs = new String[]{
                username
        };
        Cursor cursor = db.query(TABLE_NAME, null, whereClauase, whereArgs, null,
                null, null);
        if(cursor.getCount()> 0){
            cursor.moveToFirst();
            do{
                user = new User(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                System.out.println("cursor.getString(1) = " + cursor.getString(1));
                System.out.println("cursor.getString(2) = " + cursor.getString(2));
            }while(cursor.moveToNext());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return user;
    }

    public boolean updateuserData(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String whereClauase = USER_USERNAME+ " = ?";
        String[] whereArgs = new String[]{
                user.getUsername()
        };
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_EMAIL, user.getEmail());
        contentValues.put(USER_PASSWORD, user.getPassword());
        contentValues.put(USER_GENDER, user.getGender());

        int result = db.update(TABLE_NAME, contentValues, whereClauase, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        System.out.println("result = " + result);
        return (result > 0);
    }

    public List<User> getAllUserData(){
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db =this.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null,
                null, null);
        if(cursor.getCount()> 0){
            cursor.moveToFirst();
            do{
                User user = new User(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                userList.add(user);
                System.out.println("cursor.getString(1) = " + cursor.getString(1));
                System.out.println("cursor.getString(2) = " + cursor.getString(2));
            }while(cursor.moveToNext());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return userList;
    }
}
