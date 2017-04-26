package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.DataHelper;

/**
 * Created by Novan on 22/04/2017.
 */

public class Contact implements Parcelable{
    private String name;
    private String number;
    private int id;

    public Contact(String name, String number){
        this.name = name;
        this.number = number;
    }

    public Contact(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_CONTACT));
        this.number = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_NUMBER_CONTACT));
        this.name = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_NAME_CONTACT));
    }

    protected Contact(Parcel in){
        id = in.readInt();
        number = in.readString();
        name = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(number);
        dest.writeInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
