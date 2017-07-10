package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.database.DataHelper;

/**
 * Created by Novan on 10/07/2017.
 */

public class Friends implements Parcelable{

    int id;
    String uid_user;

    public Friends(String uid_user){
        this.uid_user = uid_user;
    }

    public Friends(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_FRIEND));
        this.uid_user = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_IDuser_FRIEND));
    }


    protected Friends(Parcel in) {
        id = in.readInt();
        uid_user = in.readString();
    }

    public static final Creator<Friends> CREATOR = new Creator<Friends>() {
        @Override
        public Friends createFromParcel(Parcel in) {
            return new Friends(in);
        }

        @Override
        public Friends[] newArray(int size) {
            return new Friends[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uid_user);
    }
}
