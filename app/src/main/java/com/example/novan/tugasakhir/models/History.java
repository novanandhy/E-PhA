package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.database.DataHelper;

/**
 * Created by Novan on 11/05/2017.
 */

public class History implements Parcelable {
    private int id;
    private String uid_user;
    private int id_medicine;
    private String status_history;
    private String date;
    private String month;
    private String year;

    public History(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_HISTORY));
        this.uid_user = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_UIDuser_HISTORY));
        this.id_medicine = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_IDmedicine_HISTORY));
        this.status_history = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_STATUS_HISTORY));
        this.date = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_DATE_HISTORY));
        this.month = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_MONTH_HISTORY));
        this.year = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_YEAR_HISTORY));
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

    public int getId_medicine() {
        return id_medicine;
    }

    public void setId_medicine(int id_medicine) {
        this.id_medicine = id_medicine;
    }

    public String getStatus_history() {
        return status_history;
    }

    public void setStatus_history(String status_history) {
        this.status_history = status_history;
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

    protected History(Parcel in) {
        id = in.readInt();
        uid_user = in.readString();
        id_medicine = in.readInt();
        status_history = in.readString();
        date = in.readString();
        month = in.readString();
        year = in.readString();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
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
        dest.writeInt(id_medicine);
        dest.writeString(status_history);
        dest.writeString(date);
        dest.writeString(month);
        dest.writeString(year);
    }
}
