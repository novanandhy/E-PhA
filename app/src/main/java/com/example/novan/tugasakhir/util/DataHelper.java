package com.example.novan.tugasakhir.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.novan.tugasakhir.models.Medicine;

import java.util.ArrayList;

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
    public static final String COLUMN_ID_MEDICINE = "id_medicine";
    public static final String COLUMN_AMOUNT_MEDICINE = "amount_medicine";
    public static final String COLUMN_NAME_MEDICINE = "name_medicine";
    public static final String COLUMN_DOSAGE_MEDICINE = "dosage_medicine";
    public static final String COLUMN_REMAINS_MEDICINE = "remain_medicine";
    public static final String COLUMN_COUNT_MEDICINE = "count_medicine";

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
    private static final String DB_MEDICINE = "create table db_medic (id_medicine integer primary key AUTOINCREMENT, name_medicine text, amount_medicine integer , dosage_medicine integer, count_medicine integer, remain_medicine integer );";
    private SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DB_MEDICINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST db_medic");
    }

    public Medicine save_medicine(String name, int amount, int dosage, int remain, int count){
        SQLiteDatabase db = this.getWritableDatabase();

        Medicine medicine = new Medicine(name,amount,remain,dosage,count);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_MEDICINE,name);
        cv.put(COLUMN_AMOUNT_MEDICINE,amount);
        cv.put(COLUMN_DOSAGE_MEDICINE,dosage);
        cv.put(COLUMN_COUNT_MEDICINE,count);
        cv.put(COLUMN_REMAINS_MEDICINE,remain);
        db.insert(TABLE_MEDICINE,null,cv);
        db.close();

        return medicine;
    }

    public void delete_medicine(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINE,"id_medicine="+id, null);
        db.close();
    }

    public ArrayList<Medicine> getAllMedicine(){
        ArrayList<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_MEDICINE +" ORDER BY "+COLUMN_ID_MEDICINE;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d("Epha","size cursor: "+cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                medicines.add(new Medicine(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return medicines;
    }

}



