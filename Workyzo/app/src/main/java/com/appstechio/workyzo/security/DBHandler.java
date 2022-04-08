package com.appstechio.workyzo.security;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "securitydb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "mykeys";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    private static final String USERID_COL = "Userid";
    // below variable is for our course name column
    private static final String KEY_COL = "Secretkey";

    // below variable id for our course duration column.
   // private static final String DURATION_COL = "duration";

    // below variable for our course description column.
    //private static final String DESCRIPTION_COL = "description";

    // below variable is for our course tracks column.
    //private static final String TRACKS_COL = "tracks";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " TEXT PRIMARY KEY, "
                + KEY_COL + " TEXT)";


        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewKey(String userid,String privatekey) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ID_COL,userid);
        values.put(KEY_COL, privatekey);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    // we have created a new method for reading all the courses.
    public ArrayList<Keys> readKeys() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorKeys = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);


        // on below line we are creating a new array list.
        ArrayList<Keys> keyArrayList = new ArrayList<>();
        String id = null;
        String privatekey = null;
        // moving our cursor to first position.
        if (cursorKeys.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                keyArrayList.add(new Keys(cursorKeys.getString(0),cursorKeys.getString(1)));

            } while (cursorKeys.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorKeys.close();
        return keyArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
