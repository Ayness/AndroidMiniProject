package com.example.ramzi.sharpytodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ramzi on 01/12/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDo";
    private static final String TABLE_NAME = "user";
    private static final String KEY_LOGIN= "login";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWD = "passwd";

    public DBHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_LOGIN + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PASSWD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_LOGIN, user.getLogin());
        values.put(KEY_PASSWD, user.getPasswd());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
    public User getUser(String login) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_LOGIN,
                        KEY_NAME, KEY_PASSWD }, KEY_LOGIN + "=?",
                new String[] { String.valueOf(login) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount()>0) {
            User user = new User(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2));
            // return contact
            return user;
        }
        return null;
    }

}
