package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.database.DataHelper;

/**
 * Created by Novan on 30/05/2017.
 */

public class User implements Parcelable {

    int id;
    String name;
    String previllage;
    String username;
    String unique_id;
    String created_at;
    byte[] image;

    public User(String name, String previllage, String username, String unique_id, byte[] image){
        this.name = name;
        this.previllage = previllage;
        this.username = username;
        this.unique_id = unique_id;
        this.image = image;
    }

    public User(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.KEY_ID));
        this.name = cursor.getString(cursor.getColumnIndex(DataHelper.KEY_NAME));
        this.previllage = cursor.getString(cursor.getColumnIndex(DataHelper.KEY_PREVILLAGE));
        this.username = cursor.getString(cursor.getColumnIndex(DataHelper.KEY_USERNAME));
        this.unique_id = cursor.getString(cursor.getColumnIndex(DataHelper.KEY_UID));
        this.created_at = cursor.getString(cursor.getColumnIndex(DataHelper.KEY_CREATED_AT));
        this.image = cursor.getBlob(cursor.getColumnIndex(DataHelper.KEY_PHOTO));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrevillage() {
        return previllage;
    }

    public void setPrevillage(String previllage) {
        this.previllage = previllage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    protected User(Parcel in) {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
