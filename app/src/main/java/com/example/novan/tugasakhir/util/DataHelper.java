package com.example.novan.tugasakhir.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Novan on 29/03/2017.
 */

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DBase_EphA";
    private static final int DATABASE_VERSION = 1;
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    //Table Name
    private static  final String TABLE_USER = "db_user";
    private static  final String TABLE_MEDICINE = "db_medic";
    private static  final String TABLE_SCHEDULE = "db_schedule";
    private static  final String TABLE_HISTORY = "db_history";
    private static  final String TABLE_RELAPSE = "db_relapse";
    private static  final String TABLE_EPILEPTICS = "db_epileptic";
    private static  final String TABLE_CONTACTS = "db_contact";

    //Table User Data
    private static final String COLUMN_ID_USER = "id_user";
    private static final String COLUMN_NAME_USER = "name_user";
    private static final String COLUMN_USERNAME_USER = "username_user";
    private static final String COLUMN_PASSWORD_USER = "password_user";

    //Table Medicine Data
    private static final String COLUMN_ID_MEDICINE = "id_medicine";
    private static final String COLUMN_AMOUNT_MEDICINE = "amount_medicine";
    private static final String COLUMN_NAME_MEDICINE = "name_medicine";
    private static final String COLUMN_DOSAGE_MEDICINE = "dosage_medicine";
    private static final String COLUMN_REMAINS_MEDICINE = "remain_medicine";
    private static final String COLUMN_COUNT_MEDICINE = "count_medicine";

    //Table Schedule Data
    private static final String COLUMN_ID_SCHEDULE = "id_schedule";
    private static final String COLUMN_IDmedicine_SCHEDULE = "id_medicine";
    private static final String COLUMN_TIME_SCHEDULE = "time_schedule";

    //Table History Data
    private static final String COLUMN_ID_HISTORY = "id_history";
    private static final String COLUMN_IDmedicine_HISTORY = "id_medicine";
    private static final String COLUMN_IDuser_HISTORY = "id_user";
    private static final String COLUMN_TIME_HISTORY = "time_history";
    private static final String COLUMN_STATUS_HISTORY = "status_history";

    //Table Contact Data
    private static final String COLUMN_ID_CONTACT = "id_contact";
    private static final String COLUMN_NUMBER_CONTACT = "number_contact";
    private static final String COLUMN_NAME_CONTACT = "name_contact";

    //Table Relapse Data
    private static final String COLUMN_ID_RELAPSE = "id_relapse";
    private static final String COLUMN_IDuser_RELAPSE = "id_user";
    private static final String COLUMN_LOCATION_RELAPSE = "location_relapse";
    private static final String COLUMN_TIME_RELAPSE = "time_relapse";

    //Table Epileptics Data
    private static final String COLUMN_ID_EPILEPTICS = "id_epileptic";
    private static final String COLUMN_IDuser_EPILEPTICS = "id_user";
    private static final String COLUMN_NAME_EPILEPTICS = "name_epileptic";

    //Create Table
    private static final String DB_USER = "create table "+TABLE_USER+" ("+COLUMN_ID_USER+" integer primary key, "+COLUMN_NAME_USER+" text, "+COLUMN_USERNAME_USER+" text, "+COLUMN_PASSWORD_USER+" text, "+COLUMN_REMAINS_MEDICINE+" integer)";
    private static final String DB_MEDICINE = "create table "+TABLE_MEDICINE+" ("+COLUMN_ID_MEDICINE+" integer primary key AUTOINCREMENT, "+COLUMN_NAME_MEDICINE+" text, "+COLUMN_AMOUNT_MEDICINE+" integer, "+COLUMN_DOSAGE_MEDICINE+" integer, "+COLUMN_COUNT_MEDICINE+" integer)";
    private static final String DB_SCHEDULE = "create table "+TABLE_SCHEDULE+" ("+COLUMN_ID_SCHEDULE+" integer primary key AUTOINCREMENT, "+COLUMN_IDmedicine_SCHEDULE+" integer, "+COLUMN_TIME_SCHEDULE+" text)";
    private static final String DB_HISTORY = "create table "+TABLE_HISTORY+" ("+COLUMN_ID_HISTORY+" integer primary key AUTOINCREMENT, "+COLUMN_IDmedicine_HISTORY+" integer, "+COLUMN_IDuser_HISTORY+" integer, "+COLUMN_STATUS_HISTORY+" text, "+COLUMN_TIME_HISTORY+" text)";
    private static final String DB_CONTACT = "create table "+TABLE_CONTACTS+" ("+COLUMN_ID_CONTACT+" integer primary key AUTOINCREMENT, "+COLUMN_NUMBER_CONTACT+" integer, "+COLUMN_NAME_CONTACT+" text)";
    private static final String DB_RELAPSE = "create table "+TABLE_RELAPSE+" ("+COLUMN_ID_RELAPSE+" integer primary key AUTOINCREMENT, "+COLUMN_IDuser_RELAPSE+" integer, "+COLUMN_LOCATION_RELAPSE+" text, "+COLUMN_TIME_RELAPSE+" text)";
    private static final String DB_EPIEPTIC = "create table "+TABLE_EPILEPTICS+" ("+COLUMN_ID_EPILEPTICS+" integer primary key AUTOINCREMENT, "+COLUMN_IDuser_EPILEPTICS+" integer, "+COLUMN_NAME_EPILEPTICS+" text)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
//        String sql = "create table biodata(no integer primary key, nama text null, tgl text null, jk text null, alamat text null);";
//        Log.d("Data", "onCreate: " + sql);
//        Log.i("Data",DB_USER);
//        Log.i("Data",DB_MEDICINE);
//        Log.i("Data",DB_SCHEDULE);
//        Log.i("Data",DB_HISTORY);
//        Log.i("Data",DB_CONTACT);
//        Log.i("Data",DB_RELAPSE);
//        Log.i("Data",DB_EPIEPTIC);
        db.execSQL(DB_MEDICINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
