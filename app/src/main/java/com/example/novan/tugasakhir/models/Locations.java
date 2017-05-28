package com.example.novan.tugasakhir.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.novan.tugasakhir.util.database.DataHelper;

/**
 * Created by Novan on 28/05/2017.
 */

public class Locations implements Parcelable {
    private int id;
    private String latitude;
    private String longitude;

    public Locations(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Locations(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DataHelper.COLUMN_ID_LOCATION));
        this.latitude = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_LATITUDE_LOCATION));
        this.longitude = cursor.getString(cursor.getColumnIndex(DataHelper.COLUMN_LONGITUDE_LOCATION));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    protected Locations(Parcel in) {
        id = in.readInt();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
