package com.sseung.akasoschedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ScheduleDatabase {

    private static ScheduleDatabase database;
    public static String TABLE1 = "SCHEDULE";
    public static String TABLE2 = "POPUP";
    public static int DATABASE_VERSION = 1;

    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private static Context context;

    private ScheduleDatabase(Context context) {
        this.context = context;
    }
    public static void print(String text){
        Log.d("database", text);
    }

    public static ScheduleDatabase getInstance(Context context){
        if (database == null){
            print("database null");
            database = new ScheduleDatabase(context);
        }

        return database;
    }

    public void create(){
        dbHelper.onCreate(db);
    }

    public static boolean open(){
        print("opening database [" + UseFunction.DataBaseNAME + "].");


        dbHelper = new DatabaseHelper(context, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close(){
        print("closing database [" + UseFunction.DataBaseNAME + "].");

        db.close();
        database = null;
    }

    public Cursor rawQuery(String SQL){
        print("\nexecuteQuery called.\n");

        Cursor cursor = null;

        try{
            cursor = db.rawQuery(SQL, null);
            print("cursor count : " + cursor.getCount());
        } catch (Exception e){
            print("Exception in executeQuery : " + e);
        }

        return cursor;
    }

    public boolean execSQL(String SQL){
        print("\nexecute called.\n");

        try{
            print("SQL : " + SQL);
            db.execSQL(SQL);
        } catch (Exception e){
            print("Exception in executeQuery : " + e);
            return false;
        }

        return true;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, int version){
            super(context, UseFunction.DataBaseNAME, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            print("creating database [" + UseFunction.DataBaseNAME + "].");

            print("creating table [" + TABLE1 + "].");

            String DROP_SQL1 = "drop table if exists " + TABLE1;

            try{
                db.execSQL(DROP_SQL1);
            } catch (Exception e) {print("Exception in DROP_SQL1 : " + e);}

            String CREATE_SQL1 = "create table " + TABLE1 + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " number INTEGER DEFAULT '', "
                    + " year INTEGER DEFAULT '', "
                    + " month INTEGER DEFAULT '', "
                    + " day INTEGER DEFAULT '', "
                    + " division TEXT DEFAULT '', "
                    + " detail TEXT DEFAULT '', "
                    + " name TEXT DEFAULT '', "
                    + " time TEXT DEFAULT '', "
                    + " image TEXT DEFAULT '', "
                    + " uri TEXT DEFAULT '', "
                    + " sale TEXT DEFAULT '', "
                    + " source TEXT DEFAULT '' "
                    + ")";

            try {
                db.execSQL(CREATE_SQL1);
            } catch (Exception e){print("Exception in CREATE_SQL1 : " + e);}

            String CREATE_INDEX_SQL1 = "create index " + TABLE1 + "_IDX ON " + TABLE1 + "("
                    + "CREATE_DATE" + ")";

            try {
                db.execSQL(CREATE_INDEX_SQL1);
            } catch (Exception e) {
                print("Exception in CREATE_INDEX_SQL1 : " + e);
            }


            print("creating table [" + TABLE2 + "].");

            String DROP_SQL2 = "drop table if exists " + TABLE2;

            try{
                db.execSQL(DROP_SQL2);
            } catch (Exception e) {print("Exception in DROP_SQL2 : " + e);}

            String CREATE_SQL2 = "create table " + TABLE2 + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " year INTEGER DEFAULT '', "
                    + " month INTEGER DEFAULT '', "
                    + " day INTEGER DEFAULT '', "
                    + " value TEXT DEFAULT ''"
                    + ")";

            try {
                db.execSQL(CREATE_SQL2);
            } catch (Exception e){print("Exception in CREATE_SQL2 : " + e);}

            String CREATE_INDEX_SQL2 = "create index " + TABLE2 + "_IDX ON " + TABLE2 + "("
                    + "CREATE_DATE" + ")";

            try {
                db.execSQL(CREATE_INDEX_SQL2);
            } catch (Exception e) {
                print("Exception in CREATE_INDEX_SQL2 : " + e);
            }
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);

            print("opened database [" + UseFunction.DataBaseNAME + "].");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            print("upgrade database from version " + oldVersion + " to " + newVersion + ".");
        }
    }
}
