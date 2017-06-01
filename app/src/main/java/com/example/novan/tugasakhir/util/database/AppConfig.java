package com.example.novan.tugasakhir.util.database;

/**
 * Created by Novan on 01/05/2017.
 */

public class AppConfig {
    static String DOMAIN = "websitegratis.esy.es";
    static String IP_HOME = "192.168.1.10";
    static String IP_PRIVATE = "192.168.43.166";

    // Server user login url
    public static String URL_LOGIN = "http://"+IP_HOME+"/android_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://"+IP_HOME+"/android_api/register.php";

    // Server user update url
    public static String URL_UPDATE = "http://"+IP_HOME+"/android_api/update.php";
}
