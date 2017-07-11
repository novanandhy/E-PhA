package com.example.novan.tugasakhir.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Novan on 11/07/2017.
 */

public class UserJSON extends ArrayList<Parcelable> implements Parcelable {

    private String uid_user;
    private String username;
    private String name;
    private String image;

    public UserJSON(){}

    public UserJSON(String uid_user, String username, String name, String image){
        this.uid_user = uid_user;
        this.username = username;
        this.name = name;
        this.image = image;
    }

    protected UserJSON(Parcel in) {
        uid_user = in.readString();
        username = in.readString();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<UserJSON> CREATOR = new Creator<UserJSON>() {
        @Override
        public UserJSON createFromParcel(Parcel in) {
            return new UserJSON(in);
        }

        @Override
        public UserJSON[] newArray(int size) {
            return new UserJSON[size];
        }
    };

    public String getUid_user() {
        return uid_user;
    }

    public void setUid_user(String uid_user) {
        this.uid_user = uid_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid_user);
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(image);
    }
}
