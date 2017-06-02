package com.example.novan.tugasakhir.util.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.novan.tugasakhir.models.Contact;
import com.example.novan.tugasakhir.models.History;
import com.example.novan.tugasakhir.models.Locations;
import com.example.novan.tugasakhir.models.Medicine;
import com.example.novan.tugasakhir.models.Schedule;
import com.example.novan.tugasakhir.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Novan on 29/03/2017.
 */

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DBase_EphA";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "TAGapp";
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    //Table Name
    private static  final String TABLE_USER = "user";
    private static  final String TABLE_MEDICINE = "db_medic";
    private static  final String TABLE_SCHEDULE = "db_schedule";
    private static  final String TABLE_HISTORY = "db_history";
    private static  final String TABLE_RELAPSE = "db_relapse";
    private static  final String TABLE_LOCATION = "db_location";
    private static  final String TABLE_EPILEPTICS = "db_epileptic";
    private static  final String TABLE_CONTACTS = "db_contact";

    //Table User Data
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PREVILLAGE = "previllage";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_UID = "uid";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_CREATED_AT = "created_at";

    //Table Medicine Data
    public static final String COLUMN_ID_MEDICINE = "id_medicine";
    public static final String COLUMN_UID_MEDICINE = "uid_medicine";
    public static final String COLUMN_AMOUNT_MEDICINE = "amount_medicine";
    public static final String COLUMN_NAME_MEDICINE = "name_medicine";
    public static final String COLUMN_DOSAGE_MEDICINE = "dosage_medicine";
    public static final String COLUMN_REMAINS_MEDICINE = "remain_medicine";
    public static final String COLUMN_COUNT_MEDICINE = "count_medicine";

    //Table Schedule Data
    public static final String COLUMN_ID_SCHEDULE = "id_schedule";
    public static final String COLUMN_UIDMedicine_SCHEDULE = "uid_schedule";
    public static final String COLUMN_HOUR_SCHEDULE = "hour_schedule";
    public static final String COLUMN_MINUTE_SCHEDULE = "minute_schedule";
    public static final String COLUMN_STATUS_SCHEDULE = "status_schedule";

    //Table Contact Data
    public static final String COLUMN_ID_CONTACT = "id_contact";
    public static final String COLUMN_NUMBER_CONTACT = "number_contact";
    public static final String COLUMN_NAME_CONTACT = "name_contact";

    //Table History Data
    public static final String COLUMN_ID_HISTORY = "id_history";
    public static final String COLUMN_IDmedicine_HISTORY = "id_medicine";
    public static final String COLUMN_UIDuser_HISTORY = "uid_user";
    public static final String COLUMN_TIME_HISTORY = "time_history";
    public static final String COLUMN_STATUS_HISTORY = "status_history";

    //Table Location
    public static final String COLUMN_ID_LOCATION="id_location";
    public static final String COLUMN_LATITUDE_LOCATION="latitude_location";
    public static final String COLUMN_LONGITUDE_LOCATION="latitude_location";

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
    private static final String DB_MEDICINE = "create table db_medic (id_medicine integer primary key" +
            " AUTOINCREMENT, uid_medicine text, name_medicine text, amount_medicine integer , " +
            "dosage_medicine integer, count_medicine integer, remain_medicine integer );";
    private static final String DB_CONTACT = "create table db_contact (id_contact integer primary " +
            "key AUTOINCREMENT, name_contact text, number_contact text);";
    private static final String DB_SCHEDULE = "create table db_schedule (id_schedule integer primary " +
            "key AUTOINCREMENT, uid_schedule text, hour_schedule integer, minute_schedule integer, " +
            "status_schedule integer);";
    String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PREVILLAGE + " TEXT,"
            + KEY_USERNAME + " TEXT UNIQUE," + KEY_UID + " TEXT," + KEY_CREATED_AT + " TEXT," + KEY_PHOTO + " BLOB)";
    private static final String DB_HISTORY = "create table db_history (id_history integer primary key " +
            "AUTOINCREMENT, uid_user text, id_medicine integer, time_history date, status_history integer)";
    private static final String DB_LOCATION = "create table db_location (id_location integer primary " +
            "key AUTOINCREMENT, latitude_location text, longitude_location text);";



    private static final String createNullLocation = "insert into 'db_location' ('id_location', " +
            "'latitude_location', longitude_location) values (1,null,null);";
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DB_MEDICINE);
        db.execSQL(DB_CONTACT);
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(DB_SCHEDULE);
        db.execSQL(DB_HISTORY);
        db.execSQL(DB_LOCATION);

        try {
            db.execSQL(createNullLocation);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST db_medic");
        db.execSQL("DROP TABLE IF EXIST db_contact");
        db.execSQL("DROP TABLE IF EXIST db_schedule");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
    }


    //CRUD Medicine
    public Medicine save_medicine(String uid, String name, int amount, int dosage, int remain, int count){
        SQLiteDatabase db = this.getWritableDatabase();

        Medicine medicine = new Medicine(name,amount,remain,dosage,count);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UID_MEDICINE,uid);
        cv.put(COLUMN_NAME_MEDICINE,name);
        cv.put(COLUMN_AMOUNT_MEDICINE,amount);
        cv.put(COLUMN_DOSAGE_MEDICINE,dosage);
        cv.put(COLUMN_COUNT_MEDICINE,count);
        cv.put(COLUMN_REMAINS_MEDICINE,remain);
        addSchedule(uid,count);
        db.insert(TABLE_MEDICINE,null,cv);
        db.close();

        return medicine;
    }

    public Medicine update_medicine(int id, String name, int amount, int dosage, int remain, int count){
        SQLiteDatabase db = this.getWritableDatabase();

        Medicine medicine = new Medicine(name,amount,remain,dosage,count);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_MEDICINE,name);
        cv.put(COLUMN_AMOUNT_MEDICINE,amount);
        cv.put(COLUMN_DOSAGE_MEDICINE,dosage);
        cv.put(COLUMN_COUNT_MEDICINE,count);
        cv.put(COLUMN_REMAINS_MEDICINE,remain);
        db.update(TABLE_MEDICINE,cv,"id_medicine="+id,null);
        db.close();

        Log.d(TAG,"Success update medicine");

        return medicine;
    }

    public void delete_medicine(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINE,"id_medicine="+id, null);
        db.close();
        Log.d(TAG, "medicine by id= "+id+" has been deleted");
    }

    public ArrayList<Medicine> getAllMedicine(){
        ArrayList<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_MEDICINE +" ORDER BY "+COLUMN_ID_MEDICINE;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d(TAG,"size cursor medicine: "+cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                medicines.add(new Medicine(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return medicines;
    }

    public void clear_medicine() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_MEDICINE, null, null);
        db.close();

        Log.d(TAG, "Deleted all medicine info from sqlite");
    }

    //CRUS SCHEDULE
    public void addSchedule(String uid, int count) {
        SQLiteDatabase db = this.getWritableDatabase();

        int hour = 0;
        int minute = 0;
        int status = 0;

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UIDMedicine_SCHEDULE,uid);
        cv.put(COLUMN_HOUR_SCHEDULE, hour);
        cv.put(COLUMN_MINUTE_SCHEDULE, minute);
        cv.put(COLUMN_STATUS_SCHEDULE, status);
        for (int i = 0; i < count ; i++){
            try{
                db.insert(TABLE_SCHEDULE,null,cv);
                Log.d(TAG, "Fetching schedule form "+TABLE_SCHEDULE+" : "+cv.toString());
            }catch (Exception e){
            }

        }
    }

    public ArrayList<Schedule> getAllSchedule(String uid){
        ArrayList<Schedule> schedule = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_SCHEDULE +" WHERE "+COLUMN_UIDMedicine_SCHEDULE+" = '"+uid+"' ORDER BY "+COLUMN_ID_SCHEDULE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d(TAG,"size cursor schedule: "+cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                schedule.add(new Schedule(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return schedule;
    }

    public void update_schedule(int id, int hour, int minute, int status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HOUR_SCHEDULE,hour);
        cv.put(COLUMN_MINUTE_SCHEDULE,minute);
        cv.put(COLUMN_STATUS_SCHEDULE,status);

        db.update(TABLE_SCHEDULE,cv,"id_schedule="+id,null);
        Log.d(TAG,"fetching from schedule = "+cv.toString());
        db.close();
    }

    public void delete_schedule(String uid){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            String sql = "DELETE FROM "+TABLE_SCHEDULE+" WHERE "+COLUMN_UIDMedicine_SCHEDULE+" = '"+uid+"'";
            db.execSQL(sql);
        }catch (Exception e){
            Log.d(TAG,"schedule not deleted");
        }

        db.close();
    }

    //CRUD Contact
    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_CONTACTS +" ORDER BY "+COLUMN_ID_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d(TAG,"size cursor contact: "+cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                contacts.add(new Contact(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return contacts;
    }

    public Contact save_contact(String name, String number){

        SQLiteDatabase db = this.getWritableDatabase();
        Contact contact = new Contact(name,number);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_CONTACT,name);
        cv.put(COLUMN_NUMBER_CONTACT,number);
        db.insert(TABLE_CONTACTS,null,cv);
        db.close();

        return contact;
    }

    public void delete_contact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLE_CONTACTS,"id_contact="+id, null);
        }catch (Exception e){
            Log.d(TAG,"contact not deleted");
        }

        db.close();
    }

    public void clear_contact() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CONTACTS, null, null);
        db.close();

        Log.d(TAG, "Deleted all contacts info from sqlite");
    }

    //CRUD User
    /**
     * Storing user details in database
     * */
    public void addUser(String uid, String name, String previllage, String username, String created_at, byte[] img) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PREVILLAGE, previllage); // previllage
        values.put(KEY_USERNAME, username); // username
        values.put(KEY_UID, uid); // uid
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_PHOTO, img); // Created At

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

    }

    /**
     * Getting user data from database
     * */
    public ArrayList<User> getUserDetail(){
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_USER +" ORDER BY "+KEY_ID;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d(TAG,"size cursor user: "+cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                users.add(new User(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return users;
    }

    /**
     * update user detail
     * */
    public void update_user(int id, String username, String name, byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,name);
        cv.put(KEY_USERNAME,username);
        cv.put(KEY_PHOTO,img);

        db.update(TABLE_USER,cv,"id="+id,null);
        Log.d(TAG,"fetching from schedule = "+cv.toString());
        db.close();
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    //CRUD History
    public ArrayList<History> getAllHistory(){
        ArrayList<History> histories = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_HISTORY +" ORDER BY "+COLUMN_ID_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d(TAG,"size cursor history: "+cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                histories.add(new History(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return histories;
    }

    public ArrayList<History> getAllHistoryWhere(int status){
        ArrayList<History> histories = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_HISTORY +" WHERE "+COLUMN_STATUS_HISTORY+" = '"+status+"' ORDER BY "+COLUMN_ID_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d(TAG,"size cursor history where status = "+status+ ": " +cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                histories.add(new History(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return histories;
    }

    public void addStatusHistory(String uid_user, int id_medicine, int status_history) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_UIDuser_HISTORY, uid_user); // uid_user
        values.put(COLUMN_IDmedicine_HISTORY, id_medicine); // id_medicine
        values.put(COLUMN_STATUS_HISTORY, status_history); // id_medicine
        values.put(COLUMN_TIME_HISTORY, dateFormat.format(date)); // id_medicine

        Log.d(TAG, "Fetching history from Sqlite: " + values.toString());

        // Inserting Row
        try{
            db.insert(TABLE_HISTORY, null, values);
            db.close(); // Closing database connection
        }catch (Exception e){
            Log.d(TAG,"Failed add status history");
        }
    }

    //Update location
    public void update_location(String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LATITUDE_LOCATION,latitude);
        cv.put(COLUMN_LONGITUDE_LOCATION,longitude);

        try{
            db.update(TABLE_LOCATION,cv,"id_location = 1",null);
            Log.d(TAG,"Succes update location");
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"Failed update location");
        }
    }

    //show location
    public ArrayList<Locations> getLocation(){
        ArrayList<Locations> location = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_LOCATION +" ORDER BY "+COLUMN_ID_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        Log.d(TAG,"size cursor location: "+cursor.getCount());
        if(cursor.getCount() > 0 ) {
            do {
                location.add(new Locations(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return location;
    }

}



