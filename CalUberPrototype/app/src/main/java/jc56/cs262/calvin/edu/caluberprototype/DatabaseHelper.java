package jc56.cs262.calvin.edu.caluberprototype;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;
import android.widget.Toast;

import java.security.KeyStore;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RidesManager.db";
    private static final String TABLE_USER = "User";
    private static final String KEY_ID = "UserId";
    private static final String KEY_STUDENTID = "StudentId";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_LASTNAME = "LastName";
    private static final String KEY_FIRSTNAME = "FirstName";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USER + " ("
                + KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_STUDENTID + "TEXT, "
                + KEY_PASSWORD + "TEXT, "   //need to find a way to encrypt
                + KEY_LASTNAME + "TEXT, "
                + KEY_FIRSTNAME + "TEXT "
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getUserId());
        values.put(KEY_STUDENTID, user.getStudentId());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_LASTNAME, user.getLastName());
        values.put(KEY_FIRSTNAME,user.getFirstName());

        long result = db.insert(TABLE_USER, null, values);

        if(result == -1) {
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public Cursor getUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER;
        Cursor user = db.rawQuery(query, null);
        return user;
    }

//    User getUserId(int userId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_USER , new String[] {KEY_ID, KEY_STUDENTID, KEY_PASSWORD, KEY_LASTNAME, KEY_FIRSTNAME},
//                KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
//        if(cursor != null) {
//            cursor.moveToFirst();
//        }
//
//        User user = new User(cursor);
//
//
//    }


    // TODO: Move these codes to logInActivity.java
    /*
        All codes below need to go into the logInActivity.java
     */

//    public void addUser(User newUser) {
//        boolean insertData = databaseHelper.addUser(newUser);
//
//        if(insertData) {
//            toastMessage("Welcome to CalUber!");
//        }
//    }
//
//    private void toastMessage(String message) {
//        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
//    }

}
