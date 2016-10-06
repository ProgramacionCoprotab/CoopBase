package com.coprotab.agarcia.coopbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by agarcia on 04/10/2016.
 */

public class CoopBaseDBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "coopbasedb.db";

    public static final String TABLA_RACK = "rack";
    public static final String RACK_ID = "_idrack";

    public static final String TABLA_FARDOXRACK = "fardoxrack";
    public static final String FXR_ID = "_id";
    public static final String FXRRACK_ID = "idrack";
    public static final String FXRFARDO_ID = "idfardo";
    public static final String FXRLEGAJO_ID =  "idlegajo";
    public static final String FXRFECHA = "fecha";

    private static final String SQL_CREAR1 = "create table "+ TABLA_RACK + "("
            + RACK_ID + " integer primary key)";

    private static final String SQL_CREAR2 = "create table " + TABLA_FARDOXRACK + "("
            + FXR_ID + " integer primary key autoincrement, "
            + FXRRACK_ID + " integer, "
            + FXRFARDO_ID + " integer, "
            + FXRLEGAJO_ID + " integer, "
            + FXRFECHA + " text)";

    public CoopBaseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREAR1);
        //db.execSQL(SQL_CREAR2);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    public void agregarRack(Integer cod){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(RACK_ID, cod);

        db.insert(TABLA_RACK, null, values);
        db.close();
    }

    public void agregarRackxFardo(Integer codfardo, Integer codrack, Integer legajo, String fecha){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FXRFARDO_ID, codfardo);
        values.put(FXRRACK_ID, codrack);
        values.put(FXRLEGAJO_ID,legajo);
        values.put(FXRFECHA,fecha);

        db.insert(TABLA_FARDOXRACK, null, values);
        db.close();
    }

    public boolean eliminarRackxFardo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLA_FARDOXRACK,
                    " " +FXRFARDO_ID+ " = ?",
                    new String[] { String.valueOf (id ) });
            db.close();
            return true;

        }catch(Exception ex){
            return false;
        }
    }

    public boolean eliminarRackxFardo() {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLA_FARDOXRACK, null, null);
            db.close();
            return true;

        }catch(Exception ex){
            return false;
        }
    }


    public boolean existeRack(int id){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] projection = {RACK_ID};

            Cursor cursor =
                    db.query(TABLA_RACK,
                            projection,
                            " " +RACK_ID+ " = ?",
                            new String[]{String.valueOf(id)},
                            null,
                            null,
                            null,
                            null);

            if (cursor != null)
                cursor.moveToFirst();

            db.close();
            return true;
        }
        catch (Exception ex){
            return false;
        }

    }


}
