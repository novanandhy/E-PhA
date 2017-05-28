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
    private int status_history;

    public History(int id, String uid_user, int id_medicine, int status_history){
        this.id = id;
        this.uid_user = uid_user;
        this.id_medicine = id_medicine;
        this.status_history = status_history;
    }

    public History(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_HISTORY));
        this.uid_user = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_UIDuser_HISTORY));
        this.id_medicine = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_IDmedicine_HISTORY));
        this.status_history = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_STATUS_HISTORY));
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

    public int getStatus_history() {
        return status_history;
    }

    public void setStatus_history(int status_history) {
        this.status_history = status_history;
    }

    protected History(Parcel in) {
        id = in.readInt();
        uid_user = in.readString();
        id_medicine = in.readInt();
        status_history = in.readInt();
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
        dest.writeInt(status_history);
    }
}
