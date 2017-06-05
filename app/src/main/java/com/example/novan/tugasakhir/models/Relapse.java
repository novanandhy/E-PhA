package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.database.DataHelper;

/**
 * Created by Novan on 05/06/2017.
 */

public class Relapse implements Parcelable {
    private int id;
    private String uid_user;
    private String latitude;
    private String longitude;
    private String date;
    private String month;
    private String year;
    private String hour;
    private String minute;

    public Relapse(String uid, String latitude, String longitude, String date, String month, String year, String hour, String minute){
        this.uid_user = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public Relapse(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_RELAPSE));
        this.uid_user = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_IDuser_RELAPSE));
        this.latitude = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_LATITUDE_RELAPSE));
        this.longitude = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_LONGITUDE_RELAPSE));
        this.date = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_DATE_RELAPSE));
        this.month = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_MONTH_RELAPSE));
        this.year = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_YEAR_RELAPSE));
        this.hour = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_HOUR_RELAPSE));
        this.minute = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_MINUTE_RELAPSE));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid_user() {
        return uid_user;
    }

    public void setUid_user(String uid_user) {
        this.uid_user = uid_user;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    protected Relapse(Parcel in) {
        id = in.readInt();
        uid_user = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        date = in.readString();
        month = in.readString();
        year = in.readString();
        hour = in.readString();
        minute = in.readString();
    }

    public static final Creator<Relapse> CREATOR = new Creator<Relapse>() {
        @Override
        public Relapse createFromParcel(Parcel in) {
            return new Relapse(in);
        }

        @Override
        public Relapse[] newArray(int size) {
            return new Relapse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uid_user);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(date);
        dest.writeString(month);
        dest.writeString(year);
        dest.writeString(hour);
        dest.writeString(minute);
    }
}
