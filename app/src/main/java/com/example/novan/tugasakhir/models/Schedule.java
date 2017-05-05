package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.DataHelper;

/**
 * Created by Novan on 04/05/2017.
 */

public class Schedule implements Parcelable {
    int id;
    String medicine_name;
    int hour;
    int minute;
    int status;

    public Schedule(String medicine_name, int hour, int minute, int status){
        this.medicine_name = medicine_name;
        this.hour = hour;
        this.minute = minute;
        this.status = status;
    }

    public Schedule(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_SCHEDULE));
        this.medicine_name = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_NameMadicine_SCHEDULE));
        this.hour = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_HOUR_SCHEDULE));
        this.minute = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_MINUTE_SCHEDULE));
        this.status = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_STATUS_SCHEDULE));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected Schedule(Parcel in) {
        id = in.readInt();
        medicine_name = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        status = in.readInt();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(medicine_name);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(status);
    }
}
