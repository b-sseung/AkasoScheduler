package com.sseung.akasoschedule;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UseFunction {
    public static String DataBaseNAME = "SCHEDULE";
    public static Context mainContext;

    public static HashMap<Integer, Upload_Item> upload_list = new HashMap<>();

    //그냥 조회
    public static HashMap<String, Schedule_Item> loadDatabase(){

        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        String sql = "select _id, number, year, month, day, division, detail, name, time, image, uri, sale, source "
                + "from " + ScheduleDatabase.TABLE1 + " "
                + "order by year desc, month desc, day desc";

        Log.d("database", sql);

        Cursor cursor = database.rawQuery(sql);
        int recordCound = cursor.getCount();

        Log.d("tlqkf", String.valueOf(recordCound));

        HashMap<String, Schedule_Item> map = new HashMap<>();

        if (recordCound == 0) {
            return map;
        }


        for (int i = 0; i < recordCound; i++){
            cursor.moveToNext();

            int number = cursor.getInt(1);
            int year = cursor.getInt(2);
            int month = cursor.getInt(3);
            int day = cursor.getInt(4);
            String division = cursor.getString(5);
            String detail = cursor.getString(6);
            String name = cursor.getString(7);
            String time = cursor.getString(8);
            String image = cursor.getString(9);
            String uri = cursor.getString(10);
            String sale = cursor.getString(11);
            String source = cursor.getString(12);

            Log.d("tlqkf", "load : " + year + ",  " + month + ",  " + day + ",  " + division
                    + ",  " + detail + ",  " + name + ",  " + time + ",  " + image + ",  " + uri + ",  " + sale + ",  " + source);


            map.put(Integer.toString(number), new Schedule_Item(year, month, day, division, detail, name, time, image, uri, sale, source));
        }

        return map;
    }

    public static void addData(int number, Schedule_Item item) {
        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        String sql = "insert into " + ScheduleDatabase.TABLE1 +
                "(number, year, month, day, division, detail, name, time, image, uri, sale, source) values ("
                + "'" + number + "', "
                + "'" + item.getYear() + "', "
                + "'" + item.getMonth() + "', "
                + "'" + item.getDay() + "', "
                + "'" + item.getDivision() + "', "
                + "'" + item.getDetail() + "', "
                + "'" + item.getName() + "', "
                + "'" + item.getTime() + "', "
                + "'" + item.getImage() + "', "
                + "'" + item.getUri() + "', "
                + "'" + item.getSale() + "', "
                + "'" + item.getSource() + "')";

        Log.d("tlqkf", "sql : " + sql);

        database.execSQL(sql);
    }

    public static void updateData(int number, Schedule_Item item) {
        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        String sql = "update " + ScheduleDatabase.TABLE1 +
                " set "
                + " number = " + number + ""
                + ", year = " + item.getYear() + ""
                + ", month = " + item.getMonth() + ""
                + ", day = " + item.getDay() + ""
                + ", division = '" + item.getDivision() + "'"
                + ", detail = '" + item.getDetail() + "'"
                + ", name = '" + item.getName() + "'"
                + ", time = '" + item.getTime() + "'"
                + ", image = '" + item.getImage() + "'"
                + ", uri = '" + item.getUri() + "'"
                + ", sale = '" + item.getSale() + "'"
                + ", source = '" + item.getSource() + "'"
                + " where number = " + number;

        Log.d("tlqkf", "sql : " + sql);

        database.execSQL(sql);
    }

    public static boolean getloadPopUp(){

        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        String sql = "select _id, year, month, day, value "
                + "from " + ScheduleDatabase.TABLE2  + " ";

        Log.d("database", sql);

        Cursor cursor = database.rawQuery(sql);

        if (cursor.getCount() == 0) {
            setPopUpDB();
            cursor = database.rawQuery(sql);
        }

        cursor.moveToNext();

        int id = cursor.getInt(0);
        int year = cursor.getInt(1);
        int month = cursor.getInt(2);
        int day = cursor.getInt(3);
        String value = cursor.getString(4);

        Log.d("tlqkf", "load : " + year + ",  " + month + ",  " + day + ",  " + value);

        Calendar calendar = Calendar.getInstance();
        if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)) {
            if (value.equals("true")) { //true면 오픈, false면 패스
                return true;
            } else {
                return false;
            }
        } else {
            updatePopUpDB(id);
            return true;
        }
    }

    public static void updatePopUpDB(int id) {
        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        Calendar calendar = Calendar.getInstance();

        Log.d("tlqkf", calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH) + ", " + calendar.get(Calendar.DAY_OF_MONTH));

        String sql = "update " + ScheduleDatabase.TABLE2 +
                " set "
                + " year = " + calendar.get(Calendar.YEAR) + ""
                + ", month = " + calendar.get(Calendar.MONTH) + ""
                + ", day = " + calendar.get(Calendar.DAY_OF_MONTH) + ""
                + ", value = '" + "true" + "'"
                + " where _id = " + id;

        Log.d("tlqkf", "sql : " + sql);

        database.execSQL(sql);
    }

    public static void setPopUpDB() {
        ScheduleDatabase database = ScheduleDatabase.getInstance(UseFunction.mainContext);

        Calendar calendar = Calendar.getInstance();

        Log.d("tlqkf", calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH) + ", " + calendar.get(Calendar.DAY_OF_MONTH));

        String sql = "insert into " + ScheduleDatabase.TABLE2 +
                "(year, month, day, value) values ("
                + "'" + calendar.get(Calendar.YEAR) + "', "
                + "'" + calendar.get(Calendar.MONTH) + "', "
                + "'" + calendar.get(Calendar.DAY_OF_MONTH) + "', "
                + "'" + "true" + "')";

        Log.d("tlqkf", "sql : " + sql);

        database.execSQL(sql);
    }

    public static void addUploadList(int number, Upload_Item item){
        upload_list.put(number, item);
    }
}
