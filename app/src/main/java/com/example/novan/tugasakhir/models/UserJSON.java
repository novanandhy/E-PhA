package com.example.novan.tugasakhir.models;

/**
 * Created by Novan on 11/07/2017.
 */

public class UserJSON {

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
}
