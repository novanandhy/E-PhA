package com.example.novan.tugasakhir.util.database;

/**
 * Created by Novan on 01/05/2017.
 */

public class AppConfig {
    static String IP_HOME = "192.168.1.11";
    static String IP_PRIVATE = "192.168.43.83";

    // Server user login url
    public static String URL_LOGIN = "http://"+IP_PRIVATE+"/android_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://"+IP_PRIVATE+"/android_api/register.php";
}